package edu.hm.cs.tado.handlers;

import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TaDoManager;

import java.util.Map;
import java.util.Optional;

import static edu.hm.cs.tado.PhrasesAndConstants.CARD_TITLE;

public class WhatsToDoOnThisListIntentHandler extends AbstractListRequestHandler{

    public WhatsToDoOnThisListIntentHandler() {
        super("WhatsToDoOnThisListIntent");
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
     * Generates an output String that can be used to read out the unchecked elements of a list
     *
     * @param list list which unchecked elements are to be read out
     * @return returns a simple concatenated String with the names of all elements that are unchecked
     */
    private String makeSimpleListOutput(TaDoList list) {
        if(list.getElements(false).isEmpty()){
            return "Es liegen aktuell keine Aufgaben vor, die noch zu erledigen sind.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Ich werde jetzt die Aufgaben der Liste aufzählen, die noch zu erledigen sind.\n");
        list.getElements(false).forEach(e -> sb.append(e.getName()+"\n"));
        sb.append("Das sind alle Aufgaben gewesen, die noch zu erledigen sind!");
        return sb.toString();
    }
}
