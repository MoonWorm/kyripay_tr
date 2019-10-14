package com.kyripay.notification.integration;

import com.kyripay.notification.api.NotificationController;
import com.kyripay.notification.domain.vo.Status;
import com.kyripay.notification.dto.EmailNotificationRequest;
import com.kyripay.notification.dto.NotificationResponse;
import com.kyripay.notification.service.EmailServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NotificationController.class)
@ActiveProfiles("test")
public class NotificationRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailServiceImpl emailService;

    @Test
    public void emailIsSentToUser() throws Exception {
        EmailNotificationRequest notificationRequest = new EmailNotificationRequest("vasia@pupkin.com",
                "registration_conf_subj", "registration_conf_body");
        Map<String, Object> params = new HashMap<>();
        params.put("firstName", "Vasia");
        params.put("lastName", "Pupkin");
        params.put("honorific", "Mr.");
        notificationRequest.setParameters(params);

        NotificationResponse expectedResponse = new NotificationResponse("abc-abc-abc", Status.SENT);

        when(emailService.sendEmail(notificationRequest)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/emailnotifications")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "  \"to\": \"vasia@pupkin.com\",\n" +
                        "  \"titleTemplateId\": \"registration_conf_subj\",\n" +
                        "  \"bodyTemplateId\": \"registration_conf_body\",\n" +
                        "  \"parameters\": {\n" +
                        "    \"firstName\": \"Vasia\",\n" +
                        "    \"lastName\": \"Pupkin\",\n" +
                        "    \"honorific\": \"Mr.\"\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus().name()));
    }

    @Test
    public void invalidEmailNotificationRequestShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/emailnotifications")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "  \"to\": \"\\n \\t \",\n" +
                        "  \"titleTemplateId\": null,\n" +
                        "  \"bodyTemplateId\": \"registration_conf_body\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void serviceThrowsAnExceptionShouldHandleException() throws Exception {
        EmailNotificationRequest notificationRequest = new EmailNotificationRequest("vasia@pupkin.com",
                "registration_conf_subj", "registration_conf_body");

        when(emailService.sendEmail(notificationRequest)).thenThrow(new MailAuthenticationException(""));

        mockMvc.perform(post("/api/v1/emailnotifications")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "  \"to\": \"vasia@pupkin.com\",\n" +
                        "  \"titleTemplateId\": \"registration_conf_subj\",\n" +
                        "  \"bodyTemplateId\": \"registration_conf_body\"\n" +
                        "}"))
                .andExpect(status().isInternalServerError());
    }

}
