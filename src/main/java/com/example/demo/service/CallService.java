package com.example.demo.service;

import com.example.demo.service.integration.IntegrationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallService {
    private IntegrationComponent integrationComponent;

    @Autowired
    public CallService(IntegrationComponent integrationComponent) {
        this.integrationComponent = integrationComponent;
    }
}
