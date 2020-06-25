package edu.hm.cs.tado.handlers;

import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoManager;
import edu.hm.cs.tado.model.TodoElement;
import edu.hm.cs.tado.utils.UniqueConstrainException;

import java.util.Map;
import java.util.Optional;

public class AddToListIntentHandler extends AbstractListRequestHandler {

    public AddToListIntentHandler() {
        super("AddToListIntent");
    }

    @Override
    public Optional<Response> handle(TaDoManager manager, Map<String, Slot> slots, ResponseBuilder responseBuilder) {
        String elementName = slots.get(PhrasesAndConstants.ELEMENT_NAME).getValue();
        String responseText;
        try {
            transformManager(x -> x.getLists().get(0).addElement(
                    new TodoElement(elementName)));
            responseText = "Die Aufgabe " + elementName + " wurde der Liste hinzugefügt!";
        } catch (UniqueConstrainException e) {
            responseText = "Das Element " + elementName + " existiert bereits";
        }

        return responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, responseText)
                .withSpeech(responseText)
                .withShouldEndSession(false)
                .build();
    }
}
