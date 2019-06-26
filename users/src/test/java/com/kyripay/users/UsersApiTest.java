package com.kyripay.users;

import com.kyripay.users.dto.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
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

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;
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
public class UsersApiTest
{
    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void createUser() {
        RestDocumentationFilter document = document("createUser",
                requestPreprocessor,
                requestFields(
                        fieldWithPath("userDetails").description("User details"),
                        fieldWithPath("userDetails.firstName").description("First name"),
                        fieldWithPath("userDetails.lastName").description("Last name"),
                        fieldWithPath("userDetails.address").description("Address"),
                        fieldWithPath("userDetails.email").description("Email"),
                        fieldWithPath("userDetails.phoneNumber").description("Phone number"),
                        fieldWithPath("userDetails.secretHash").description("md5 of the password"),
                        fieldWithPath("userDetails.userGroup").description("Group of the use CUSTOMER|PRODUCT"),
                        fieldWithPath("userDetails.active").description("Is user active"),
                        fieldWithPath("recipients[]").description("The list of recipients associated with the user"),
                        fieldWithPath("recipients[].firstName").description("Recipient firstName"),
                        fieldWithPath("recipients[].lastName").description("Recipient LastName"),
                        fieldWithPath("recipients[].bankName").description("Recipient bankName"),
                        fieldWithPath("recipients[].bankAddress").description("Recipient bankAddress"),
                        fieldWithPath("recipients[].bankUrn").description("Recipient bank URN"),
                        fieldWithPath("recipients[].accountNumber").description("Recipient accountNumber"),
                        fieldWithPath("accounts[]").description("The list of accounts associated with the user"),
                        fieldWithPath("accounts[].bankId").description("Bank id"),
                        fieldWithPath("accounts[].number").description("Account number"),
                        fieldWithPath("accounts[].currency").description("Account currency")
                ),
                responseFields(
                        fieldWithPath("id").description("Id of created user"),
                        fieldWithPath("userDetails").description("User details"),
                        fieldWithPath("userDetails.firstName").description("First name"),
                        fieldWithPath("userDetails.lastName").description("Last name"),
                        fieldWithPath("userDetails.address").description("Address"),
                        fieldWithPath("userDetails.email").description("Email"),
                        fieldWithPath("userDetails.phoneNumber").description("Phone number"),
                        fieldWithPath("userDetails.secretHash").description("md5 of the password"),
                        fieldWithPath("userDetails.userGroup").description("Group of the use CUSTOMER|PRODUCT"),
                        fieldWithPath("userDetails.active").description("Is user active"),
                        fieldWithPath("recipients[]").description("The list of recipients associated with the user"),
                        fieldWithPath("recipients[].id").description("Recipient id"),
                        fieldWithPath("recipients[].firstName").description("Recipient firstName"),
                        fieldWithPath("recipients[].lastName").description("Recipient LastName"),
                        fieldWithPath("recipients[].bankName").description("Recipient bankName"),
                        fieldWithPath("recipients[].bankAddress").description("Recipient bankAddress"),
                        fieldWithPath("recipients[].bankUrn").description("Recipient bank URN"),
                        fieldWithPath("recipients[].accountNumber").description("Recipient accountNumber"),
                        fieldWithPath("accounts[]").description("The list of accounts associated with the user"),
                        fieldWithPath("accounts[].id").description("Account id"),
                        fieldWithPath("accounts[].bankId").description("Bank id"),
                        fieldWithPath("accounts[].number").description("Account number"),
                        fieldWithPath("accounts[].currency").description("Account currency")
                )
        );

        User user = given(this.documentationSpec)
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(TestsHelper.userCreationBodySample())
            .filter(document)
            .when()
                .post("/api/v1/users")
            .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .extract()
                .body()
                .as(User.class);

        assertNotNull(user);
    }

    @Test
    public void getUser() {
        User user = TestsHelper.addUser(restTemplate);
        assertNotNull(user);

        RestDocumentationFilter document = document("getUser",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("id").description("User id")),
                responseFields(
                        fieldWithPath("id").description("Id of created user"),
                        fieldWithPath("userDetails").description("User details"),
                        fieldWithPath("userDetails.firstName").description("First name"),
                        fieldWithPath("userDetails.lastName").description("Last name"),
                        fieldWithPath("userDetails.address").description("Address"),
                        fieldWithPath("userDetails.email").description("Email"),
                        fieldWithPath("userDetails.phoneNumber").description("Phone number"),
                        fieldWithPath("userDetails.secretHash").description("md5 of the password"),
                        fieldWithPath("userDetails.userGroup").description("Group of the use CUSTOMER|PRODUCT"),
                        fieldWithPath("userDetails.active").description("Is user active"),
                        fieldWithPath("recipients[]").description("The list of recipients associated with the user"),
                        fieldWithPath("recipients[].id").description("Recipient id"),
                        fieldWithPath("recipients[].firstName").description("Recipient firstName"),
                        fieldWithPath("recipients[].lastName").description("Recipient LastName"),
                        fieldWithPath("recipients[].bankName").description("Recipient bankName"),
                        fieldWithPath("recipients[].bankAddress").description("Recipient bankAddress"),
                        fieldWithPath("recipients[].bankUrn").description("Recipient bank URN"),
                        fieldWithPath("recipients[].accountNumber").description("Recipient accountNumber"),
                        fieldWithPath("accounts[]").description("The list of accounts associated with the user"),
                        fieldWithPath("accounts[].id").description("Account id"),
                        fieldWithPath("accounts[].bankId").description("Bank id"),
                        fieldWithPath("accounts[].number").description("Account number"),
                        fieldWithPath("accounts[].currency").description("Account currency")
                )
        );

        User requestedUser = given(this.documentationSpec)
                        .port(port)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .filter(document)
                .when()
                    .get("/api/v1/users/{id}", user.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(User.class);

        assertEquals(user, requestedUser);
    }

    @Test
    public void getAllUsers() {
        TestsHelper.addUser(restTemplate);
        TestsHelper.addUser(restTemplate);

        RestDocumentationFilter document = document("getUsers", requestPreprocessor);

        User[] users = given(this.documentationSpec)
                    .port(port)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .filter(document)
                .when()
                    .get("/api/v1/users")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(User[].class);

        assertEquals(2, users.length);
    }

    @Test
    public void deleteUser() {
        User user = TestsHelper.addUser(restTemplate);
        assertNotNull(user);

        RestDocumentationFilter document = document("getUsers", requestPreprocessor);

        given(this.documentationSpec)
                    .port(port)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .filter(document)
                .when()
                    .delete("/api/v1/users/" + user.getId())
                .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);

        assertEquals(HttpStatus.SC_NOT_FOUND, restTemplate.getForEntity("/api/v1/users/" + user.getId(), User.class).getStatusCode().value());
    }

    @Test
    public void notFound() {
        given()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .when()
            .get("/api/v1/users/8822e1f8-8053-40ee-8b73-bc7e6785a371")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void badRequest() {
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
