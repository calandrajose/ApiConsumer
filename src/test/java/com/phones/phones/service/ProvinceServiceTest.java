package com.phones.phones.service;

import com.phones.phones.TestFixture;
import com.phones.phones.exception.province.ProviceAlreadyExistException;
import com.phones.phones.exception.province.ProvinceDoesNotExistException;
import com.phones.phones.model.Province;
import com.phones.phones.repository.ProvinceRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProvinceServiceTest {

    ProvinceService provinceService;

    @Mock
    ProvinceRepository provinceRepository;

    @Before
    public void setUp() {
        initMocks(this);
        this.provinceService = new ProvinceService(provinceRepository);
    }

    @Test
    public void testCreateOk() throws ProviceAlreadyExistException {

        Province newProvince = TestFixture.testProvince();

        when(provinceRepository.findByName(newProvince.getName())).thenReturn(Optional.empty());
        when(provinceRepository.save(newProvince)).thenReturn(newProvince);

        Province returnedProvince = this.provinceService.create(newProvince);
        System.out.println(returnedProvince);

        assertEquals(newProvince.getId(), returnedProvince.getId());
        assertEquals(newProvince.getName(), returnedProvince.getName());
    }


    @Test(expected = ProviceAlreadyExistException.class)
    public void testCreateExistingProvince() throws ProviceAlreadyExistException {

        Province newProvince = TestFixture.testProvince();

        when(provinceRepository.findByName(newProvince.getName())).thenReturn(Optional.ofNullable(newProvince));
        provinceService.create(newProvince);
    }


    @Test
    public void testFindAllOk() {
        List<Province> allProvinces = TestFixture.testListOfProvinces();
        when(provinceRepository.findAll()).thenReturn(allProvinces);

        List<Province> returnedUsers = provinceService.findAll();

        assertEquals(returnedUsers.size(), allProvinces.size());
        assertEquals(returnedUsers.get(0).getId(), allProvinces.get(0).getId());
    }

    @Test
    public void testFindByIdOk() throws ProvinceDoesNotExistException {

        Province newProvince = TestFixture.testProvince();

        when(provinceRepository.findById(1L)).thenReturn(Optional.ofNullable(newProvince));

        Province returnedProvince = this.provinceService.findById(1L);

        assertEquals(newProvince.getId(), returnedProvince.getId());
    }



    @Test(expected = ProvinceDoesNotExistException.class)
    public void testFindByIdProvinceDoesNotExist() throws ProvinceDoesNotExistException {

        Province newProvince = TestFixture.testProvince();

        when(provinceRepository.findById(1L)).thenReturn(Optional.empty());
        provinceService.findById(1L);
    }
}
