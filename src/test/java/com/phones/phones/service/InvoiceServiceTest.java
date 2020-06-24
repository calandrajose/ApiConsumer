package com.phones.phones.service;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.invoice.InvoiceDoesNotExistException;
import com.phones.phones.exception.user.UserDoesNotExistException;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.User;
import com.phones.phones.repository.InvoiceRepository;
import com.phones.phones.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvoiceServiceTest {


    InvoiceService invoiceService;
    @Mock
    UserRepository userRepository;

    @Mock
    InvoiceRepository invoiceRepository;


    @Before
    public void setUp() {
        initMocks(this);
        this.invoiceService = new InvoiceService(invoiceRepository, userRepository);
    }


    @Test
    public void testFindAllOk() {
        List<Invoice> allInvoices = TestFixture.testListOfInvoices();
        when(invoiceRepository.findAll()).thenReturn(allInvoices);

        List<Invoice> invoicesReturned = invoiceService.findAll();

        assertEquals(invoicesReturned.size(), allInvoices.size());
        assertEquals(invoicesReturned.get(0).getId(), allInvoices.get(0).getId());
    }

    @Test
    public void testFindAllEmpty() {
        List<Invoice> emptyList = new ArrayList<>();
        when(invoiceRepository.findAll()).thenReturn(emptyList);

        List<Invoice> returnedCalls = invoiceService.findAll();
        assertEquals(returnedCalls.size(), 0);
    }

    @Test
    public void testFindByIdOk() throws InvoiceDoesNotExistException {
        Invoice invoiceGetById = TestFixture.testInvoice();

        when(invoiceRepository.findById(1L)).thenReturn(Optional.ofNullable(invoiceGetById));

        Invoice returnedInvoice = this.invoiceService.findById(1L);

        assertEquals(invoiceGetById.getId(), returnedInvoice.getId());
    }


    @Test(expected = InvoiceDoesNotExistException.class)
    public void testFindByIdInvoiceDoesNotExist() throws InvoiceDoesNotExistException {
        when(invoiceRepository.findById(2L)).thenReturn(Optional.empty());
        this.invoiceService.findById(2L);
    }


    @Test
    public void testFindByUserIdOk() throws UserDoesNotExistException {
        User userGetById = TestFixture.testUser();
        List<Invoice> invoices = TestFixture.testListOfInvoices();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userGetById));
        when(invoiceRepository.findByUserId(1L)).thenReturn(invoices);

        List<Invoice> returnedCalls = this.invoiceService.findByUserId(1L);
        assertEquals(invoices.size(), returnedCalls.size());
        assertEquals(invoices.get(0).getId(), returnedCalls.get(0).getId());
    }


    @Test(expected = UserDoesNotExistException.class)
    public void testFindByUserIdUserNotExist() throws UserDoesNotExistException {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        List<Invoice> returnedInvoice = this.invoiceService.findByUserId(2L);
    }
}
