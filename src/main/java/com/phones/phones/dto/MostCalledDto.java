package com.phones.phones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostCalledDto {

    private String CallerName;
    private String CallerSurname;
    private String mostCalled;

}