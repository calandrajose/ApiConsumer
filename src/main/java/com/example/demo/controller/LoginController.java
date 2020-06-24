package com.example.demo.controller;


import com.example.demo.service.InvoiceService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private InvoiceService invoiceService;

    @Autowired
    public LoginController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("")
    public void login(){
        try {
            this.invoiceService.login();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
