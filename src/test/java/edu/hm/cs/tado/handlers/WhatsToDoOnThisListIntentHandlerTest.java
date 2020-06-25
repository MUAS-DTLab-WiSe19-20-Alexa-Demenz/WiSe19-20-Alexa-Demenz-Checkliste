package edu.hm.cs.tado.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import edu.hm.cs.tado.PhrasesAndConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class WhatsToDoOnThisListIntentHandlerTest {

    private WhatsToDoOnThisListIntentHandler handler;

    @Before
    public void setup() {
        handler = new WhatsToDoOnThisListIntentHandler();
    }


    @Test
    public void canHandleIntent() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput("WhatsToDoOnThisListIntent"
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
    public void noListsCreated() {
        Map<String, Object> persistentAttributes = new HashMap<>();
        persistentAttributes.put("lists", new ArrayList());
        AttributesManager attrM = Mockito.mock(AttributesManager.class);
        when(attrM.getPersistentAttributes()).thenReturn(persistentAttributes);

        final HandlerInput inputMock = HandlerTestUtil
                .mockHandlerInput("WhatsToDoOnThisListIntent"
                        , null
                        , attrM);

        assertFalse(handler.canHandle(inputMock));
    }

}
