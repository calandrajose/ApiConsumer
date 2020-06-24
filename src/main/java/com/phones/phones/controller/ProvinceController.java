package com.phones.phones.controller;

import com.phones.phones.exception.province.ProviceAlreadyExistException;
import com.phones.phones.exception.province.ProvinceDoesNotExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Province;
import com.phones.phones.model.User;
import com.phones.phones.service.ProvinceService;
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
public class ProvinceController {

    private final ProvinceService provinceService;
    private final SessionManager sessionManager;

    @Autowired
    public ProvinceController(final ProvinceService provinceService,
                              final SessionManager sessionManager) {
        this.provinceService = provinceService;
        this.sessionManager = sessionManager;
    }


    public ResponseEntity createProvince(@RequestHeader("Authorization") final String sessionToken,
                                         @RequestBody @Valid final Province province) throws ProviceAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        Province newProvince = provinceService.create(province);
        return ResponseEntity.created(getLocation(newProvince)).build();
    }

    public ResponseEntity<List<Province>> findAllProvinces(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Province> provinces = provinceService.findAll();
        return (provinces.size() > 0) ? ResponseEntity.ok(provinces) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<Province> findProvinceById(@RequestHeader("Authorization") final String sessionToken,
                                                     @PathVariable final Long id) throws ProvinceDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        Province province = provinceService.findById(id);
        return ResponseEntity.ok(province);
    }

    private URI getLocation(Province province) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(province.getId())
                .toUri();
    }

}
