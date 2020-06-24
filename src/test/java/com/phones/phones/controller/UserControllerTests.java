package com.phones.phones.controller;

import com.phones.phones.TestFixture;
import com.phones.phones.dto.UserDto;
import com.phones.phones.exception.user.*;
import com.phones.phones.model.Call;
import com.phones.phones.model.Invoice;
import com.phones.phones.model.Line;
import com.phones.phones.model.User;
import com.phones.phones.service.*;
import com.phones.phones.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserControllerTests {
    UserController userController;

    @Mock
    UserService userService;
    @Mock
    CallService callService;
    @Mock
    LineService lineService;
    @Mock
    CityService cityService;
    @Mock
    InvoiceService invoiceService;
    @Mock
    SessionManager sessionManager;

    @Before
    public void setUp() {
        initMocks(this);
        userController = new UserController(userService, callService, lineService, cityService, invoiceService, sessionManager);
    }

/***
    @Test
    public void createUserOk() throws UserSessionDoesNotExistException, UserAlreadyExistException, UsernameAlreadyExistException {
        User loggedUser = TestFixture.testUser();
        User newUser = TestFixture.testClientUser();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.create(newUser)).thenReturn(newUser);

        ResponseEntity <User> createdUser = userController.createUser("123", newUser);

        assertEquals(loggedUser.getId(), createdUser.getBody().getId());
        assertEquals(loggedUser.getDni(), createdUser.getBody().getDni());
       // assertEquals(loggedUser.getLines().size(), createdUser.getBody().getLines().size());
    }
    todo create
 */

    /**
     *
     * findAllUsers
     *
     * */


    @Test
    public void findAllUsersOk() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<User> users = TestFixture.testListOfUsers();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<User>> dbUsers = userController.findAllUsers("123");

        assertEquals(users.size(), dbUsers.getBody().size());
        assertEquals(users.get(0).getDni(), dbUsers.getBody().get(0).getDni());
    }


    @Test
    public void findAllUsersNotEmployeeLogged() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<List<User>> returnedUsers = userController.findAllUsers("123");
        assertEquals(response.getStatusCode(), returnedUsers.getStatusCode());
    }


    @Test
    public void findAllUsersNoUsersFound() throws UserSessionDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<User> noUsers = new ArrayList<>();
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.findAll()).thenReturn(noUsers);

        ResponseEntity<List<User>> returnedUsers = userController.findAllUsers("123");

        assertEquals(response.getStatusCode(), returnedUsers.getStatusCode());
    }



    /**
     *
     * findUserById
     *
     * */

    @Test
    public void findUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        User user = TestFixture.testClientUser();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<User> returnedUser = userController.findUserById("123", 1L);

        assertEquals(user.getId(), returnedUser.getBody().getId());
        assertEquals(user.getDni(), returnedUser.getBody().getDni());
        assertEquals(user.getSurname(), returnedUser.getBody().getSurname());
        assertEquals(1L, returnedUser.getBody().getId());
    }

    @Test
    public void findUserByIdUserIsNotEmployee() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<User> returnedUser = userController.findUserById("123", 1L);
        assertEquals(response.getStatusCode(), returnedUser.getStatusCode());
    }


    /**
     *
     * deleteUserById
     *
     * */

    @Test
    public void deleteUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, UserAlreadyDisableException {
        User loggedUser = TestFixture.testUser();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.disableById(1L)).thenReturn(true);

        ResponseEntity deleted = userController.deleteUserById("123", 1L);

        assertEquals(ResponseEntity.ok().build(), deleted);
    }

    @Test
    public void deleteUserByIdUserIsNotEmployee() throws UserSessionDoesNotExistException, UserDoesNotExistException, UserAlreadyDisableException {
        User loggedUser = TestFixture.testClientUser();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity deleted = userController.deleteUserById("123", 1L);
        assertEquals(response.getStatusCode(), deleted.getStatusCode());
    }


    /**
     *
     * updateUserById
     *
     * */


    @Test
    public void updateUserByIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, UsernameAlreadyExistException {
        User loggedUser = TestFixture.testUser();
        User updatedUser = TestFixture.testClientUser();
        UserDto userUpdate = TestFixture.testUserDto();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(userService.updateById(1L, userUpdate)).thenReturn(updatedUser);

        ResponseEntity updated = userController.updateUserById("123", userUpdate, 1L);

        assertEquals(ResponseEntity.ok().build(), updated);
    }

    @Test
    public void updateUserByIdUserIsNotEmployee() throws UserSessionDoesNotExistException, UserDoesNotExistException, UsernameAlreadyExistException {
        User loggedUser = TestFixture.testClientUser();
        UserDto userUpdate = TestFixture.testUserDto();
        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity updated = userController.updateUserById("123", userUpdate,1L);
        assertEquals(response.getStatusCode(), updated.getStatusCode());
    }


    /**
     *
     * findCallsByUserId
     *
     * */



    @Test
    public void findCallsByUserIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Call> testCalls = TestFixture.testListOfCalls();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findByUserId(1L)).thenReturn(testCalls);

        ResponseEntity<List<Call>> returnedCalls = userController.findCallsByUserId("123", 1L);

        assertEquals(testCalls.size(), returnedCalls.getBody().size());
        assertEquals(testCalls.get(0).getOriginNumber(), returnedCalls.getBody().get(0).getOriginNumber());
    }

    @Test
    public void findCallsByUserIdUserIsNotEmployee() throws UserSessionDoesNotExistException, UserDoesNotExistException, UserAlreadyDisableException, UsernameAlreadyExistException {
        User loggedUser = TestFixture.testClientUser();

        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<List<Call>> returnedCalls = userController.findCallsByUserId("123", 1L);
        assertEquals(response.getStatusCode(), returnedCalls.getStatusCode());
    }


    @Test
    public void findAllCallsByUserIdNoCallsDone() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Call> emptyCalls = new ArrayList<>();

        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findByUserId(1L)).thenReturn(emptyCalls);

        ResponseEntity<List<Call>> returnedCalls = userController.findCallsByUserId("123", 1L);

        assertEquals(response.getStatusCode(), returnedCalls.getStatusCode());
    }




    /**
     *
     * findCallsByUserSessionBetweenDates
     *
     * */


    @Test
    public void findCallsByUserSessionBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        List<Call> testCalls = TestFixture.testListOfCalls();
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2020");
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("19/06/2020");
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findByUserIdBetweenDates(1L, fromDate, toDate)).thenReturn(testCalls);

        ResponseEntity<List<Call>> returnedCalls = userController.findCallsByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(testCalls.size(), returnedCalls.getBody().size());
        assertEquals(testCalls.get(0).getOriginNumber(), returnedCalls.getBody().get(0).getOriginNumber());
    }



    @Test
    public void findCallsByUserSessionBetweenDatesNoCallsFound() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        List<Call> emptyCalls = new ArrayList<>();
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2020");
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("19/06/2020");
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(callService.findByUserIdBetweenDates(1L, fromDate, toDate)).thenReturn(emptyCalls);

        ResponseEntity<List<Call>> returnedCalls = userController.findCallsByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(response.getStatusCode(), returnedCalls.getStatusCode());
    }



    @Test(expected =ValidationException.class)
    public void findCallsByUserSessionBetweenDatesInvalidParam() throws ValidationException, ParseException, UserSessionDoesNotExistException, UserDoesNotExistException {
        String from = null;
        String to = null;
        userController.findCallsByUserSessionBetweenDates("123", from, to);
    }


    /**
     *
     * findLinesByUserId
     *
     * */


    @Test
    public void findLinesByUserIdOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Line> testLines = TestFixture.testListOfLines();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(lineService.findByUserId(1L)).thenReturn(testLines);

        ResponseEntity<List<Line>> returnedCalls = userController.findLinesByUserId("123", 1L);

        assertEquals(testLines.size(), returnedCalls.getBody().size());
        assertEquals(testLines.get(0).getUser(), returnedCalls.getBody().get(0). getUser());
        assertEquals(testLines.get(0).getUser().getUsername(), returnedCalls.getBody().get(0). getUser().getUsername());
        assertEquals("rl", returnedCalls.getBody().get(0). getUser().getUsername());
        assertEquals("404040", returnedCalls.getBody().get(0). getUser().getDni());
    }

    @Test
    public void findLinesByUserIdUserIsNotEmployee() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();

        ResponseEntity response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);

        ResponseEntity<List<Line>> returnedLines = userController.findLinesByUserId("123", 1L);
        assertEquals(response.getStatusCode(), returnedLines.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, returnedLines.getStatusCode());
    }


    @Test
    public void findLinesByUserIdNoLinesFound() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Line> emptyLines = new ArrayList<>();

        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(lineService.findByUserId(1L)).thenReturn(emptyLines);

        ResponseEntity<List<Line>> returnedLines = userController.findLinesByUserId("123", 1L);

        assertEquals(response.getStatusCode(), returnedLines.getStatusCode());
    }


    /**
     *
     * findLinesByUserSession
     *
     * */


    @Test
    public void findLinesByUserSessionOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<Line> testLines = TestFixture.testListOfLines();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(lineService.findByUserId(loggedUser.getId())).thenReturn(testLines);

        ResponseEntity<List<Line>> returnedLines = userController.findLinesByUserSession("123");

        assertEquals(testLines.size(), returnedLines.getBody().size());
        assertEquals(testLines.get(0).getUser(), returnedLines.getBody().get(0). getUser());
        assertEquals(testLines.get(0).getUser().getUsername(), returnedLines.getBody().get(0). getUser().getUsername());
        assertEquals("rl", returnedLines.getBody().get(0). getUser().getUsername());
        assertEquals("404040", returnedLines.getBody().get(0). getUser().getDni());
    }


    @Test
    public void findLinesByUserSessionNoLinesFound() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        List<Line> emptyLines = new ArrayList<>();

        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(lineService.findByUserId(loggedUser.getId())).thenReturn(emptyLines);

        ResponseEntity<List<Line>> returnedLines = userController.findLinesByUserSession("123");

        assertEquals(response.getStatusCode(), returnedLines.getStatusCode());
    }



    /**
     *
     * findInvoicesByUserSessionBetweenDates
     *
     * */


    @Test
    public void findInvoicesByUserSessionBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        List<Invoice> testInvoices = TestFixture.testListOfInvoices();
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2020");
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("19/06/2020");
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findByUserIdBetweenDates(loggedUser.getId(), fromDate, toDate)).thenReturn(testInvoices);

        ResponseEntity<List<Invoice>> returnedInvoices = userController.findInvoicesByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(testInvoices.size(), returnedInvoices.getBody().size());
        assertEquals(testInvoices.get(0).getTotalPrice(), returnedInvoices.getBody().get(0).getTotalPrice());
    }



    @Test
    public void findInvoicesByUserSessionBetweenDatesNoInvoicesFound() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        List<Invoice> emptyInvoiceList = new ArrayList<>();
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2020");
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("19/06/2020");
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findByUserIdBetweenDates(loggedUser.getId(), fromDate, toDate)).thenReturn(emptyInvoiceList);

        ResponseEntity<List<Invoice>> returnedInvoices = userController.findInvoicesByUserSessionBetweenDates("123", "05/01/2020", "19/06/2020");

        assertEquals(response.getStatusCode(), returnedInvoices.getStatusCode());
    }



    @Test(expected =ValidationException.class)
    public void findInvoicesByUserSessionBetweenDatesNoInvalidParam() throws ValidationException, ParseException, UserSessionDoesNotExistException, UserDoesNotExistException {
        String from = null;
        String to = null;
        userController.findInvoicesByUserSessionBetweenDates("123", from, to);
    }


    /**
     *
     * findInvoicesByUserIdBetweenDates
     *
     * */


    @Test
    public void findInvoicesByUserIdBetweenDatesBetweenDatesOk() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        List<Invoice> testInvoices = TestFixture.testListOfInvoices();
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2020");
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("19/06/2020");
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findByUserIdBetweenDates(1L, fromDate, toDate)).thenReturn(testInvoices);

        ResponseEntity<List<Invoice>> returnedInvoices = userController.findInvoicesByUserIdBetweenDates("123", 1L,"05/01/2020", "19/06/2020");

        assertEquals(testInvoices.size(), returnedInvoices.getBody().size());
        assertEquals(testInvoices.get(0).getTotalPrice(), returnedInvoices.getBody().get(0).getTotalPrice());
    }



    @Test
    public void findInvoicesByUserIdBetweenDatesNoInvoicesFound() throws UserSessionDoesNotExistException, UserDoesNotExistException, ParseException {
        User loggedUser = TestFixture.testUser();
        List<Invoice> emptyInvoiceList = new ArrayList<>();
        Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2020");
        Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse("19/06/2020");
        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(invoiceService.findByUserIdBetweenDates(1L, fromDate, toDate)).thenReturn(emptyInvoiceList);

        ResponseEntity<List<Invoice>> returnedInvoices = userController.findInvoicesByUserIdBetweenDates("123", 1L,"05/01/2020", "19/06/2020");

        assertEquals(response.getStatusCode(), returnedInvoices.getStatusCode());
    }


    @Test(expected =ValidationException.class)
    public void findInvoicesByUserIdBetweenDatesInvalidParam() throws ValidationException, ParseException, UserSessionDoesNotExistException, UserDoesNotExistException {
        String from = null;
        String to = null;
        userController.findInvoicesByUserIdBetweenDates("123", 1L,from, to);
    }


    /**
     *
     * findTopCitiesCallsByUserSession
     *
     * */
