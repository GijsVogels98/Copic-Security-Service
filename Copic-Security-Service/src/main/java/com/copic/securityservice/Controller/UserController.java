package com.copic.securityservice.Controller;


import antlr.Token;
import com.copic.securityservice.Model.AuthRequestModel;
import com.copic.securityservice.Model.JwtResponse;
import com.copic.securityservice.Model.UpdateUserModel;
import com.copic.securityservice.Model.UserModel;
import com.copic.securityservice.Repository.UserRepository;
import com.copic.securityservice.Service.UserService;
import com.copic.securityservice.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@CrossOrigin("*")
@RequestMapping("/security/")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequestModel authRequestModel) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestModel.getUsername(), authRequestModel.getPassword())
            );
        }catch (Exception exception){
            throw new Exception("invalid username or password");
        }
        UserModel user = userRepository.findByUsername(authRequestModel.getUsername());
        user.setLastToken(jwtUtil.generateToken(authRequestModel.getUsername()));
        userRepository.save(user);

        String jwt = jwtUtil.generateToken(authRequestModel.getUsername());

        return ResponseEntity.ok(new JwtResponse(
                user.getId(),
                jwt,
                user.getUsername(),
                user.getEmail()
        ));

    }
    @PostMapping("/delete/{id}")
    public void DeleteUser(@PathVariable Long id){userService.deleteUser(id);}

    @PutMapping("/update_password")
    public ResponseEntity<?> UpdateUserPassword(@RequestBody UpdateUserModel user) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPasswordold())
            );
        }catch (Exception exception){
            throw new Exception("Old password does not match");
        }


        UserModel updateUser = userService.updateUserPassword(user);
        if(updateUser != null) {
           updateUser.setPassword(passwordEncoder.encode(user.getPasswordnew()));
            String jwt = jwtUtil.generateToken(user.getUsername());
            userRepository.save(updateUser);
            return ResponseEntity.ok(new JwtResponse(
                    updateUser.getId(),
                    jwt,
                    updateUser.getUsername(),
                    updateUser.getEmail()

            ));

        }else{
            return new ResponseEntity<String>("Error while updateting user", HttpStatus.EXPECTATION_FAILED);
        }
    }
    @PutMapping("/update_email")
    public ResponseEntity<?> UpdateUserEmail(@RequestBody UpdateUserModel user) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPasswordold())
            );
        }catch (Exception exception){
            throw new Exception("password does not match");
        }


        UserModel updateUser = userService.updateUserEmail(user);
        if(updateUser != null) {
            String jwt = jwtUtil.generateToken(user.getUsername());
            userRepository.save(updateUser);
            return ResponseEntity.ok(new JwtResponse(
                    updateUser.getId(),
                    jwt,
                    updateUser.getUsername(),
                    updateUser.getEmail()

            ));

        }else{
            return new ResponseEntity<String>("Error while updateting user", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/users")
    public List<UserModel> GetAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/sign-up" )
    public UserModel CreateUser(@RequestBody UserModel user) throws Exception {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username already used");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already used");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userService.saveUser(user);
    }
}
