package edu.hm.cs.tado.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TaDoManager;
import edu.hm.cs.tado.model.TodoElement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.*;

import static edu.hm.cs.tado.PhrasesAndConstants.MAPPER;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddToListIntentHandlerTest {

    private AddToListIntentHandler handler;

    @Before
    public void setup() {
        handler = new AddToListIntentHandler();
    }


    @Test
    public void canHandleIntent() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput("AddToListIntent"
                , null, null, HandlerTestUtil.mockPeristentAttributes(HandlerTestUtil.mockManager()), null);
        assertTrue("Handler should be able to handle intent", handler.canHandle(inputMock));
    }

    @Test
    public void cannotHandleOtherIntents() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(false);
        assertFalse("Handler should not be able to handle any other intent", handler.canHandle(inputMock));
    }

    @Test
    public void addOneToList() {
        TaDoManager tdm = new TaDoManager();
        tdm.getLists().add(new TaDoList());
        AttributesManager attriM = Mockito.mock(AttributesManager.class);
        when(attriM.getPersistentAttributes()).thenReturn(
                MAPPER.convertValue(tdm, new TypeReference<Map<String, Object>>() {
        }));
        Map<String, Slot> slots = new HashMap<>();
        Slot testslot =  Slot.builder().withValue(PhrasesAndConstants.ELEMENT_NAME).withValue("Duschen").build();
        slots.put(PhrasesAndConstants.ELEMENT_NAME, testslot);
        final HandlerInput inputMock = HandlerTestUtil
                .mockHandlerInput("AddToListIntent"
                        , slots
                        , attriM);
        Optional<Response> res = handler.handle(inputMock);


        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, Object>> argCaptor = ArgumentCaptor.forClass(Map.class);

        verify(attriM, Mockito.atLeastOnce()).setPersistentAttributes(argCaptor.capture());
        Map<String, Object> persistedArguments = argCaptor.getValue();
        TaDoList list = new TaDoList();
        list.addElement(new TodoElement("Duschen"));
        TaDoManager expectedMgr = new TaDoManager();
        expectedMgr.getLists().add(list);
        assertEquals(expectedMgr, MAPPER.convertValue(persistedArguments, TaDoManager.class));

        assertTrue(res.isPresent());
        final Response response = res.get();
        assertTrue(response.getOutputSpeech().toString().contains("Die Aufgabe Duschen wurde der Liste hinzugefügt!"));
    }

}
