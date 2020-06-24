package com.phones.phones.service;

import com.phones.phones.TestFixture;
import com.phones.phones.dto.LineDto;
import com.phones.phones.exception.line.LineAlreadyDisabledException;
import com.phones.phones.exception.line.LineDoesNotExistException;
import com.phones.phones.exception.line.LineNumberAlreadyExistException;
import com.phones.phones.exception.user.UserDoesNotExistException;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.repository.LineRepository;
import com.phones.phones.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LineServiceTest {

    LineService lineService;

    @Mock
    UserRepository userRepository;

    @Mock
    LineRepository lineRepository;


    @Before
    public void setUp() {
        initMocks(this);
        this.lineService = new LineService(lineRepository, userRepository);
    }


    @Test
    public void testCreateOk() throws LineNumberAlreadyExistException {
        Line newLine = TestFixture.testLine("2235472861");
        System.out.println(newLine);
        when(lineRepository.findByNumber(newLine.getNumber())).thenReturn(Optional.empty());
        when(lineRepository.save(newLine)).thenReturn(newLine);

        Line returnedLine = this.lineService.create(newLine);

        assertEquals(newLine.getId(), returnedLine.getId());
        assertEquals(newLine.getNumber(), returnedLine.getNumber());
    }

    @Test(expected = LineNumberAlreadyExistException.class)
    public void testCreateLineNumberAlreadyExist() throws LineNumberAlreadyExistException {
        Line newLine = TestFixture.testLine("2235472861");
        when(lineRepository.findByNumber(newLine.getNumber())).thenReturn(Optional.ofNullable(newLine));
        this.lineService.create(newLine);
    }

    @Test
    public void testFindAllOk() {
        List<Line> allInvoices = TestFixture.testListOfLines();
        when(lineRepository.findAll()).thenReturn(allInvoices);

        List<Line> invoicesReturned = lineService.findAll();

        assertEquals(invoicesReturned.size(), allInvoices.size());
        assertEquals(invoicesReturned.get(0).getId(), allInvoices.get(0).getId());
    }


    @Test
    public void testFindAllEmpty() {
        List<Line> emptyList = new ArrayList<>();
        when(lineRepository.findAll()).thenReturn(emptyList);

        List<Line> returnedCalls = lineService.findAll();
        assertEquals(returnedCalls.size(), 0);
    }


    @Test
    public void testFindByIdOk() throws LineDoesNotExistException {
        Line lineGetById = TestFixture.testLine("2235472861");

        when(lineRepository.findById(1L)).thenReturn(Optional.ofNullable(lineGetById));

        Line returnedInvoice = this.lineService.findById(1L);

        assertEquals(lineGetById.getId(), returnedInvoice.getId());
    }


    @Test(expected = LineDoesNotExistException.class)
    public void testFindByIdInvoiceDoesNotExist() throws LineDoesNotExistException {
        when(lineRepository.findById(2L)).thenReturn(Optional.empty());
        this.lineService.findById(2L);
    }

    @Test
    public void testFindByUserIdOk() throws UserDoesNotExistException {
        User userGetById = TestFixture.testUser();
        List<Line> lines = TestFixture.testListOfLines();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userGetById));
        when(lineRepository.findAllByUserId(1L)).thenReturn(lines);

        List<Line> returnedLines = this.lineService.findByUserId(1L);
        assertEquals(lines.size(), returnedLines.size());
        assertEquals(lines.get(0).getId(), returnedLines.get(0).getId());
    }


    @Test(expected = UserDoesNotExistException.class)
    public void testFindByUserIdUserNotExist() throws UserDoesNotExistException {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        List<Line> returnedLines = this.lineService.findByUserId(2L);
    }


    /***
     * Returns 1 if disabled
     * @throws LineDoesNotExistException
     * @throws LineAlreadyDisabledException
     */
    @Test
    public void testDisableByIdOk() throws LineDoesNotExistException, LineAlreadyDisabledException {
        Line disabledLine = TestFixture.testLine("2235472861");
        when(lineRepository.findById(1L)).thenReturn(Optional.ofNullable(disabledLine));
        when(lineRepository.disableById(1L)).thenReturn(1);
        int disabledTrue = lineService.disableById(disabledLine.getId());
        assertEquals(1, disabledTrue);
    }


    @Test(expected = LineDoesNotExistException.class)
    public void testDisableByIdLineDoesNotExistException() throws LineDoesNotExistException, LineAlreadyDisabledException {

        when(lineRepository.findById(2L)).thenReturn(Optional.empty());
        this.lineService.disableById(2L);
    }

    @Test(expected = LineAlreadyDisabledException.class)
    public void testDisableByIdLineAlreadyDisabled() throws LineDoesNotExistException, LineAlreadyDisabledException {
        Line disabledLine = TestFixture.testDisabledLine("2235472862");

        when(lineRepository.findById(1L)).thenReturn(Optional.ofNullable(disabledLine));
        this.lineService.disableById(1L);
    }


    @Test
    public void testUpdateLineByIdOk() throws LineDoesNotExistException {
        LineDto updatedLine = TestFixture.testLineDto();
        Line toUpdateLine = TestFixture.testLine("2235472861");
        when(lineRepository.findById(1L)).thenReturn(Optional.ofNullable(toUpdateLine));
        when(lineRepository.updateById(1L, toUpdateLine.getNumber(), String.valueOf(toUpdateLine.getStatus()),
                toUpdateLine.getLineType().getId())).thenReturn(4);
        boolean update = lineService.updateLineByIdLine(toUpdateLine.getId(), updatedLine);
        assertEquals(true, update);
    }


    @Test(expected = LineDoesNotExistException.class)
    public void testUpdateLineByIdLineDoesNotExist() throws LineDoesNotExistException {
        Line toUpdateLine = TestFixture.testLine("2235472861");
        when(lineRepository.findById(1L)).thenReturn(Optional.empty());
        lineService.updateLineByIdLine(toUpdateLine.getId(), TestFixture.testLineDto());

    }
    /*






     */

}
