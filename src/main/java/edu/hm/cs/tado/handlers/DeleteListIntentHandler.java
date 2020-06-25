package edu.hm.cs.tado.handlers;

import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoManager;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.hm.cs.tado.PhrasesAndConstants.LIST_MISSING;

public class DeleteListIntentHandler extends AbstractListRequestHandler {
    public DeleteListIntentHandler() {
        super("DeleteListIntent");
    }


    @Override
    public Optional<Response> handle(TaDoManager manager, Map<String, Slot> slots, ResponseBuilder responseBuilder) {
        String speechText;
        try {
            transformManager(m -> m.getLists().clear());

            speechText = PhrasesAndConstants.LIST_DELETED;
        } catch (
                NoSuchElementException e) {
            speechText = LIST_MISSING;
        }
        return responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false).build();
    }

}
