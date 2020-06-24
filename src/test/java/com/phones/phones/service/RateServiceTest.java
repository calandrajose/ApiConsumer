package com.phones.phones.service;

import com.phones.phones.TestFixture;
import com.phones.phones.dto.RateDto;
import com.phones.phones.repository.dto.RateDtoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RateServiceTest {

    RateService rateService;

    @Mock
    RateDtoRepository rateDtoRepository;

    @Before
    public void setUp() {
        initMocks(this);
        this.rateService = new RateService(rateDtoRepository);
    }


    @Test
    public void testFindAllOk() {
        List<RateDto> rates = TestFixture.testListOfRatesDto();
        when(rateDtoRepository.findAll()).thenReturn(rates);

        List<RateDto> returnedRates = rateService.findAll();

        assertEquals(returnedRates.size(), rates.size());
        assertEquals(returnedRates.get(0).getId(), rates.get(0).getId());
    }

}
