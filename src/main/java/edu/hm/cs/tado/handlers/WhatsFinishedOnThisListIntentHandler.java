package edu.hm.cs.tado.handlers;

import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TaDoManager;

import java.util.Map;
import java.util.Optional;

import static edu.hm.cs.tado.PhrasesAndConstants.CARD_TITLE;

public class WhatsFinishedOnThisListIntentHandler extends AbstractListRequestHandler {

    public WhatsFinishedOnThisListIntentHandler() {
        super("WhatsFinishedOnThisListIntent");
    }

    @Override
    public Optional<Response> handle(TaDoManager manager, Map<String, Slot> slots, ResponseBuilder responseBuilder) {
        String responseText = makeSimpleListOutput(manager.getLists().get(0));

        return responseBuilder.withSimpleCard(CARD_TITLE, responseText)
                .withSpeech(responseText)
                .withShouldEndSession(false)
                .build();
    }

    /**
     * Generates an output String that can be used to read out the content of a list
     *
     * @param list list which elements are to be read out
     * @return For now only returns a simple concatenated String with the names of all elements and whether they are checked or not
     */
    private String makeSimpleListOutput(TaDoList list) {
        if (list.getElements(true).isEmpty()) {
            return "Es liegen aktuell keine Aufgaben vor, die schon erledigt sind.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Ich werde jetzt die Aufgaben der Liste aufzählen, die schon erledigen sind.\n");
        list.getElements(true).forEach(e -> sb.append(e.getName() + "\n"));
        sb.append("Das sind alle Aufgaben gewesen, die schon erledigt sind!");
        return sb.toString();
    }
}
