package com.phones.phones.repository;

import com.phones.phones.model.Rate;
import com.phones.phones.projection.RateByCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    @Query(value = "select" +
            " rates.price" +
            " from rates " +
            " where rates.id_city_origin = :id_city_origin and rates.id_city_destination = :id_city_destination", nativeQuery = true)
    RateByCity getRateByCities(@Param("id_city_origin") Integer idCityFrom, @Param("id_city_destination") Integer idCityTo);

}
