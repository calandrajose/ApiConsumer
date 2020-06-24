package com.phones.phones.controller;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.call.CallDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Call;
import com.phones.phones.model.User;
import com.phones.phones.service.CallService;
import com.phones.phones.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class CallControllerTest {

    CallController callController;

    @Mock
    CallService callService;
    @Mock
    SessionManager sessionManager;


    @Before
    public void setUp() {
        initMocks(this);
        callController = new CallController(callService, sessionManager);
    }

    @Test
    public void findAllCallsOk() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Call> listOfCalls = TestFixture.testListOfCalls();
        List<Call> testCalls = TestFixture.testListOfCalls();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findAll()).thenReturn(listOfCalls);

        ResponseEntity<List<Call>> returnedCalls = callController.findAllCalls("123");

        assertEquals(testCalls.size(), returnedCalls.getBody().size());
        assertEquals(testCalls.get(0).getOriginNumber(), returnedCalls.getBody().get(0).getOriginNumber());
    }


    @Test
    public void findAllCallsUserIsNotEmployee() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<List<Call>> returnedCalls = callController.findAllCalls("123");
        assertEquals(response.getStatusCode(), returnedCalls.getStatusCode());
    }

    @Test
    public void findAllCallsNoCallsDone() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Call> emptyCalls = new ArrayList<>();
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findAll()).thenReturn(emptyCalls);

        ResponseEntity<List<Call>> returnedCalls = callController.findAllCalls("123");

        assertEquals(response.getStatusCode(), returnedCalls.getStatusCode());
    }

    @Test
    public void findAllCallByIdOk() throws UserSessionDoesNotExistException, CallDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        Call call = TestFixture.testCall();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findById(1L)).thenReturn(call);

        ResponseEntity<Call> returnedCall = callController.findCallById("123", 1L);

        assertEquals(call.getId(), returnedCall.getBody().getId());
        assertEquals(call.getDuration(), returnedCall.getBody().getDuration());
        assertEquals(call.getOriginLine(), returnedCall.getBody().getOriginLine());
        assertEquals(1L, returnedCall.getBody().getId());
    }

    @Test
    public void findCallByIdUserIsNotEmployee() throws UserSessionDoesNotExistException, CallDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<Call> returnedCall = callController.findCallById("123", 1L);
        assertEquals(response.getStatusCode(), returnedCall.getStatusCode());
    }

}
