package edu.hm.cs.tado.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SessionEndedRequestHandlerTest {

    private SessionEndedRequestHandler handler;

    @Before
    public void setup() {
        handler = new SessionEndedRequestHandler();
    }


    @Test
    public void canHandleIntent() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput(SessionEndedRequest.builder().build(), null, null, null);
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
        final Response response = HandlerTestUtil.sessionEndedTestForHandle(handler);
        assertTrue(response.getShouldEndSession());
    }

}
