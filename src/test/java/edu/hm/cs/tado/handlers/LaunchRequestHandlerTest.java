package edu.hm.cs.tado.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import edu.hm.cs.tado.PhrasesAndConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LaunchRequestHandlerTest {
    private LaunchRequestHandler handler;

    @Before
    public void setup() {
        handler = new LaunchRequestHandler();
    }


    @Test
    public void canHandleIntent() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput(LaunchRequest.builder().build()
                , null, null, null);
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
        final Response response = HandlerTestUtil.standardTestForHandle(handler);
        assertTrue(response.getOutputSpeech().toString().contains(PhrasesAndConstants.CARD_TITLE));
    }

    @Test
    public void testCreatesListsAttribute() {

    }
}
