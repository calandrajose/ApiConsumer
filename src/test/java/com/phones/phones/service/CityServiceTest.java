package com.phones.phones.service;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.city.CityAlreadyExistException;
import com.phones.phones.exception.city.CityDoesNotExistException;
import com.phones.phones.exception.user.UserDoesNotExistException;
import com.phones.phones.model.City;
import com.phones.phones.projection.CityTop;
import com.phones.phones.repository.CityRepository;
import com.phones.phones.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CityServiceTest {

    CityService cityService;

    @Mock
    UserRepository userRepository;

    @Mock
    CityRepository cityRepository;

    @Before
    public void setUp() {
        initMocks(this);
        this.cityService = new CityService(cityRepository, userRepository);
    }

    @Test
    public void testCreateOk() throws CityAlreadyExistException {

        City newCity = TestFixture.testCity();

        when(cityRepository.findByName(newCity.getName())).thenReturn(Optional.empty());
        when(cityRepository.save(newCity)).thenReturn(newCity);

        City returnedCity = this.cityService.create(newCity);

        assertEquals(newCity.getId(), returnedCity.getId());
        assertEquals(newCity.getName(), returnedCity.getName());
    }


    @Test(expected = CityAlreadyExistException.class)
    public void testCreateCityAlreadyExists() throws CityAlreadyExistException {

        City newCity = TestFixture.testCity();

        when(cityRepository.findByName(newCity.getName())).thenReturn(Optional.ofNullable(newCity));
        cityService.create(newCity);
    }


    @Test
    public void testFindAllOk() {
        List<City> allCities = TestFixture.testListOfCities();
        when(cityRepository.findAll()).thenReturn(allCities);

        List<City> returnedCities = cityService.findAll();

        assertEquals(returnedCities.size(), allCities.size());
        assertEquals(returnedCities.get(0).getId(), allCities.get(0).getId());
    }


    @Test
    public void testFindByIdOk() throws CityDoesNotExistException {

        City newCity = TestFixture.testCity();

        when(cityRepository.findById(1L)).thenReturn(Optional.ofNullable(newCity));

        City returnedCity = this.cityService.findById(1L);

        assertEquals(newCity.getId(), returnedCity.getId());
    }



    @Test(expected = CityDoesNotExistException.class)
    public void testFindByIdCityDoesNotExist() throws CityDoesNotExistException {

        City newCity = TestFixture.testCity();

        when(cityRepository.findById(1L)).thenReturn(Optional.empty());
        cityService.findById(1L);
    }

    @Test
    public void testFindTopCitiesCallsByUserId() throws UserDoesNotExistException {
        List<CityTop> topCities = new ArrayList<>();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(TestFixture.testUser()));
        when(cityRepository.findCitiesTopByUserId(1L)).thenReturn(topCities);

        List<CityTop> returnedCities = cityService.findTopCitiesCallsByUserId(1L);

        assertEquals(topCities.isEmpty(), returnedCities.isEmpty());
       // assertEquals(topCities.get(0).getName(), returnedCities.get(0).getName());
    }

}
