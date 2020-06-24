package com.phones.phones.controller;

import com.phones.phones.dto.LineDto;
import com.phones.phones.exception.line.LineAlreadyDisabledException;
import com.phones.phones.exception.line.LineDoesNotExistException;
import com.phones.phones.exception.line.LineNumberAlreadyExistException;
import com.phones.phones.exception.user.UserSessionDoesNotExistException;
import com.phones.phones.model.Call;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.service.CallService;
import com.phones.phones.service.LineService;
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
public class LineController {

    private final LineService lineService;
    private final CallService callService;
    private final SessionManager sessionManager;

    @Autowired
    public LineController(final LineService lineService,
                          final CallService callService,
                          final SessionManager sessionManager) {
        this.lineService = lineService;
        this.callService = callService;
        this.sessionManager = sessionManager;
    }


    public ResponseEntity createLine(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid final Line line) throws LineNumberAlreadyExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        Line newLine = lineService.create(line);
        return ResponseEntity.created(getLocation(newLine)).build();
    }

    public ResponseEntity<List<Line>> findAllLines(@RequestHeader("Authorization") String sessionToken) throws UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Line> lines = lineService.findAll();
        return (lines.size() > 0) ? ResponseEntity.ok(lines) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<Line> findLineById(@RequestHeader("Authorization") String sessionToken,
                                             @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        Line line = lineService.findById(id);
        return ResponseEntity.ok(line);
    }

    public ResponseEntity<List<Call>> findCallsByLineId(@RequestHeader("Authorization") String sessionToken,
                                                        @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        List<Call> calls = callService.findByLineId(id);
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity deleteLineById(@RequestHeader("Authorization") final String sessionToken,
                                         @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException, LineAlreadyDisabledException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        int deleted = lineService.disableById(id);
        return (deleted > 0) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public ResponseEntity updateLineByIdLine(@RequestHeader("Authorization") final String sessionToken,
                                             @RequestBody @Valid final LineDto updatedLine,
                                             @PathVariable final Long id) throws LineDoesNotExistException, UserSessionDoesNotExistException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        boolean line = lineService.updateLineByIdLine(id, updatedLine);
        return ResponseEntity.ok().build();
    }

    private URI getLocation(Line line) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(line.getId())
                .toUri();
    }

}
