package com.phones.phones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer numberCalls;

    @NotNull
    private Float costPrice;

    @NotNull
    private Float totalPrice;

    @JsonIgnore
    private Date creationDate;

    @JsonIgnore
    private Date dueDate;

    @NotNull
    @JsonBackReference(value = "lineInvoice")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_line", nullable = false)
    private Line line;

    @OneToMany(mappedBy = "invoice")
    private List<Call> calls;

}
