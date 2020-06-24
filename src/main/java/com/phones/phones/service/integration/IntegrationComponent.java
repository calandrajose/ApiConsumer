package com.phones.phones.service.integration;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class IntegrationComponent {

    private RestTemplate rest;
    private final static String URL = "http://localhost:8080/";
    private final static String URL_BACKOFFICE = URL + "api/backoffice/";
    private final static String URL_BACKOFFICE_INVOICE_BY_CLIENT =URL_BACKOFFICE + "users/7/invoices";
    private final static String URL_BACKOFFICE_USER =URL_BACKOFFICE + "users/1";
    private final static String URL_LOGIN = URL + "login/";
    private HttpHeaders headers = new HttpHeaders();
    private String token;

    @PostConstruct
    private void init(){
        rest = new RestTemplateBuilder()
                .build();
    }

    public void login() throws JSONException {

        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("username", "Rl97");
        personJsonObject.put("password", "1234");

        HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);

        this.token = Objects.requireNonNull(rest.postForEntity(URL_LOGIN, request, String.class).getHeaders().get("Authorization")).get(0);
    }

    public InvoiceByClient[] getInvoicesByClient(){
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, this.token);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        return this.rest.getForObject(URL_BACKOFFICE_INVOICE_BY_CLIENT, InvoiceByClient[].class);
    }

    public org.apache.catalina.User getUserById(Integer id){
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, this.token);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        return this.rest.getForObject(URL_BACKOFFICE_USER, User.class);
    }
}
