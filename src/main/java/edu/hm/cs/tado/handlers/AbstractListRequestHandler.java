package edu.hm.cs.tado.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;

import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.response.ResponseBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.hm.cs.tado.model.TaDoManager;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static edu.hm.cs.tado.PhrasesAndConstants.MAPPER;

abstract public class AbstractListRequestHandler implements RequestHandler {

    private HandlerInput input;
    private final String name;

    public AbstractListRequestHandler(String intentName) {
        this.name = intentName;
    }

    abstract public Optional<Response> handle(TaDoManager manager, Map<String, Slot> slots, ResponseBuilder responseBuilder);

    @Override
    public boolean canHandle(HandlerInput input) {
        return hasList(input) && input.matches(intentName(name));
    }

    private boolean hasList(HandlerInput input) {
        try {
            Map<String, Object> persistentAttributes = input.getAttributesManager().getPersistentAttributes();
            TaDoManager manager = MAPPER.convertValue(persistentAttributes, TaDoManager.class);
            return !manager.getLists().isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        this.input = handlerInput;

        IntentRequest request = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();
        Intent intent = request.getIntent();
        return handle(
                getMappedManager(),
                intent.getSlots(),
                input.getResponseBuilder()
        );
    }

    public void transformManager(Consumer<TaDoManager> f) {
        TaDoManager manager = getMappedManager();
        f.accept(manager);
        saveMappedManager(manager);
    }

    public void saveMappedManager(TaDoManager manager) {
        Map<String, Object> attributesMap = MAPPER.convertValue(manager, new TypeReference<Map<String, Object>>() {
        });
        AttributesManager attributesManager = input.getAttributesManager();
        attributesManager.setPersistentAttributes(attributesMap);
        attributesManager.savePersistentAttributes();
    }

    private TaDoManager getMappedManager() {
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();
        return MAPPER.convertValue(persistentAttributes, TaDoManager.class);
    }
}
