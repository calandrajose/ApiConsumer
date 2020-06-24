package com.example.demo.service;


import com.example.demo.service.integration.IntegrationComponent;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegrationService {

    private IntegrationComponent integrationComponent;

    @Autowired
    public IntegrationService(IntegrationComponent integrationComponent){
        this.integrationComponent = integrationComponent;
    }

    public void login() throws JSONException {
        this.integrationComponent.login();
    }
}
