package com.example.demo.service;

import com.example.demo.dto.InvoiceByClient;
import com.example.demo.service.integration.IntegrationComponent;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    private IntegrationComponent integrationComponent;

    @Autowired
    public InvoiceService(IntegrationComponent integrationComponent) {
        this.integrationComponent = integrationComponent;
    }

    public void login() throws JSONException {
        this.integrationComponent.login();
    }

    public InvoiceByClient[] getInvoicesByClient(){
       return this.integrationComponent.getInvoicesByClient();
    }
}
