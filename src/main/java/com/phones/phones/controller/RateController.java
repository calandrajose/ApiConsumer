package com.phones.phones.controller;

import com.phones.phones.dto.RateDto;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.User;
import com.phones.phones.service.RateService;
import com.phones.phones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RateController {

    private final RateService rateService;
    private final SessionManager sessionManager;

    @Autowired
    public RateController(final RateService rateService,
                          final SessionManager sessionManager) {
        this.rateService = rateService;
        this.sessionManager = sessionManager;
    }


    public ResponseEntity<List<RateDto>> findAllRates(@RequestHeader("Authorization") final String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<RateDto> rates = rateService.findAll();
        return (rates.size() > 0) ? ResponseEntity.ok(rates) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*
        @GetMapping("/test")
        public RateByCity getRateByCities(@RequestParam(name = "cityFrom") Integer idCityFrom,
                                          @RequestParam(name = "cityTo") Integer idCityTo){
            return this.rateService.getRateByCities(idCityFrom, idCityTo);
        }
     */

}
