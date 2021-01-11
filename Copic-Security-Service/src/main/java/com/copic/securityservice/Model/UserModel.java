package com.copic.securityservice.Model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collector;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_TABLE")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String username;
    private String password;

    private String email;

    private String lastToken;

    public UserModel(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
