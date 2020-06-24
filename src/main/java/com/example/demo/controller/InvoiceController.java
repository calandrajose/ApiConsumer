package com.example.demo.controller;

import com.example.demo.dto.InvoiceByClient;
import com.example.demo.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @GetMapping("/{idClient}")
    public InvoiceByClient[] getInvoicesByClient(@PathVariable Integer idClient){
       return this.invoiceService.getInvoicesByClient();
    }

}
