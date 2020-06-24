package com.phones.phones.controller;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.province.ProvinceDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Province;
import com.phones.phones.model.User;
import com.phones.phones.service.ProvinceService;
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

public class ProvinceControllerTest {
    ProvinceController provinceController;

    @Mock
    ProvinceService provinceService;
    @Mock
    SessionManager sessionManager;


    @Before
    public void setUp() {
        initMocks(this);
        provinceController = new ProvinceController(provinceService, sessionManager);
    }

    @Test
    public void findAllProvincesOk() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Province> listOfProvinces = TestFixture.testListOfProvinces();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(provinceService.findAll()).thenReturn(listOfProvinces);

        ResponseEntity<List<Province>> returnedProvinces = provinceController.findAllProvinces("123");

        assertEquals(listOfProvinces.size(), returnedProvinces.getBody().size());
        assertEquals(listOfProvinces.get(0).getName(), returnedProvinces.getBody().get(0).getName());
        assertEquals(listOfProvinces.get(0).getId(), returnedProvinces.getBody().get(0).getId());
        assertEquals(listOfProvinces.get(0).getCities().size(), returnedProvinces.getBody().get(0).getCities().size());
    }


    @Test
    public void findAllProvincesUserIsNotEmployee() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<List<Province>> returnedProvinces = provinceController.findAllProvinces("123");
        assertEquals(response.getStatusCode(), returnedProvinces.getStatusCode());
    }

    @Test
    public void findAllProvincesNoProvincesFound() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Province> emptyList = new ArrayList<>();
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(provinceService.findAll()).thenReturn(emptyList);

        ResponseEntity<List<Province>> returnedProvinces = provinceController.findAllProvinces("123");

        assertEquals(response.getStatusCode(), returnedProvinces.getStatusCode());
    }

    @Test
    public void findProvinceByIdOk() throws UserSessionDoesNotExistException, ProvinceDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        Province province = TestFixture.testProvince();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(provinceService.findById(1L)).thenReturn(province);

        ResponseEntity<Province> returnedProvinces = provinceController.findProvinceById("123", 1L);

        assertEquals(province.getId(), returnedProvinces.getBody().getId());
        assertEquals(province.getName(), returnedProvinces.getBody().getName());
        assertEquals(province.getCities().size(), returnedProvinces.getBody().getCities().size());
        assertEquals(1L, returnedProvinces.getBody().getId());
    }

    @Test
    public void findProvinceByIdUserIsNotEmployee() throws UserSessionDoesNotExistException, ProvinceDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<Province> returnedProvinces = provinceController.findProvinceById("123", 1L);
        assertEquals(response.getStatusCode(), returnedProvinces.getStatusCode());
    }

    /**todo getLocation
            create
     **/

}
