package edu.hm.cs.tado.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import edu.hm.cs.tado.PhrasesAndConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateListIntentHandlerTest {

    private CreateListIntentHandler handler;

    @Before
    public void setup() {
        handler = new CreateListIntentHandler();
    }


    @Test
    public void canHandleIntent() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput("CreateListIntent"
                , null, null, null, null);
        assertTrue("Handler should be able to handle intent", handler.canHandle(inputMock));
    }

    @Test
    public void cannotHandleOtherIntents() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(false);
        assertFalse("Handler should not be able to handle any other intent", handler.canHandle(inputMock));
    }

    @Test
    public void addNewList() {
        Map<String, Object> persistentAttributes = new HashMap<>();
        persistentAttributes.put("lists", new ArrayList());
        AttributesManager attrM = Mockito.mock(AttributesManager.class);
        when(attrM.getPersistentAttributes()).thenReturn(persistentAttributes);
        final HandlerInput inputMock = HandlerTestUtil
                .mockHandlerInput("CreateListIntent"
                        , null
                        , attrM);
        Optional<Response> res = handler.handle(inputMock);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, Object>> argumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(attrM, Mockito.atLeastOnce()).setPersistentAttributes(argumentCaptor.capture());

        Map<String, Object> persistedArguments = argumentCaptor.getValue();

        assertFalse("There was no new checklist created"
                , ((List) persistedArguments.get("lists")).isEmpty());
        @SuppressWarnings("unchecked")
        Map<String, Object> list = (Map<String, Object>) ((List) (persistedArguments.get("lists"))).get(0);
        assertTrue("checklist has no name", list.containsKey("name"));
        assertTrue("checklist has no list of elements", list.containsKey("elements"));

        assertTrue(res.isPresent());
        final Response response = res.get();
        assertTrue(response.getOutputSpeech().toString().contains(PhrasesAndConstants.LIST_CREATED));
    }

    @Test
    public void addNewListAfterListAlreadyExists() {
        Map<String, Object> persistentAttributes = new HashMap<>();
        List<Object> lists = new ArrayList<>();
        lists.add(new HashMap<String, Object>());
        persistentAttributes.put("lists", lists);
        AttributesManager attrM = Mockito.mock(AttributesManager.class);
        when(attrM.getPersistentAttributes()).thenReturn(persistentAttributes);
        final HandlerInput inputMock = HandlerTestUtil
                .mockHandlerInput("CreateListIntent"
                        , null
                        , attrM);
        Optional<Response> res = handler.handle(inputMock);

        verify(attrM, Mockito.never()).setPersistentAttributes(any());

        assertTrue(res.isPresent());
        final Response response = res.get();
        assertTrue(response.getOutputSpeech().toString().contains(PhrasesAndConstants.LIST_ALREADY_EXISTS));
    }
}
