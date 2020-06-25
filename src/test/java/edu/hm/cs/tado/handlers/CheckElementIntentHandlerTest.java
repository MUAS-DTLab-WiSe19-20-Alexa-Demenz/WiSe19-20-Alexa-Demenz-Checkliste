package edu.hm.cs.tado.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import edu.hm.cs.tado.PhrasesAndConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CheckElementIntentHandlerTest {
    private CheckElementIntentHandler handler;

    @Before
    public void setup() {
        handler = new CheckElementIntentHandler();
    }


    @Test
    public void canHandleIntent() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput("CheckElementIntent"
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
    public void checkUncheckedItem() {

        Map<String, Slot> slots = new HashMap<>();
        slots.put(PhrasesAndConstants.ELEMENT_NAME, Slot.builder()
                .withName(PhrasesAndConstants.ELEMENT_NAME)
                .withValue("test")
                .build()
        );

        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput("CheckElementIntent"
                , slots, null, HandlerTestUtil.mockPeristentAttributes(HandlerTestUtil.mockManager()), null);
        Response response = handler.handle(inputMock).orElseThrow(AssertionError::new);
        Assert.assertFalse(response.getCard().toString().contains(PhrasesAndConstants.NO_SUCH_ELEMENT));
    }

}
