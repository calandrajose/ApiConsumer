package com.phones.phones.model;

import com.fasterxml.jackson.annotation.*;
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
    private Province province;

    @OneToMany(mappedBy = "city")
    private List<User> users;

    @OneToMany(mappedBy = "originCity")
    private List<Rate> originRates;

    @OneToMany(mappedBy = "destinationCity")
    private List<Rate> destinationRates;

}
