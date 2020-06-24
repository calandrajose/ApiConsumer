package com.phones.phones.controller;

import com.phones.phones.exception.invoice.InvoiceDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.User;
import com.phones.phones.service.InvoiceService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final SessionManager sessionManager;

    @Autowired
    public InvoiceController(final InvoiceService invoiceService,
                             final SessionManager sessionManager) {
        this.invoiceService = invoiceService;
        this.sessionManager = sessionManager;
    }


    public ResponseEntity<List<Invoice>> findAllInvoices(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Invoice> invoices = invoiceService.findAll();;
        return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<Invoice> findInvoiceById(@RequestHeader("Authorization") final String sessionToken,
                                                   @PathVariable final Long id) throws InvoiceDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        Invoice invoice = invoiceService.findById(id);
        return ResponseEntity.ok(invoice);
    }

}
