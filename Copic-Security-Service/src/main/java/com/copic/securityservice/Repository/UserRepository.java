package com.copic.securityservice.Repository;

import com.copic.securityservice.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query
    UserModel findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