/**todo findTopCitiesCallsByUserSessionTEST*

    @Test
    public void findTopCitiesCallsByUserSessionOk() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testUser();
        List<CityTop> testLines = TestFixture.();

        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(lineService.findByUserId(loggedUser.getId())).thenReturn(testLines);

        ResponseEntity<List<Line>> returnedLines = userController.findLinesByUserSession("123");

        assertEquals(testLines.size(), returnedLines.getBody().size());
        assertEquals(testLines.get(0).getUser(), returnedLines.getBody().get(0). getUser());
        assertEquals(testLines.get(0).getUser().getUsername(), returnedLines.getBody().get(0). getUser().getUsername());
        assertEquals("rl", returnedLines.getBody().get(0). getUser().getUsername());
        assertEquals("404040", returnedLines.getBody().get(0). getUser().getDni());
    }


    @Test
    public void findTopCitiesCallsByUserSessionNoCallsFound() throws UserSessionDoesNotExistException, UserDoesNotExistException {
        User loggedUser = TestFixture.testClientUser();
        List<Line> emptyLines = new ArrayList<>();

        ResponseEntity response = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        when(sessionManager.getCurrentUser("123")).thenReturn(loggedUser);
        when(lineService.findByUserId(loggedUser.getId())).thenReturn(emptyLines);

        ResponseEntity<List<Line>> returnedLines = userController.findLinesByUserSession("123");

        assertEquals(response.getStatusCode(), returnedLines.getStatusCode());
    }
*/


    /**
     *
     * login
     *
     * */

    @Test
    public void testLoginOk() throws UserInvalidLoginException {
        User loginUser = TestFixture.testUser();
        String username = "rl";
        String password = "123";

        when(userService.login(username, password)).thenReturn(Optional.ofNullable(loginUser));

        Optional<User> loggedUser = userController.login(username, password);

        assertTrue(loggedUser.isPresent());
        assertEquals(loginUser.getUsername(), loggedUser.get().getUsername());
        assertEquals(loginUser.getDni(), loggedUser.get().getDni());
    }

    /**todo getLocation */
}


