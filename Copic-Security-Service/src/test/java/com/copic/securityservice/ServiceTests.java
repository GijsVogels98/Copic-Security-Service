package com.copic.securityservice;

import com.copic.securityservice.Model.UpdateUserModel;
import com.copic.securityservice.Model.UserModel;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import com.copic.securityservice.Repository.UserRepository;
import com.copic.securityservice.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ServiceTests {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getUsersTest(){
        List<UserModel> list = new ArrayList<>();
        list.add(
                new UserModel("test","Test","Test@test.com")
        );
        list.add(
                new UserModel("test2","Test2","Test2@test.com")
        );
        when(userRepository.findAll()).thenReturn(list);
        List<UserModel> empList = service.getAllUsers();
        assertEquals(2, empList.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void saveUserTest(){
        UserModel user = new UserModel("test","test","test@test.com");
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, service.saveUser(user));
    }

    @Test
    public void updatePasswordTest(){
        UserModel user = new UserModel("test", "test" ,"test@test.com" );
        UpdateUserModel updateUserModel = new UpdateUserModel();
        updateUserModel.setUsername("test");
        updateUserModel.setPasswordold("test");
        updateUserModel.setPasswordnew("tester");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        service.updateUserPassword(updateUserModel);
        assertEquals(user.getPassword(), "tester");
    }

    @Test
    public void updateEmailTest(){
        UserModel user = new UserModel("test", "test" ,"test@test.com" );
        UpdateUserModel updateUserModel = new UpdateUserModel();
        updateUserModel.setUsername("test");
        updateUserModel.setPasswordold("test");
        updateUserModel.setEmail("test2@test.com");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        service.updateUserEmail(updateUserModel);
        assertEquals(user.getEmail(), "test2@test.com");
    }

}
