package edu.hm.cs.tado.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import edu.hm.cs.tado.PhrasesAndConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CancelandStopIntentHandlerTest {

    private CancelandStopIntentHandler handler;

    @Before
    public void setup() {
        handler = new CancelandStopIntentHandler();
    }


    @Test
    public void canHandleCancleIntent() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput("AMAZON.CancelIntent"
                , null, null, null, null);
        assertTrue("Handler should be able to handle intent", handler.canHandle(inputMock));
    }

    @Test
    public void canHandleStopIntent() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput("AMAZON.StopIntent"
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
    public void testHandle() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput(null, null, null, null);
        final Optional<Response> res = handler.handle(inputMock);

        assertTrue(res.isPresent());
        final Response response = res.get();

        assertTrue(response.getShouldEndSession());
        assertNotNull(response.getOutputSpeech());
        assertTrue(response.getOutputSpeech().toString().contains(PhrasesAndConstants.CANCEL_AND_STOP));
    }

}
