package com.kyripay.users;

import com.kyripay.users.dto.Recipient;
import com.kyripay.users.dto.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RecipientsApiTest
{
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private RequestSpecification documentationSpec;

    private final OperationRequestPreprocessor requestPreprocessor = preprocessRequest(modifyUris()
            .scheme("http")
            .host("userservice")
            .removePort());

    @Before
    public void setup() {
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void createRecipient() throws IOException {
        RestDocumentationFilter documentationFilter = document("createRecipient",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("userId").description("User id")
                ),
                requestFields(
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("bankName").description("Bank name"),
                        fieldWithPath("bankAddress").description("Address of the bank"),
                        fieldWithPath("bankUrn").description("URN of the bank"),
                        fieldWithPath("accountNumber").description("Account number")
                ),
                responseFields(
                        fieldWithPath("id").description("Recipient id"),
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("bankName").description("Bank name"),
                        fieldWithPath("bankAddress").description("Address of the bank"),
                        fieldWithPath("bankUrn").description("URN of the bank"),
                        fieldWithPath("accountNumber").description("Account number")
                )
        );

        User user = TestsHelper.addUser(restTemplate);

        Recipient recipient = given(this.documentationSpec)
                    .port(port)
                    .filter(documentationFilter)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(IOUtils.toString(this.getClass().getResourceAsStream("/com.kyripay.users.test/recipient.json")))
                .when()
                    .post("/api/v1/users/{userId}/recipients", user.getId())
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                    .body()
                    .as(Recipient.class);

        assertNotNull(recipient);
        Recipient[] recipients = restTemplate.getForEntity(
                String.format("/api/v1/users/%s/recipients", user.getId()), Recipient[].class).getBody();
        assertNotNull(recipients);
        assertEquals(2, recipients.length);
    }

    @Test
    public void getUserRecipient() {
        RestDocumentationFilter documentationFilter = document("getUserRecipient",
                    requestPreprocessor,
                pathParameters(
                        parameterWithName("userId").description("User id"),
                        parameterWithName("recipientId").description("Recipient id")
                ),
                responseFields(
                        fieldWithPath("id").description("Recipient id"),
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("bankName").description("Bank name"),
                        fieldWithPath("bankAddress").description("Address of the bank"),
                        fieldWithPath("bankUrn").description("URN of the bank"),
                        fieldWithPath("accountNumber").description("Account number")
                ));

        User user = TestsHelper.addUser(restTemplate);

        Recipient recipient = given(this.documentationSpec)
                    .port(port)
                    .filter(documentationFilter)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                    .get("/api/v1/users/{userId}/recipients/{recipientId}",
                        user.getId(), user.getRecipients().iterator().next().getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .body()
                    .as(Recipient.class);

        assertEquals(user.getRecipients().iterator().next(), recipient);
    }

    @Test
    public void getUserRecipients() {
        RestDocumentationFilter documentationFilter = document("getUserRecipients",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("userId").description("User id")
                ),
                responseFields(
                        fieldWithPath("[].id").description("Recipient id"),
                        fieldWithPath("[].firstName").description("First name"),
                        fieldWithPath("[].lastName").description("Last name"),
                        fieldWithPath("[].bankName").description("Bank name"),
                        fieldWithPath("[].bankAddress").description("Address of the bank"),
                        fieldWithPath("[].bankUrn").description("URN of the bank"),
                        fieldWithPath("[].accountNumber").description("Account number")
                ));

        User user = TestsHelper.addUser(restTemplate);

        Recipient[] recipients = given(this.documentationSpec)
                    .port(port)
                    .filter(documentationFilter)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                    .get("/api/v1/users/{userId}/recipients",user.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .body()
                    .as(Recipient[].class);

        assertEquals(1, recipients.length);
    }

    @Test
    public void updateUserRecipient() {
        RestDocumentationFilter documentationFilter = document("updateUserRecipient",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("userId").description("User id"),
                        parameterWithName("recipientId").description("Recipient id")
                ),
                requestFields(
                        fieldWithPath("id").description("Recipient id"),
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("bankName").description("Bank name"),
                        fieldWithPath("bankAddress").description("Address of the bank"),
                        fieldWithPath("bankUrn").description("URN of the bank"),
                        fieldWithPath("accountNumber").description("Account number")
                ));

        User user = TestsHelper.addUser(restTemplate);
        Recipient recipient = user.getRecipients().iterator().next();
        String updatedFirstName = "updated";
        recipient.setFirstName(updatedFirstName);

        given(this.documentationSpec)
                    .port(port)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .filter(documentationFilter)
                    .body(recipient)
                .when()
                    .put("/api/v1/users/{userId}/recipients/{recipientId}",
                        user.getId(), recipient.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);

        Recipient updatedRecipient = restTemplate.getForEntity(
                String.format("/api/v1/users/%s/recipients/%s", user.getId(), recipient.getId()), Recipient.class).getBody();

        assertNotNull(updatedRecipient);
        assertEquals(recipient, updatedRecipient);
    }

    @Test
    public void deleteUserRecipient() {
        RestDocumentationFilter documentationFilter = document("deleteUserRecipient",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("userId").description("User id"),
                        parameterWithName("recipientId").description("Recipient id")
                ));

        User user = TestsHelper.addUser(restTemplate);

        given(this.documentationSpec)
                    .port(port)
                    .filter(documentationFilter)
                .when()
                    .delete("/api/v1/users/{userId}/recipients/{recipientId}",
                        user.getId(), user.getRecipients().iterator().next().getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);

        Recipient[] recipients = restTemplate.getForEntity(
                String.format("/api/v1/users/%s/recipients", user.getId()), Recipient[].class).getBody();
        assertNotNull(recipients);
        assertEquals(0, recipients.length);
    }

    @Test
    public void userNotFound() {
        given()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .when()
            .get("/api/v1/users/8822e1f8-8053-40ee-8b73-bc7e6785a371/recipients")
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void notFound() {
        User user = TestsHelper.addUser(restTemplate);

        given()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .when()
            .get("/api/v1/users/" + user.getId() + "/recipients/8822e1f8-8053-40ee-8b73-bc7e6785a371")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void badRequest() {
        User user = TestsHelper.addUser(restTemplate);

        given()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body("{}")
        .when()
            .post("/api/v1/users")
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
