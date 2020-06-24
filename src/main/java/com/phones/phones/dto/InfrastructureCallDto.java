package com.phones.phones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfrastructureCallDto {

    private String originNumber;
    private String destinationNumber;
    private Integer duration;
    private Date creationDate;

}
