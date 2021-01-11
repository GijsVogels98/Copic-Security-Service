package com.copic.securityservice.Service;

import com.copic.securityservice.Model.UpdateUserModel;
import com.copic.securityservice.Model.UserModel;
import com.copic.securityservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserModel> getAllUsers(){
        return userRepository.findAll();
    }

    public UserModel saveUser(UserModel user){ return userRepository.save(user); }

    public void deleteUser(Long id){userRepository.deleteById(id);}

    public UserModel updateUserPassword(UpdateUserModel user){
        UserModel foundUser = userRepository.findByUsername(user.getUsername());
        foundUser.setPassword(user.getPasswordnew());
        return userRepository.findByUsername(foundUser.getUsername());
    }

    public UserModel updateUserEmail(UpdateUserModel user){
        UserModel foundUser = userRepository.findByUsername(user.getUsername());
        foundUser.setEmail((user.getEmail()));
        return userRepository.findByUsername(foundUser.getUsername());
    }



}
