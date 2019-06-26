package com.kyripay.users;

import com.kyripay.users.dto.Account;
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
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

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
public class AccountsApiTest
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
    public void createUserAccount() throws IOException {
        RestDocumentationFilter document = document("createUserAccount",
                requestPreprocessor,
                pathParameters(parameterWithName("id").description("User id")),
                requestFields(
                        fieldWithPath("bankId").description("Bank id"),
                        fieldWithPath("number").description("Account number"),
                        fieldWithPath("currency").description("Account currency")
                ),
                responseFields(
                        fieldWithPath("id").description("Id of the created account"),
                        fieldWithPath("bankId").description("Bank id"),
                        fieldWithPath("number").description("Account number"),
                        fieldWithPath("currency").description("Account currency")
                )
        );

        User user = TestsHelper.addUser(restTemplate);

        Account createdAccount = given(this.documentationSpec)
                    .port(port)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(IOUtils.toString(this.getClass().getResourceAsStream("/com.kyripay.users.test/account.json")))
                    .filter(document)
                .when()
                    .post("/api/v1/users/{id}/accounts", user.getId())
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(Account.class);

        Account[] accounts = restTemplate.getForEntity("/api/v1/users/" + user.getId() + "/accounts", Account[].class).getBody();
        assertNotNull(accounts);
        assertEquals(2, accounts.length);
    }

    @Test
    public void getUserAccount() {
        RestDocumentationFilter document = document("getUserAccount",
                requestPreprocessor,
                pathParameters(
                            parameterWithName("userId").description("User id"),
                            parameterWithName("accountId").description("Account id")
                        ),
                responseFields(
                        fieldWithPath("id").description("Id of the created account"),
                        fieldWithPath("bankId").description("Bank id"),
                        fieldWithPath("number").description("Account number"),
                        fieldWithPath("currency").description("Account currency")
                )
        );

        User user = TestsHelper.addUser(restTemplate);

        Account account = given(this.documentationSpec)
                    .port(port)
                    .filter(document)
                .when()
                    .get("/api/v1/users/{userId}/accounts/{accountId}",
                        user.getId(), user.getAccounts().iterator().next().getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(Account.class);

        assertTrue(user.getAccounts().contains(account));
    }

    @Test
    public void getUserAccounts() {
        RestDocumentationFilter document = document("getUserAccounts",
                requestPreprocessor,
                pathParameters(parameterWithName("id").description("User id")),
                responseFields(
                        fieldWithPath("[].id").description("Id of the created account"),
                        fieldWithPath("[].bankId").description("Bank id"),
                        fieldWithPath("[].number").description("Account number"),
                        fieldWithPath("[].currency").description("Account currency")
                )
        );

        User user = TestsHelper.addUser(restTemplate);

        Account[] accounts = given(this.documentationSpec)
                    .port(port)
                    .filter(document)
                .when()
                    .get("/api/v1/users/{id}/accounts", user.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(Account[].class);

        assertNotNull(accounts);
        assertEquals(1, accounts.length);
    }

    @Test
    public void updateUserAccount() {
        RestDocumentationFilter document = document("updateUserAccount",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("userId").description("User id"),
                        parameterWithName("accountId").description("Account id")
                ),
                requestFields(
                        fieldWithPath("id").description("Id of the created account"),
                        fieldWithPath("bankId").description("Bank id"),
                        fieldWithPath("number").description("Account number"),
                        fieldWithPath("currency").description("Account currency")
                ),
                responseFields(
                        fieldWithPath("id").description("Id of the created account"),
                        fieldWithPath("bankId").description("Bank id"),
                        fieldWithPath("number").description("Account number"),
                        fieldWithPath("currency").description("Account currency")
                )
        );

        User user = TestsHelper.addUser(restTemplate);

        String newNumber = "updated";
        Account updatedAccount = user.getAccounts().iterator().next();
        updatedAccount.setNumber(newNumber);

        Account account = given(this.documentationSpec)
                    .port(port)
                    .filter(document)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(updatedAccount)
                .when()
                    .put("/api/v1/users/{userId}/accounts/{accountId}",
                            user.getId(), updatedAccount.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(Account.class);

        assertNotNull(newNumber, account.getNumber());
    }

    @Test
    public void deleteUserAccount() {
        RestDocumentationFilter documentationFilter = document("deleteUserAccount",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("userId").description("User id"),
                        parameterWithName("accountId").description("Account id")
                ));

        User user = TestsHelper.addUser(restTemplate);
        Account account = user.getAccounts().iterator().next();

        given(this.documentationSpec)
            .port(port)
            .filter(documentationFilter)
        .when()
            .delete("/api/v1/users/{userId}/accounts/{accountId}",
                    user.getId(), account.getId())
        .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);

        Account[] accounts = restTemplate.getForEntity(String.format("/api/v1/users/%s/accounts",
                user.getId()), Account[].class).getBody();
        assertNotNull(accounts);
        assertTrue(Arrays.stream(accounts).noneMatch(account::equals));
    }

    @Test
    public void userNotFound() {
        given()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get("/api/v1/users/8822e1f8-8053-40ee-8b73-bc7e6785a371/accounts")
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
                .get("/api/v1/users/" + user.getId() + "/accounts/8822e1f8-8053-40ee-8b73-bc7e6785a371")
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
                .post("/api/v1/users/" + user.getId() + "/accounts")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
