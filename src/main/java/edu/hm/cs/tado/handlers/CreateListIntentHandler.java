package edu.hm.cs.tado.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TaDoManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.tado.PhrasesAndConstants.MAPPER;

public class CreateListIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("CreateListIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        if (((List) persistentAttributes.get("lists")).isEmpty()) {

            TaDoManager manager = MAPPER.convertValue(persistentAttributes, TaDoManager.class);


            manager.getLists().add(new TaDoList());

            Map<String, Object> attributesMap = MAPPER.convertValue(manager, new TypeReference<Map<String, Object>>() {
            });

            attributesManager.setPersistentAttributes(attributesMap);
            attributesManager.savePersistentAttributes();

            String speechText = PhrasesAndConstants.LIST_CREATED;
            responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
                    .withSpeech(speechText)
                    .withShouldEndSession(false);

        } else {
            responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, PhrasesAndConstants.LIST_ALREADY_EXISTS)
                    .withSpeech(PhrasesAndConstants.LIST_ALREADY_EXISTS)
                    .withShouldEndSession(false);
        }

        return responseBuilder.build();
    }

}
