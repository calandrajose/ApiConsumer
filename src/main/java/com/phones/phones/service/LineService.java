package com.phones.phones.service;

import com.phones.phones.dto.LineDto;
import com.phones.phones.exception.line.LineAlreadyDisabledException;
import com.phones.phones.exception.line.LineDoesNotExistException;
import com.phones.phones.exception.line.LineNumberAlreadyExistException;
import com.phones.phones.exception.user.UserDoesNotExistException;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.repository.LineRepository;
import com.phones.phones.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final UserRepository userRepository;

    @Autowired
    public LineService(final LineRepository lineRepository,
                       final UserRepository userRepository) {
        this.lineRepository = lineRepository;
        this.userRepository = userRepository;
    }


    public Line create(Line newLine) throws LineNumberAlreadyExistException {
        Optional<Line> line = lineRepository.findByNumber(newLine.getNumber());
        if (line.isPresent()) {
            throw new LineNumberAlreadyExistException();
        }
        return lineRepository.save(newLine);
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public Line findById(Long id) throws LineDoesNotExistException {
        Optional<Line> line = lineRepository.findById(id);
        if (line.isEmpty()) {
            throw new LineDoesNotExistException();
        }
        return line.get();
    }

    public List<Line> findByUserId(Long id) throws UserDoesNotExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        return lineRepository.findAllByUserId(id);
    }

    public int disableById(Long id) throws LineDoesNotExistException, LineAlreadyDisabledException {
        Optional<Line> line = lineRepository.findById(id);
        if (line.isEmpty()) {
            throw new LineDoesNotExistException();
        }
        if (line.get().isDisabled()) {
            throw new LineAlreadyDisabledException();
        }
        return lineRepository.disableById(id);
    }

    public boolean updateLineByIdLine(Long id,
                                      LineDto updatedLine) throws LineDoesNotExistException {
        Optional<Line> line = lineRepository.findById(id);
        if (line.isEmpty()) {
            throw new LineDoesNotExistException();
        }
        line.get().setNumber(updatedLine.getNumber());
        line.get().setStatus(updatedLine.getStatus());
        line.get().setLineType(updatedLine.getLineType());

        int updated = lineRepository.updateById(id,
                line.get().getNumber(),
                String.valueOf(line.get().getStatus()),
                line.get().getLineType().getId());
        return (updated > 0);
    }

}
