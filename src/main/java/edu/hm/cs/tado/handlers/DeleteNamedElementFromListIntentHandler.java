package edu.hm.cs.tado.handlers;

import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.model.TaDoManager;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.hm.cs.tado.PhrasesAndConstants.*;

public class DeleteNamedElementFromListIntentHandler extends AbstractListRequestHandler {

    public DeleteNamedElementFromListIntentHandler() {
        super("DeleteNamedElementFromListIntent");
    }

    @Override
    public Optional<Response> handle(TaDoManager manager, Map<String, Slot> slots, ResponseBuilder responseBuilder) {
        String responseText;
        Slot elementName = slots.get(ELEMENT_NAME);
        try {
            transformManager(m ->
                    m.getLists().get(0).removeElement(elementName.getValue())
            );
            responseText = "Die Aufgabe " + elementName.getValue() + " wurde aus der Liste gelöscht!";
        } catch (NoSuchElementException e) {
            responseText = LIST_MISSING;
        }

        return responseBuilder.withSimpleCard(CARD_TITLE, responseText)
                .withSpeech(responseText)
                .withShouldEndSession(false)
                .build();
    }
}
