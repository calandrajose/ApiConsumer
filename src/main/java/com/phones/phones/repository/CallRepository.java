package com.phones.phones.repository;

import com.phones.phones.model.Call;
import com.phones.phones.projection.CallsBetweenDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface    CallRepository extends JpaRepository<Call, Long> {

    @Query(
            value = "SELECT c.* from `calls` c " +
                    "INNER JOIN `lines` l  ON c.id_origin_line = l.id " +
                    "INNER JOIN users u ON u.id = l.id_user " +
                    "WHERE u.id = ?1",
            nativeQuery = true
    )
    List<Call> findAllByUserId(Long id);


    @Query(
            value = "SELECT c.* FROM calls c " +
                    "INNER JOIN `lines` l ON l.id = c.id_origin_line " +
                    "INNER JOIN users u ON u.id = l.id_user " +
                    "WHERE (u.id = ?1) AND (c.creation_date BETWEEN ?2 AND ?3) ",
            nativeQuery = true
    )
    List<Call> findAllByUserIdBetweenDates(Long id, Date from, Date to);


    @Query(
            value = "SELECT l.number FROM `lines` l " +
                    "INNER JOIN (SELECT calls.id_destination_line as 'destination_id', count(id_destination_line) FROM calls c " +
                    "WHERE (calls.id_origin_line = ?1)" +
                    "GROUP BY calls.id_destination_line" +
                    "ORDER BY desc" +
                    "LIMIT 1) as mostCalled" +
                    "ON l.id = mostCalled.destination_id",
            nativeQuery = true
    )
    String findMostCalledByOriginId(Long id);


    /*Trae todas las llamadas ocurridas entre fechas*/
    @Query(value = "select\n" +
            "            from.number as 'From',\n" +
            "            to.number as 'To',\n" +
            "            r.price_minute as 'Rate',\n" +
            "            c.duration as 'Duration',\n" +
            "            c.total_price as 'Total',\n" +
            "            c.creation_date as 'Date'\n" +
            "        from\n" +
            "            calls as c\n" +
            "            inner join lines as from on c.id_origin_line = from.id_destination_line\n" +
            "            inner join lines as to on c.id_destination_line = to.id_phone_line\n" +
            "            inner join rates as r on c.id_rate = r.id\n" +
            "        where\n" +
            "            c.id_origin_line in (select id from lines where id_user = :userId) and\n" +
            "            c.creation_date between :fromDate and :toDate\n" +
            "        order by\n" +
            "            c.number;", nativeQuery = true)
    List<CallsBetweenDates> findByDates(@Param("userId")Integer clientId, @Param("fromDate") Date from, @Param("toDate") Date to);


    @Query(
            value = "SELECT c.* from `calls` c " +
                    "INNER JOIN `lines` l ON c.id_origin_line = l.id " +
                    "WHERE l.id = ?1",
            nativeQuery = true
    )
    List<Call> findAllByLineId(Long id);

}
