package com.copic.securityservice;


import com.copic.securityservice.Controller.UserController;
import com.copic.securityservice.Model.AuthRequestModel;
import com.copic.securityservice.Model.UpdateUserModel;
import com.copic.securityservice.Model.UserModel;
import com.copic.securityservice.Repository.UserRepository;
import com.copic.securityservice.Service.CustomUserDetailService;
import com.copic.securityservice.Service.UserService;
import com.copic.securityservice.Util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.hamcrest.Matchers.is;


import java.util.*;


@WebMvcTest(UserController.class)
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TestEntityManager testEntityManager;

    @MockBean
    private AuthRequestModel authRequestModel;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUtil util;

    @MockBean
    private UserRepository userRepository;

    private List<UserModel> userList;

    private String genToken(String username){
        return util.generateToken(username);
    };


    @BeforeEach
    void setup() {
        userList = new ArrayList<>();
        userList.add(new UserModel("tester", "test", "test@test.com"));
        userList.add(new UserModel("test2", "test2", "test2@test.com"));
        userList.add(new UserModel("test3", "test3", "test3@test.com"));

    }


    @Test
    public void fetchAllUsersTest() throws Exception {
        given(userService.getAllUsers()).willReturn(userList);

        mockMvc.perform(get("/security/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(userList.size())));

    }

    @Test
    public void shouldCreateUser() throws Exception{
        given(userService.saveUser(any(UserModel.class))).willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        UserModel user = new UserModel("test", "test" ,"test@test.com");
        Gson data = new Gson();

        mockMvc.perform(post("/security/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data.toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

//    @Test
//    public void updateUserPassword() throws Exception{
//        given(userService.saveUser(any(UserModel.class))).willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));
//        UserModel user = new UserModel(
//                "test",
//                "test",
//                "test@test.com"
//        );
//
//        UserModel test = new UserModel();
//
////        UpdateUserModel updateUser = new UpdateUserModel();
////        updateUser.setUsername("test");
////        updateUser.setPasswordold("test");
////        updateUser.setPasswordnew("tester");
//        Gson data = new Gson();
////        when(customUserDetailService.loadUserByUsername(user.getUsername())).thenReturn();
//        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
//        MvcResult mvcResult =  this.mockMvc.perform(put("/security/update_password")
//                .content(data.toJson(new UpdateUserModel("test", "tester", "test", "email2@mail.com")))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.password").value("tester"))
//                .andReturn();
//
//        Assert.assertEquals("application/json",
//                mvcResult.getResponse().getContentType());
//    }



//    @Test
//    public void loginTest() throws Exception {
//        given(userService.saveUser(any(UserModel.class))).willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));
//        UserModel testuser = new UserModel(
//                "test",
//                "test",
//                "test@test.com"
//        );
//
//
//        AuthRequestModel user = new AuthRequestModel(
//                "tester",
//                "test"
//        );
//        Gson data = new Gson();
//        mockMvc.perform(post("/security/sign-in")
//                .content(data.toJson(user))
//                .contentType("application/json"))
//                .andExpect(status().isOk());
//
//    }


}
