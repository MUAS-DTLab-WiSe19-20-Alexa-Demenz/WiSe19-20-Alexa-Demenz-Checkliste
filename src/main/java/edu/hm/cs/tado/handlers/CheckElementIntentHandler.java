package edu.hm.cs.tado.handlers;

import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoManager;
import edu.hm.cs.tado.service.TaDoListService;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.hm.cs.tado.PhrasesAndConstants.*;

public class CheckElementIntentHandler extends AbstractListRequestHandler {
    public CheckElementIntentHandler() {
        super("CheckElementIntent");
    }

    @Override
    public Optional<Response> handle(TaDoManager manager, Map<String, Slot> slots, ResponseBuilder responseBuilder) {

        try {
            transformManager( m -> {
                String name = slots.get(ELEMENT_NAME).getValue();
                new TaDoListService(m.getLists().get(0)).checkElement(name);
            });

            responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, ELEMENT_CHECKED)
                .withSpeech(ELEMENT_CHECKED);
        }catch (NoSuchElementException e){
            responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, NO_SUCH_ELEMENT)
                    .withSpeech(NO_SUCH_ELEMENT);
        }

        return responseBuilder.withShouldEndSession(false).build();
    }

}
