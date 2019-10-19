package com.kyripay.notification.contract;

import com.kyripay.notification.api.NotificationController;
import com.kyripay.notification.domain.vo.Status;
import com.kyripay.notification.dto.EmailNotificationRequest;
import com.kyripay.notification.dto.NotificationResponse;
import com.kyripay.notification.service.EmailServiceImpl;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMessageVerifier
@ActiveProfiles("test")
public abstract class ContractTestBase {

    @Autowired
    private NotificationController controller;

    @MockBean
    private EmailServiceImpl service;

    @Before
    public void setup() {
        when(service.sendEmail(any(EmailNotificationRequest.class))).thenReturn(new NotificationResponse(
                UUID.fromString("123e4567-e89b-12d3-a456-426655440000"), Status.SENT));
        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.standaloneSetup(controller));
    }

}
