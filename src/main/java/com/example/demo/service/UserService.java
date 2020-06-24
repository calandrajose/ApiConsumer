package com.example.demo.service;

import com.example.demo.service.integration.IntegrationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private IntegrationComponent integrationComponent;

    @Autowired
    public UserService(IntegrationComponent integrationComponent) {
        this.integrationComponent = integrationComponent;
    }


    public User getUserById(Integer id){
        return (User) this.integrationComponent.getUserById(id);
    }
}
