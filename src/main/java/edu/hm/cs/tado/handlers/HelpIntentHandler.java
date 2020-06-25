package edu.hm.cs.tado.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoManager;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.tado.PhrasesAndConstants.MAPPER;

public class HelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder responseBuilder = input.getResponseBuilder();
        if (hasList(input)) {
            return createResponse(responseBuilder, PhrasesAndConstants.HELP);
        }
        return createResponse(responseBuilder, PhrasesAndConstants.HELP_CREATE_LIST);
    }

    private Optional<Response> createResponse(ResponseBuilder responseBuilder, String text) {
        return responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, text)
                .withSpeech(text)
                .withShouldEndSession(false)
                .build();
    }

    private boolean hasList(HandlerInput input) {
        Map<String, Object> persistentAttributes = input.getAttributesManager().getPersistentAttributes();
        TaDoManager manager = MAPPER.convertValue(persistentAttributes, TaDoManager.class);
        return !manager.getLists().isEmpty();
    }
}
