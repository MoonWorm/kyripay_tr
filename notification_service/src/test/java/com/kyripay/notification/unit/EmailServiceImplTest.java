package com.kyripay.notification.unit;

import com.kyripay.notification.dao.entity.EmailNotificationDocument;
import com.kyripay.notification.dao.repository.EmailNotificationRepository;
import com.kyripay.notification.domain.vo.Status;
import com.kyripay.notification.dto.EmailNotificationRequest;
import com.kyripay.notification.dto.NotificationResponse;
import com.kyripay.notification.exception.ServiceException;
import com.kyripay.notification.service.EmailNotificationValidator;
import com.kyripay.notification.service.EmailServiceImpl;
import com.kyripay.notification.service.TemplateResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static com.kyripay.notification.domain.vo.Status.FAILED;
import static com.kyripay.notification.domain.vo.Status.SENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceImplTest {

    @Mock
    private TemplateResolver templateResolver;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private EmailNotificationRepository repository;
    @Mock
    private EmailNotificationValidator validator;
    @InjectMocks
    private EmailServiceImpl sut;

    @Test
    public void sendEmail_emailIsSentSuccessfully_assertResultResponse() {
        // given
        String to = "vasia@pupkin.com";
        String subjTemplate = "subj_template";
        String bodyTemplate = "body_template";
        EmailNotificationRequest request = new EmailNotificationRequest(to, subjTemplate, bodyTemplate);
        String emailSubject = "Subj";
        when(templateResolver.resolveText(subjTemplate, null)).thenReturn(emailSubject);
        String emailBody = "Body";
        when(templateResolver.resolveText(bodyTemplate, null)).thenReturn(emailBody);

        Status status = SENT;
        EmailNotificationDocument email = new EmailNotificationDocument(to, emailSubject, emailBody, status);
        EmailNotificationDocument emailSaved = new EmailNotificationDocument(email.getTo(), email.getSubject(),
                email.getBody(), status);
        String emailId = "aaa-bbb-ccc";
        emailSaved.setId(emailId);
        when(repository.save(email)).thenReturn(emailSaved);

        // when
        NotificationResponse response = sut.sendEmail(request);

        // then
        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", emailId)
                .hasFieldOrPropertyWithValue("status", status);
    }

    @Test
    public void sendEmail_emailIsNotSent_assertResultResponse() {
        // given
        String to = "vasia@pupkin.com";
        String subjTemplate = "subj_template";
        String bodyTemplate = "body_template";
        EmailNotificationRequest request = new EmailNotificationRequest(to, subjTemplate, bodyTemplate);
        String emailSubject = "Subj";
        when(templateResolver.resolveText(subjTemplate, null)).thenReturn(emailSubject);
        String emailBody = "Body";
        when(templateResolver.resolveText(bodyTemplate, null)).thenReturn(emailBody);

        doThrow(new MailAuthenticationException("Email sending error"))
                .when(mailSender).send(any(SimpleMailMessage.class));
        ArgumentCaptor<EmailNotificationDocument> emailDocumentCaptor =
                ArgumentCaptor.forClass(EmailNotificationDocument.class);
        EmailNotificationDocument emailSaved = new EmailNotificationDocument(to, emailSubject, emailBody, FAILED);
        String emailId = "aaa-bbb-ccc";
        emailSaved.setId(emailId);
        when(repository.save(emailDocumentCaptor.capture())).thenReturn(emailSaved);

        // when
        NotificationResponse response = sut.sendEmail(request);

        // then
        assertThat(emailDocumentCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("status", FAILED);
        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", emailId)
                .hasFieldOrPropertyWithValue("status", emailSaved.getStatus());
    }

    @Test(expected = ServiceException.class)
    public void sendEmail_unexpectedErrorHappened_shouldThrowException() {
        // given
        String to = "vasia@pupkin.com";
        String subjTemplate = "subj_template";
        String bodyTemplate = "body_template";
        EmailNotificationRequest request = new EmailNotificationRequest(to, subjTemplate, bodyTemplate);
        when(templateResolver.resolveText(anyString(), any())).thenThrow(new RuntimeException(""));

        // when
        sut.sendEmail(request);

        // then exception is thrown
    }

}
