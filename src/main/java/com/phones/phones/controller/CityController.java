package com.phones.phones.controller;

import com.phones.phones.exception.city.CityAlreadyExistException;
import com.phones.phones.exception.city.CityDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.City;
import com.phones.phones.model.User;
import com.phones.phones.service.CityService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
public class CityController {

    private final CityService cityService;
    private final SessionManager sessionManager;

    @Autowired
    public CityController(final CityService cityService,
                          final SessionManager sessionManager) {
        this.cityService = cityService;
        this.sessionManager = sessionManager;
    }


    public ResponseEntity createCity(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid final City city) throws CityAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        City newCity = cityService.create(city);
        return ResponseEntity.created(getLocation(newCity)).build();
    }

    public ResponseEntity<List<City>> findAllCities(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<City> cities = cityService.findAll();
        return (cities.size() > 0) ? ResponseEntity.ok(cities) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<City> findCityById(@RequestHeader("Authorization") String sessionToken,
                                             @PathVariable final Long id) throws CityDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        City city = cityService.findById(id);
        return ResponseEntity.ok(city);
    }

    private URI getLocation(City city) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(city.getId())
                .toUri();
    }

}
