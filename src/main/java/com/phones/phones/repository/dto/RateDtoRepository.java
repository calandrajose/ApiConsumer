package com.phones.phones.repository.dto;

import com.phones.phones.dto.RateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateDtoRepository extends JpaRepository<RateDto, Long> {

    @Query(
            value = "SELECT r.id, co.name AS origin_city, cd.name AS destination_city, r.price_minute FROM rates r " +
                    "INNER JOIN cities co ON r.id_city_origin = co.id " +
                    "INNER JOIN cities cd ON r.id_city_destination = cd.id",
            nativeQuery = true
    )
    List<RateDto> findAll();

}
