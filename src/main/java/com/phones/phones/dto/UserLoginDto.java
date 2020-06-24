package com.phones.phones.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDto implements Serializable {

    private String username;
    private String password;

}
