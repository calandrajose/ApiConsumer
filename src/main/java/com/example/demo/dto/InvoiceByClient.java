package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceByClient {
   private Integer idInvoice;
   private String phoneLineNumber;
   private Integer numberOfCall;
   private Float priceCost;
   private Date invoiceDate;
   private Date dueDate;
   private Float totalPrice;
   private Boolean paid;
   private String  firstName;
   private String  lastName;
}
