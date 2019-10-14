package com.kyripay.notification.integration;

import com.kyripay.notification.dao.entity.EmailNotificationDocument;
import com.kyripay.notification.dao.repository.EmailNotificationRepository;
import com.kyripay.notification.domain.vo.Status;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = EmailNotificationRepositoryTest.Initializer.class)
public class EmailNotificationRepositoryTest {

    private static final int MONGODB_PORT = 27017;

    @ClassRule
    public static GenericContainer mongodb = new GenericContainer("mongo:3.6").withExposedPorts(MONGODB_PORT);
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>
    {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.data.mongodb.host=" + mongodb.getContainerIpAddress(),
                    "spring.data.mongodb.port=" + mongodb.getMappedPort(MONGODB_PORT)
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    @Autowired
    private EmailNotificationRepository repository;

    @Test
    public void save_passEmailNotificationDocument_shouldBeSuccessfullyStoredWithGeneratedId() {
        // given
        EmailNotificationDocument document = new EmailNotificationDocument("vasia.pupkin@kpay.com", "test subj", "test body", Status.SENT);

        // when
        EmailNotificationDocument documentSaved = repository.save(document);

        // then
        assertThat(documentSaved)
                .isNotNull()
                .hasFieldOrPropertyWithValue("to", document.getTo())
                .hasFieldOrPropertyWithValue("subject", document.getSubject())
                .hasFieldOrPropertyWithValue("body", document.getBody())
                .hasFieldOrProperty("id");
        assertThat(documentSaved.getId()).isNotBlank();
    }

}