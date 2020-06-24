package com.phones.phones.controller;

import com.phones.phones.TestFixture;
import com.phones.phones.dto.RateDto;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.User;
import com.phones.phones.service.RateService;
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

public class RateControllerTest {

    RateController rateController;

    @Mock
    RateService rateService;
    @Mock
    SessionManager sessionManager;


    @Before
    public void setUp() {
        initMocks(this);
        rateController = new RateController(rateService, sessionManager);
    }

    @Test
    public void findAllRatesOk() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<RateDto> listOfRates = TestFixture.testListOfRatesDto();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(rateService.findAll()).thenReturn(listOfRates);

        ResponseEntity<List<RateDto>> returnedRates = rateController.findAllRates("123");

        assertEquals(listOfRates.size(), returnedRates.getBody().size());
        assertEquals(listOfRates.get(0).getDestinationCity(), returnedRates.getBody().get(0).getDestinationCity());
        assertEquals(listOfRates.get(1).getDestinationCity(), returnedRates.getBody().get(1).getDestinationCity());
        assertEquals(listOfRates.get(1).getPriceMinute(), returnedRates.getBody().get(1).getPriceMinute());
    }


    @Test
    public void findAllRatesUserIsNotEmployee() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<List<RateDto>> returnedRates = rateController.findAllRates("123");
        assertEquals(response.getStatusCode(), returnedRates.getStatusCode());
    }

    @Test
    public void findAllRatesNoRatesFound() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<RateDto> emptyList = new ArrayList<>();
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(rateService.findAll()).thenReturn(emptyList);

        ResponseEntity<List<RateDto>> returnedRates = rateController.findAllRates("123");

        assertEquals(response.getStatusCode(), returnedRates.getStatusCode());
    }
}
