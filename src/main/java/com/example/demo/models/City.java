package com.example.demo.models;

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
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String prefix;

    @NotNull
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_province", nullable = false)
    private com.phones.phones.model.Province province;

    @OneToMany(mappedBy = "city")
    private List<com.phones.phones.model.User> users;

    @OneToMany(mappedBy = "originCity")
    private List<Rate> originRates;

    @OneToMany(mappedBy = "destinationCity")
    private List<Rate> destinationRates;

}
