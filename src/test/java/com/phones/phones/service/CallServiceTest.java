package com.phones.phones.service;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.call.CallDoesNotExistException;
import com.phones.phones.exception.line.LineDoesNotExistException;
import com.phones.phones.exception.user.UserDoesNotExistException;
import com.phones.phones.model.Call;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.repository.CallRepository;
import com.phones.phones.repository.LineRepository;
import com.phones.phones.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CallServiceTest {
    CallService callService;

    @Mock
    UserRepository userRepository;

    @Mock
    CallRepository callRepository;

    @Mock
    LineRepository lineRepository;

    @Mock
    Call.CallBuilder callBuilder;


    @Before
    public void setUp() {
        initMocks(this);
        this.callService = new CallService(callRepository, lineRepository, userRepository);
    }

    /*todo Test Create method, need to mock builder
/*    @Test
    public void testCreateOk() throws LineNumberNotExistException, LineCannotMakeCallsException {
        InfrastructureCallDto infrastructureCallDto= TestFixture.testInfrastructureCallDto();
        Line testOriginLine = TestFixture.testLine(infrastructureCallDto.getOriginNumber());
        Line testDestinationLine = TestFixture.testLine(infrastructureCallDto.getDestinationNumber());

       Call testCall = Call//Mockito.mock(Call.class, RETURNS_DEEP_STUBS);
                        .builder()
                        .id(1L)
                        .duration(infrastructureCallDto.getDuration())
                        .creationDate(infrastructureCallDto.getCreationDate())
                        .originNumber(infrastructureCallDto.getOriginNumber())
                        .destinationNumber(infrastructureCallDto.getDestinationNumber())
                        .build();
        //Call tesCall = TestFixture.testCalls();

        when(lineRepository.findByNumber(infrastructureCallDto.getDestinationNumber())).thenReturn(Optional.ofNullable(testDestinationLine));
        when(lineRepository.findByNumber(infrastructureCallDto.getOriginNumber())).thenReturn(Optional.ofNullable(testOriginLine));
        when(callBuilder).thenReturn(testCall);
        when(callRepository.save(testCall)).thenReturn(testCall);
        Call returnedCall = this.callService.create(infrastructureCallDto);

        assertEquals(testCall.getId(), returnedCall.getId());
        assertEquals(testCall.getOriginNumber(), returnedCall.getOriginNumber());
    }*/

    @Test
    public void testFindAllOk() {
        List<Call> allCalls = TestFixture.testListOfCalls();
        when(callRepository.findAll()).thenReturn(allCalls);

        List<Call> returnedCalls = callService.findAll();

        assertEquals(returnedCalls.size(), allCalls.size());
        assertEquals(returnedCalls.get(0).getId(), allCalls.get(0).getId());
    }

    @Test
    public void testFindAllEmpty() {
        List<Call> emptyList = new ArrayList<>();
        when(callRepository.findAll()).thenReturn(emptyList);

        List<Call> returnedCalls = callService.findAll();
        assertEquals(returnedCalls.size(), 0);
    }

    @Test
    public void testFindByIdOk() throws CallDoesNotExistException {
        Call callGetById = TestFixture.testCall();

        when(callRepository.findById(1L)).thenReturn(Optional.ofNullable(callGetById));

        Call returnedCall = this.callService.findById(1L);

        assertEquals(callGetById.getId(), returnedCall.getId());
    }

    @Test(expected = CallDoesNotExistException.class)
    public void testFindByUserIdCallNotExist() throws CallDoesNotExistException {
        when(callRepository.findById(2L)).thenReturn(Optional.empty());
        this.callService.findById(2L);
    }


    @Test
    public void testFindByUserIdOk() throws UserDoesNotExistException {
        User userGetById = TestFixture.testUser();
        List<Call> calls = TestFixture.testListOfCalls();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userGetById));
        when(callRepository.findAllByUserId(1L)).thenReturn(calls);

        List<Call> returnedCalls = this.callService.findByUserId(1L);
        assertEquals(calls.size(), returnedCalls.size());
    }


    @Test(expected = UserDoesNotExistException.class)
    public void testFindByUserIdUserNotExist() throws UserDoesNotExistException {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        List<Call> returnedCalls = this.callService.findByUserId(2L);
    }

    @Test
    public void testFindByLineIdOk() throws LineDoesNotExistException {
        Line lineGetById = TestFixture.testLine("2235472861");
        List<Call> calls = TestFixture.testListOfCalls();

        when(lineRepository.findById(1L)).thenReturn(Optional.ofNullable(lineGetById));
        when(callRepository.findAllByLineId(1L)).thenReturn(calls);

        List<Call> returnedCalls = this.callService.findByLineId(1L);
        assertEquals(calls.size(), returnedCalls.size());
    }


    @Test(expected = UserDoesNotExistException.class)
    public void testFindByLineIdLineNotExist() throws UserDoesNotExistException {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        List<Call> returnedCalls = this.callService.findByUserId(2L);
    }

    @Test
    public void testFindByUserIdBetweenDatesOk() throws UserDoesNotExistException {
        User userGetById = TestFixture.testUser();
        List<Call> calls = TestFixture.testListOfCalls();
        Date from = new Date(2020,06,10);
        Date to = new Date(2020,06,14);


        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userGetById));
        when(callRepository.findAllByUserIdBetweenDates(1L,from, to)).thenReturn(calls);

        List<Call> returnedCalls = this.callService.findByUserIdBetweenDates(1L, from, to);

        assertEquals(calls.size(), returnedCalls.size());
        assertTrue(returnedCalls.get(0).getCreationDate().compareTo(from)>=0);
        assertTrue(returnedCalls.get(0).getCreationDate().compareTo(to)<1);
    }


    @Test(expected = UserDoesNotExistException.class)
    public void testFindByUserIdBetweenDatesUserNotExist() throws UserDoesNotExistException {
        Date from = new Date(2020,06,10);
        Date to = new Date(2020,06,14);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        List<Call> returnedCalls = this.callService.findByUserIdBetweenDates(2L, from, to);
    }

}
