package com.mettle.rest;

import com.mettle.security.WithMockedUser;
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

    public static final String TOKEN = "Bearer eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJ2YXNpYSIsImlhdCI6MTY2MzA4MDg1MSwiZXhwIjoxNjYzMDg5ODUxfQ.PuUvFDQddNtpUg779Ew0EmenGupG1BksfAZzSElpQgeaCUSbHTPxaTZZAGsjCNimJThy9XPng87kEUKo-UR1okVZGSdeHplJTa-82qXmrpjbI4L6hLL_ikvro_kiW6eIx28KCW4TXOEa4YzzOXKMpIg0_xS313cjfK9j5DQs9vRG_uKKEY7Nx2KEHFeA64_7SWvXWlU8cYOd6CdnDV2YCkO_DuX1668EEwkOygMs8M0pFwFcm3uLTl3BQIBIkmSh6CKcXyqXRb6PJ9M4JPpd8D0qvJ2g0a_YnSppoJPqpNpQqFbCIT2T2I9PtafxLGaiRSMfohaIfkwGyIVAWORtk9W1DjwuftLsp8ce-4n02Db4DYYEXWAm8Iua6h3uA1UIEdnrSeBF8FuU3A6O-h-kV2hdiK4D6LXJzvLonjvtpF1-hm-I3UQiPR9or-boMYX-K3c7FZPTqU-3NQXVWYolsDDxsq6U_ljCJ7xf5vQwwlFCQR3gQ1HDy7a5B3_qVLF--7EwrGg-OaOFxcwgDIz81X9_qqdEvgm5eUXhUNL1Kt15ZxUUBDW2co5RJJo23d7fHQpRsn9_tnP6OqrFnFWiIcylVGAT1_MxvVlUP1y1XAd_G1WmmXsD0FFEbBpb30Z9PJ2qoSw-ZRk-wlMAqxiq3ynvs0TfEqTLhqn7yxgQmPI";
    @Autowired
    private MockMvc mockMvc;

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
        String user = """
                {"firstName": "Vasia","lastName": "Pup","userName": "vasia","password": "pass","role": "ADMIN"}
                """;
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(user))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));
        mockMvc.perform(post("/feature_flags")
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .content(request))
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()));
        mockMvc.perform(put("/feature_flags/feature/enable")
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
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