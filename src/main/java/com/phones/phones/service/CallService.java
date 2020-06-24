package com.phones.phones.service;

import com.phones.phones.dto.InfrastructureCallDto;
import com.phones.phones.exception.call.CallDoesNotExistException;
import com.phones.phones.exception.line.LineCannotMakeCallsException;
import com.phones.phones.exception.line.LineDoesNotExistException;
import com.phones.phones.exception.line.LineNumberDoesNotExistException;
import com.phones.phones.exception.user.UserDoesNotExistException;
import com.phones.phones.model.Call;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.repository.CallRepository;
import com.phones.phones.repository.LineRepository;
import com.phones.phones.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CallService {

    private final CallRepository callRepository;
    private final LineRepository lineRepository;
    private final UserRepository userRepository;

    @Autowired
    public CallService(final CallRepository callRepository,
                       final LineRepository lineRepository,
                       final UserRepository userRepository) {
        this.callRepository = callRepository;
        this.lineRepository = lineRepository;
        this.userRepository = userRepository;
    }


    public void create(Call newCall) {
        callRepository.save(newCall);
    }

    public Call create(InfrastructureCallDto infrastructureCallDto) throws LineNumberDoesNotExistException, LineCannotMakeCallsException {
        Optional<Line> originLine = lineRepository.findByNumber(infrastructureCallDto.getOriginNumber());
        Optional<Line> destinationLine = lineRepository.findByNumber(infrastructureCallDto.getDestinationNumber());

        if (originLine.isEmpty() || destinationLine.isEmpty()) {
            throw new LineNumberDoesNotExistException();
        }

        if (originLine.get().isDisabled() ||
                originLine.get().isSuspended() ||
                destinationLine.get().isDisabled() ||
                destinationLine.get().isSuspended()) {
            throw new LineCannotMakeCallsException();
        }

        Call newCall = Call
                        .builder()
                        .duration(infrastructureCallDto.getDuration())
                        .creationDate(infrastructureCallDto.getCreationDate())
                        .originNumber(infrastructureCallDto.getOriginNumber())
                        .destinationNumber(infrastructureCallDto.getDestinationNumber())
                        .build();

        return callRepository.save(newCall);
    }

    public List<Call> findAll() {
        return callRepository.findAll();
    }

    public Call findById(Long id) throws CallDoesNotExistException {
        Optional<Call> call = callRepository.findById(id);
        if (call.isEmpty()) {
            throw new CallDoesNotExistException();
        }
        return call.get();
    }

    public List<Call> findByUserId(Long id) throws UserDoesNotExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        return callRepository.findAllByUserId(id);
    }

    public List<Call> findByLineId(Long id) throws LineDoesNotExistException {
        Optional<Line> line = lineRepository.findById(id);
        if(line.isEmpty()) {
            throw new LineDoesNotExistException();
        }
        return callRepository.findAllByLineId(id);
    }

    public List<Call> findByUserIdBetweenDates(Long id,
                                               Date from,
                                               Date to) throws UserDoesNotExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException();
        }
        return callRepository.findAllByUserIdBetweenDates(id, from, to);
    }

    /*
        public String findMostCalledByOriginId(Long id){
            return callRepository.findMostCalledByOriginId(id);
        }
     */

}
