package edu.hm.cs.tado.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ServiceClientFactory;
import com.amazon.ask.model.services.ServiceException;
import com.amazon.ask.model.services.ups.UpsServiceClient;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.hm.cs.tado.PhrasesAndConstants;
import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TaDoManager;
import edu.hm.cs.tado.model.TodoElement;
import edu.hm.cs.tado.service.MailSenderService;
import edu.hm.cs.tado.service.TaDoListRenderer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.mail.internet.InternetAddress;
import java.util.*;

import static edu.hm.cs.tado.PhrasesAndConstants.MAPPER;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SendListMailIntentHandlerTest {

    private SendListMailIntentHandler handler;
    private MailSenderService mailSender;

    @Before
    public void setup() {
        mailSender = mock(MailSenderService.class);
        handler = new SendListMailIntentHandler(mailSender);
    }


    @Test
    public void canHandleIntent() {
        final HandlerInput inputMock = HandlerTestUtil.mockHandlerInput("SendListMailIntent"
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
    public void sendDayMail() {
        String message = new TaDoListRenderer(getTaDoManager().getLists().get(0)).render();
        sendMail(message, "SendListMailIntent");
    }

    @Test
    public void sendWeekMail() {
        String message = new TaDoListRenderer(getTaDoManager().getLists().get(0)).renderWeek();
        sendMail(message, "SendWeekListMailIntent");
    }

    private void sendMail(String renderedMessage, String intentName) {
        TaDoManager manager = getTaDoManager();
        Map<String, Object> persistentAttributes =
                MAPPER.convertValue(manager, new TypeReference<Map<String, Object>>() {
                });
        HandlerInput input = HandlerTestUtil.mockHandlerInput(intentName,
                null, null,
                persistentAttributes, null);

        mockClientFactory(input);

        handler.handle(input);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<InternetAddress[]> addressCaptor = ArgumentCaptor.forClass(InternetAddress[].class);
        verify(mailSender, times(1))
                .sendMail(subjectCaptor.capture(), messageCaptor.capture(), addressCaptor.capture());
        assertEquals("Ihre TaDo Liste", subjectCaptor.getValue());
        assertEquals(renderedMessage, messageCaptor.getValue());
        assertEquals("test@example.com", addressCaptor.getValue()[0].getAddress());
    }

    @Test
    public void noLists() {
        TaDoManager manager = new TaDoManager();
        manager.setLists(new ArrayList<>());

        Map<String, Object> persistentAttributes =
                MAPPER.convertValue(manager, new TypeReference<Map<String, Object>>() {
                });
        HandlerInput input = HandlerTestUtil.mockHandlerInput("", null, null,
                persistentAttributes, null);

        mockClientFactory(input);

        assertFalse(handler.canHandle(input));
    }

    @Test
    public void missingPermission() {
        TaDoManager manager = getTaDoManager();
        Map<String, Object> persistentAttributes =
                MAPPER.convertValue(manager, new TypeReference<Map<String, Object>>() {
                });
        HandlerInput input = HandlerTestUtil.mockHandlerInput("", null, null,
                persistentAttributes, null);

        mockClientFactoryWithException(input);

        handler.handle(input);
        Optional<Response> res = handler.handle(input);
        final Response response = res.orElseThrow(NoSuchElementException::new);
        assertTrue(response.getOutputSpeech().toString().contains(PhrasesAndConstants.GIVE_MAIL_PERMISSION));
    }


    private void mockClientFactory(HandlerInput input) {
        UpsServiceClient mockClient = mock(UpsServiceClient.class);
        when(mockClient.getProfileEmail()).thenReturn("test@example.com");
        ServiceClientFactory mockClientFactory = mock(ServiceClientFactory.class);
        when(mockClientFactory.getUpsService()).thenReturn(mockClient);
        when(input.getServiceClientFactory()).thenReturn(mockClientFactory);
    }

    private void mockClientFactoryWithException(HandlerInput input) {
        UpsServiceClient mockClient = mock(UpsServiceClient.class);
        when(mockClient.getProfileEmail()).thenThrow(ServiceException.class);
        ServiceClientFactory mockClientFactory = mock(ServiceClientFactory.class);
        when(mockClientFactory.getUpsService()).thenReturn(mockClient);
        when(input.getServiceClientFactory()).thenReturn(mockClientFactory);
    }

    private TaDoManager getTaDoManager() {
        TaDoManager manager = new TaDoManager();
        TaDoList list = new TaDoList();
        List<TodoElement> elements = new ArrayList<>();
        elements.add(new TodoElement("test"));
        list.setElements(elements);
        List<TaDoList> lists = new ArrayList<>();
        lists.add(list);
        manager.setLists(lists);
        return manager;
    }
}
