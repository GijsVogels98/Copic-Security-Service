package com.copic.securityservice.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    @Id
    Long id;
    String accessToken;
    String username;
    String email;

}
