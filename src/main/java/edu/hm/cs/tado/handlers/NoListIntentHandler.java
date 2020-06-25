package edu.hm.cs.tado.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import edu.hm.cs.tado.model.TaDoManager;

import java.util.Map;
import java.util.Optional;

import static edu.hm.cs.tado.PhrasesAndConstants.*;

public class NoListIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        Map<String, Object> persistentAttributes = input.getAttributesManager().getPersistentAttributes();
        TaDoManager manager = MAPPER.convertValue(persistentAttributes, TaDoManager.class);
        return manager.getLists().isEmpty();
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder().withShouldEndSession(false)
                .withSimpleCard(CARD_TITLE, THERE_ARE_NO_LISTS)
                .withSpeech(THERE_ARE_NO_LISTS)
                .build();
    }
}
