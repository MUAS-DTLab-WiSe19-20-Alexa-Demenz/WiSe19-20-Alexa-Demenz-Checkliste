package edu.hm.cs.tado.handlers;

import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TaDoManager;
import edu.hm.cs.tado.model.TodoElement;

import java.util.Map;
import java.util.Optional;

public class WhatsOnThisListIntentHandler extends AbstractListRequestHandler {

    public WhatsOnThisListIntentHandler() {
        super("WhatsOnThisListIntent");
    }

    @Override
    public Optional<Response> handle(TaDoManager manager, Map<String, Slot> slots, ResponseBuilder responseBuilder) {
        String responseText = makeSimpleListOutput(manager.getLists().get(0));
        return responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, responseText)
                .withSpeech(responseText)
                .withShouldEndSession(false)
                .build();
    }

    /**
     * Generates an output String that can be used to read out the content of a list
     *
     * @param list list which elements are to be read out
     * @return For now only returns a simple concatenated String with the names of all elements
     * (Optional: and whether they are checked or not)
     */
    private String makeSimpleListOutput(TaDoList list) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ich werde jetzt die Aufgaben der Liste aufzählen.\n");

        for (TodoElement e : list.getElements()) {
            sb.append(e.getName());
            sb.append(".\n");
        }

        sb.append("Das sind alle Aufgaben gewesen!");
        return sb.toString();
    }
}
