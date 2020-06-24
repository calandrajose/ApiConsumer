package com.phones.phones.service;

import com.phones.phones.dto.RateDto;
import com.phones.phones.repository.RateRepository;
import com.phones.phones.repository.dto.RateDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateService {

    private final RateDtoRepository rateDtoRepository;
    private RateRepository rateRepository;


    @Autowired
    public RateService(final RateDtoRepository rateDtoRepository) {
        this.rateDtoRepository = rateDtoRepository;
    }

    public List<RateDto> findAll() {
        return rateDtoRepository.findAll();
    }

    /*
        public RateByCity getRateByCities(Integer idCityFrom, Integer idCityTto) {
            return rateRepository.getRateByCities(idCityFrom, idCityTto);
        }
     */

}
