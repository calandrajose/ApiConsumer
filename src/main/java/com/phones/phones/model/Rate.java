package com.phones.phones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Float priceMinute;

    @NotNull
    private Float cost;

    @NotNull
    @JsonBackReference(value = "originCityRate")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_city_origin", nullable = false)
    private City originCity;

    @NotNull
    @JsonBackReference(value = "destinationCityRate")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_city_destination", nullable = false)
    private City destinationCity;

    @OneToMany(mappedBy = "rate")
    private List<Call> calls;

}
