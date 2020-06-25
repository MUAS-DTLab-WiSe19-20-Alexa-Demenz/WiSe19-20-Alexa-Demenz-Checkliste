package edu.hm.cs.tado.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ServiceException;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoManager;
import edu.hm.cs.tado.service.MailSenderService;
import edu.hm.cs.tado.service.TaDoListRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.tado.PhrasesAndConstants.MAPPER;

public class SendListMailIntentHandler implements RequestHandler {

    public static final String MAIL_SUBJECT = "Ihre TaDo Liste";
    private static final String DAY_INTENT_NAME = "SendListMailIntent";
    private static final String WEEK_INTENT_NAME = "SendWeekListMailIntent";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MailSenderService mailSender;

    public SendListMailIntentHandler(MailSenderService mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(DAY_INTENT_NAME))
                || input.matches(intentName(WEEK_INTENT_NAME));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        String emailAddress;
        // Versuch, die mit dem Amazon Account des Users verbundene Email Addresse abzufragen
        try {
            emailAddress = input.getServiceClientFactory().getUpsService().getProfileEmail();
        } catch (ServiceException e) {
            return buildAskForPermissionResponse(responseBuilder);
        }

        TaDoManager manager = MAPPER.convertValue(persistentAttributes, TaDoManager.class);

        // Senden der Liste (falls vorhanden)
        if (!manager.getLists().isEmpty()) {
            if (input.matches(intentName(DAY_INTENT_NAME))) {
                sendMail(manager, TaDoListRenderer::render, emailAddress, responseBuilder);
            } else {
                sendMail(manager, TaDoListRenderer::renderWeek, emailAddress, responseBuilder);
            }
        } else {
            buildResponseWithText(responseBuilder, PhrasesAndConstants.THERE_ARE_NO_LISTS);
        }
        return responseBuilder.build();
    }

    private void sendMail(TaDoManager manager, Function<TaDoListRenderer, String> renderFunction,
                          String emailAddress, ResponseBuilder responseBuilder) {
        try {
            TaDoListRenderer renderer = new TaDoListRenderer(manager.getLists().get(0));
            mailSender.sendMail(MAIL_SUBJECT, renderFunction.apply(renderer),
                    InternetAddress.parse(emailAddress));
            String speechText = PhrasesAndConstants.MAIL_SENT;
            buildResponseWithText(responseBuilder, speechText);
        } catch (AddressException e) {
            logger.error("couldn't open mail.properties file");
            buildResponseWithText(responseBuilder, PhrasesAndConstants.CANT_SEND_MAIL);
        }
    }

    private void buildResponseWithText(ResponseBuilder responseBuilder, String text) {
        responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, text)
                .withSpeech(text)
                .withShouldEndSession(false);
    }

    /* Wenn der Benutzer noch keine Verwendungserlaubnis für seine Email Addresse gegeben hat,
    dann wird hier eine Karte verschickt, die ihn dazu auffordert */
    private Optional<Response> buildAskForPermissionResponse(ResponseBuilder responseBuilder) {
        List<String> neededPermissions = new ArrayList<>();
        neededPermissions.add("alexa::profile:email:read");
        return responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, PhrasesAndConstants.GIVE_MAIL_PERMISSION)
                .withSpeech(PhrasesAndConstants.GIVE_MAIL_PERMISSION)
                .withAskForPermissionsConsentCard(neededPermissions)
                .withShouldEndSession(false).build();
    }

}
