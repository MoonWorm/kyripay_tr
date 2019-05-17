package com.kyripay.users;

import com.kyripay.users.dto.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UsersApiTest
{
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
    public void userCreated() {
        RestDocumentationFilter document = document("createUser",
                requestPreprocessor,
                requestFields(
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("address").description("Address"),
                        fieldWithPath("email").description("Email"),
                        fieldWithPath("phoneNumber").description("Phone number"),
                        fieldWithPath("passwordHash").description("md5 of the password"),
                        fieldWithPath("group").description("Group of the use CUSTOMER|PRODUCT"),
                        fieldWithPath("active").description("Is user active")
                ),
                responseFields(
                        fieldWithPath("id").description("Id of created user"),
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("address").description("Address"),
                        fieldWithPath("email").description("Email"),
                        fieldWithPath("phoneNumber").description("Phone number"),
                        fieldWithPath("passwordHash").description("md5 of the password"),
                        fieldWithPath("group").description("Group of the use CUSTOMER|PRODUCT"),
                        fieldWithPath("active").description("Is user active"),
                        fieldWithPath("recipients").description("The list of recipients associated with the user"),
                        fieldWithPath("accounts").description("The list of accounts associated with the user")
                )
        );

        User user = given(this.documentationSpec)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(userSample())
            .filter(document)
            .when()
                .post("/v1/users")
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
        RestDocumentationFilter document = document("getUser",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("id").description("User id")),
                responseFields(
                        fieldWithPath("id").description("Id of created user"),
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("address").description("Address"),
                        fieldWithPath("email").description("Email"),
                        fieldWithPath("phoneNumber").description("Phone number"),
                        fieldWithPath("passwordHash").description("md5 of the password"),
                        fieldWithPath("group").description("Group of the use CUSTOMER|PRODUCT"),
                        fieldWithPath("active").description("Is user active"),
                        fieldWithPath("recipients[]").description("The list of recipients associated with the user"),
                        fieldWithPath("recipients[].id").description("Recipient Id"),
                        fieldWithPath("recipients[].firstName").description("Recipient firstName"),
                        fieldWithPath("recipients[].lastName").description("Recipient LastName"),
                        fieldWithPath("recipients[].bankName").description("Recipient bankName"),
                        fieldWithPath("recipients[].bankAddress").description("Recipient bankAddress"),
                        fieldWithPath("recipients[].accountNumber").description("Recipient accountNumber"),
                        fieldWithPath("accounts[]").description("The list of accounts associated with the user"),
                        fieldWithPath("accounts[].id").description("Account ID"),
                        fieldWithPath("accounts[].number").description("Account number"),
                        fieldWithPath("accounts[].currency").description("Account currency")
                )
        );

        User user = given(this.documentationSpec)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .filter(document)
                .when()
                    .get("/v1/users/{id}", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(User.class);

        assertNotNull(user);
    }

    @Test
    public void getAllUsers() {
        RestDocumentationFilter document = document("getUsers", requestPreprocessor);

        User[] users = given(this.documentationSpec)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .filter(document)
                .when()
                    .get("/v1/users")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(User[].class);

        assertNotNull(users);
    }

    @Test
    public void activateUser() {
        RestDocumentationFilter document = document("activateUser", requestPreprocessor,
                pathParameters(
                        parameterWithName("id").description("User id"))
                );

         given(this.documentationSpec)
            .filter(document)
        .when()
            .post("/v1/users/{id}/activation", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
        .then()
            .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void deactivateUser() {
        RestDocumentationFilter document = document("deactivateUser", requestPreprocessor,
                pathParameters(
                        parameterWithName("id").description("User id"))
        );

        given(this.documentationSpec)
            .filter(document)
        .when()
            .post("/v1/users/{id}/deactivation", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
        .then()
            .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void updateUser() {
        RestDocumentationFilter document = document("updateUser", requestPreprocessor,
                pathParameters(
                        parameterWithName("id").description("User id"))
        );

        User user = given(this.documentationSpec)
                .filter(document)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(userSample())
            .when()
                .patch("/v1/users/{id}", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
            .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(User.class);

        assertNotNull(user);

    }

    // todo add Accounts
    // todo add Recipients



    private String userSample() {
        return "{\n" +
                "        \"firstName\": \"Kyriba\",\n" +
                "        \"lastName\": \"Kyriba\",\n" +
                "        \"address\": \"Kalvariuskaia 42, Minsk\",\n" +
                "        \"email\": \"kyriba@gmail.com\",\n" +
                "        \"phoneNumber\": \"7788\",\n" +
                "        \"passwordHash\": \"blahbla\",\n" +
                "        \"group\": \"CUSTOMER\",\n" +
                "        \"active\": true\n" +
                "    }";
    }

    private String recipientSample() {
        return "{\n" +
                "                \"firstName\": \"Mikalai\",\n" +
                "                \"lastName\": \"Ivanov\",\n" +
                "                \"bankName\": \"prior bank\",\n" +
                "                \"bankAddress\": \"kiev\",\n" +
                "                \"accountNumber\": \"12345\"\n" +
                "            }";
    }

    private String accountSample() {
        return "{\n" +
                "                \"number\": \"11122\",\n" +
                "                \"currency\": \"EUR\"\n" +
                "            }";
    }
}
