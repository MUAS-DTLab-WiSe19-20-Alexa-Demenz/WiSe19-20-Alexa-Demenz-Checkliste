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

public class DeleteListIntentHandlerTest {

    private DeleteListIntentHandler handler;

    @Before
    public void setup() {
        handler = new DeleteListIntentHandler();
    }


    @Test
    public void cannotHandleOtherIntents() {
        final HandlerInput inputMock = Mockito.mock(HandlerInput.class);
        when(inputMock.matches(any())).thenReturn(false);
        assertFalse("Handler should not be able to handle any other intent", handler.canHandle(inputMock));
    }

    @Test
    public void deleteListAfterListAlreadyExists() {
        Map<String, Object> persistentAttributes = new HashMap<>();
        List<Object> lists = new ArrayList<>();
        lists.add(new HashMap<String, Object>());
        persistentAttributes.put("lists", lists);
        AttributesManager attriM = Mockito.mock(AttributesManager.class);
        when(attriM.getPersistentAttributes()).thenReturn(persistentAttributes);
        final HandlerInput inputMock = HandlerTestUtil
                .mockHandlerInput("DeleteListIntent"
                        , null
                        , attriM);
        Optional<Response> res = handler.handle(inputMock);


        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, Object>> argumentCaptor = ArgumentCaptor.forClass(Map.class);

        verify(attriM, Mockito.atLeastOnce()).setPersistentAttributes(argumentCaptor.capture());
        Map<String, Object> persistedArguments = argumentCaptor.getValue();
        assertTrue("The checklist was not deleted"
                , ((List) persistedArguments.get("lists")).isEmpty());

        assertTrue(res.isPresent());
        final Response response = res.get();
        assertTrue(response.getOutputSpeech().toString().contains(PhrasesAndConstants.LIST_DELETED));
    }
}
