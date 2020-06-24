package com.phones.phones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "`calls`")
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer duration;

    private Float totalPrice;

    @JsonIgnore
    private Date creationDate;

    private String originNumber;

    private String destinationNumber;

    @JsonBackReference(value = "originLineCall")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_origin_line", nullable = false)
    private Line originLine;

    @JsonBackReference(value = "destinationLineCall")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_destination_line", nullable = false)
    private Line destinationLine;

    @JsonBackReference(value = "rateCall")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rate", nullable = false)
    private Rate rate;

    @JsonBackReference(value = "invoiceCall")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_invoice", nullable = false)
    private Invoice invoice;

}
