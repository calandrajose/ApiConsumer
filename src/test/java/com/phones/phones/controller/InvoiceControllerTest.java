package com.phones.phones.controller;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.city.CityDoesNotExistException;
import com.phones.phones.exception.invoice.InvoiceDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.User;
import com.phones.phones.service.InvoiceService;
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

public class InvoiceControllerTest {
    InvoiceController invoiceController;

    @Mock
    InvoiceService invoiceService;
    @Mock
    SessionManager sessionManager;


    @Before
    public void setUp() {
        initMocks(this);
        invoiceController = new InvoiceController(invoiceService, sessionManager);
    }

    @Test
    public void findAllInvoicesOk() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Invoice> listOfInvoices = TestFixture.testListOfInvoices();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findAll()).thenReturn(listOfInvoices);

        ResponseEntity<List<Invoice>> returnedInvoices = invoiceController.findAllInvoices("123");

        assertEquals(listOfInvoices.size(), returnedInvoices.getBody().size());
        assertEquals(listOfInvoices.get(0).getDueDate(), returnedInvoices.getBody().get(0).getDueDate());
        assertEquals(listOfInvoices.get(1).getNumberCalls(), returnedInvoices.getBody().get(1).getNumberCalls());
    }


    @Test
    public void findAllInvoicesUserIsNotEmployee() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<List<Invoice>> returnedInvoices = invoiceController.findAllInvoices("123");
        assertEquals(response.getStatusCode(), returnedInvoices.getStatusCode());
    }

    @Test
    public void findAllInvoicesNoInvoiceFound() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Invoice> emptyList = new ArrayList<>();
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findAll()).thenReturn(emptyList);

        ResponseEntity<List<Invoice>> returnedInvoices = invoiceController.findAllInvoices("123");

        assertEquals(response.getStatusCode(), returnedInvoices.getStatusCode());
    }

    @Test
    public void findInvoiceByIdOk() throws UserSessionDoesNotExistException, InvoiceDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        Invoice invoice = TestFixture.testInvoice();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findById(1L)).thenReturn(invoice);

        ResponseEntity<Invoice> returnedInvoices = invoiceController.findInvoiceById("123", 1L);

        assertEquals(invoice.getId(), returnedInvoices.getBody().getId());
        assertEquals(invoice.getDueDate(), returnedInvoices.getBody().getDueDate());
        assertEquals(invoice.getNumberCalls(), returnedInvoices.getBody().getNumberCalls());
        assertEquals(1L, returnedInvoices.getBody().getId());
    }


    @Test
    public void findInvoiceByIdUserIsNotEmployee() throws UserSessionDoesNotExistException, CityDoesNotExistException, InvoiceDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<Invoice> returnedInvoices = invoiceController.findInvoiceById("123", 1L);
        assertEquals(response.getStatusCode(), returnedInvoices.getStatusCode());
    }

    /**todo getLocation*/
}
