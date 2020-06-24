package com.phones.phones.repository;

import com.phones.phones.model.City;
import com.phones.phones.projection.CityTop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query(
            value = "SELECT c.* FROM user u " +
                    "INNER JOIN cities c ON u.id_city = c.id " +
                    "WHERE u.id = ?1",
            nativeQuery = true
    )
    Optional<City> findByUserId(Long id);

    Optional<City> findByName(String name);

    @Query(
            value = "SELECT c.name, count(c.id) AS quantity FROM cities c " +
                    "INNER JOIN rates r ON c.id = r.id_city_destination " +
                    "INNER JOIN calls ca ON ca.id_rate = r.id " +
                    "INNER JOIN `lines` l ON ca.id_origin_line = l.id " +
                    "INNER JOIN users u ON l.id_user = u.id " +
                    "WHERE u.id = ?1 " +
                    "GROUP BY c.name " +
                    "ORDER BY quantity DESC LIMIT 10",
            nativeQuery = true
    )
    List<CityTop> findCitiesTopByUserId(Long id);

}
