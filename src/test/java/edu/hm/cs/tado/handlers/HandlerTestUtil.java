package edu.hm.cs.tado.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TaDoManager;
import edu.hm.cs.tado.model.TodoElement;
import org.mockito.Mockito;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HandlerTestUtil {

    static TaDoManager mockManager() {
        TaDoManager manager = new TaDoManager();
        manager.setLists(
                Collections.singletonList(
                        new TaDoList("test", Collections.singletonList(
                                new TodoElement("test")
                        )
                        )));

        return manager;
    }

    static Map<String, Object> mockPeristentAttributes(TaDoManager manager){
        return PhrasesAndConstants.MAPPER.convertValue(manager, new TypeReference<Map<String, Object>>() {});
    }

    static HandlerInput mockHandlerInput(String name,
                                         Map<String, com.amazon.ask.model.Slot> slots,
                                         Map<String, Object> sessionAttributes,
                                         Map<String, Object> persistentAttributes,
                                         Map<String, Object> requestAttributes) {
        return mockHandlerInput(
                IntentRequest.builder().withIntent(Intent.builder().withName(name).withSlots(slots).build()).build()
                , sessionAttributes, persistentAttributes, requestAttributes);
    }

    static HandlerInput mockHandlerInput(Request request,
                                         Map<String, Object> sessionAttributes,
                                         Map<String, Object> persistentAttributes,
                                         Map<String, Object> requestAttributes) {
        final AttributesManager attributesManagerMock = Mockito.mock(AttributesManager.class);
        when(attributesManagerMock.getSessionAttributes()).thenReturn(sessionAttributes);
        when(attributesManagerMock.getPersistentAttributes()).thenReturn(persistentAttributes);
        when(attributesManagerMock.getRequestAttributes()).thenReturn(requestAttributes);

        return mockHandlerInput(request, attributesManagerMock);
    }

    static HandlerInput mockHandlerInput(String name, Map<String, com.amazon.ask.model.Slot> slots,
                                         AttributesManager attributesManager) {
        return mockHandlerInput(
                IntentRequest.builder().withIntent(Intent.builder().withName(name).withSlots(slots).build()).build()
                , attributesManager);
    }

    @SuppressWarnings("WeakerAccess")
    static HandlerInput mockHandlerInput(Request request,
                                         AttributesManager attributesManager) {

        // Mock Slots
        final RequestEnvelope requestEnvelopeMock = RequestEnvelope.builder()
                .withRequest(request)
                .build();


        // Mock Handler input attributes
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        when(input.getAttributesManager()).thenReturn(attributesManager);
        when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
        when(input.getRequestEnvelope()).thenReturn(requestEnvelopeMock);
        when(input.matches(any())).thenCallRealMethod();

        return input;
    }

    static Response standardTestForHandle(RequestHandler handler) {
        final Map<String, Object> sessionAttributes = new HashMap<>();
        final Map<String, Object> persistentAttributes = new HashMap<>();
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput(null, sessionAttributes, persistentAttributes, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        //assertFalse(response.getShouldEndSession());
        assertNotEquals("Test", response.getReprompt());
        assertNotNull(response.getOutputSpeech());
        return response;
    }

    static Response sessionEndedTestForHandle(RequestHandler handler) {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput(null, null, null, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();
        assertTrue(response.getShouldEndSession());
        return response;
    }

}

