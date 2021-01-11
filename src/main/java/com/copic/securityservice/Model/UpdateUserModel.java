package com.copic.securityservice.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_TABLE")
public class UpdateUserModel {

    private String username;
    private String passwordold;
    private String passwordnew;
    private String email;

}
