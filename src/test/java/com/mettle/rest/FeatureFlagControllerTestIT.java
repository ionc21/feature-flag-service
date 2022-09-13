package com.mettle.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mettle.auth.dto.AuthenticationResponse;
import com.mettle.security.WithMockedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeatureFlagControllerTestIT {

    private String token;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() throws Exception {
        String user = """
                {"firstName": "Vasia","lastName": "Pup","userName": "vasia","password": "pass","role": "ADMIN"}
                """;
        String loginRequest = """
                {"userName": "vasia","password": "pass"}
                """;
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(user))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));

        ResultActions result = mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(loginRequest))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));
        String responseContent = result.andReturn().getResponse().getContentAsString();
        AuthenticationResponse authenticationResponse = mapper.readValue(responseContent, AuthenticationResponse.class);
        token = authenticationResponse.getAuthenticationToken();
    }

    @Test
    @WithMockedUser
    void getAllEnabledFeatures() throws Exception {
        //given
        String expected = """
                {"globallyEnabled":[{"id":1,"name":"PNLStatments","enabled":true}],"userEnabled":[{"id":1,"name":"PNLStatments","enabled":true}]}
                """;
        String request = """
                {"featureName": "PNLStatments"}
                """;
        String userFeatureRequest = """
                     {"userName": "vasia", "featureName": "PNLStatments"}
                """;

        mockMvc.perform(post("/feature_flags")
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(request))
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()));
        mockMvc.perform(put("/feature_flags/feature/enable")
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(userFeatureRequest))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));

        // when
        ResultActions result = mockMvc.perform(get("/feature_flags/feature/enabled/all"));

        // then
        result.andExpect(status().isOk()).andDo(print()).andExpect(content().json(expected));
    }

    @Test
    @WithMockedUser
    void addNewFeature() throws Exception {
        //given
        String request = "{\"featureName\": \"PNLStatments\"}";

        //then
        mockMvc.perform(post("/feature_flags")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }
}