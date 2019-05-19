package com.kyripay.users;

import com.kyripay.users.dto.Account;
import com.kyripay.users.dto.Recipient;
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
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

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
    public void createUser() {
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
                        fieldWithPath("recipients[].id").description("Recipient id"),
                        fieldWithPath("recipients[].firstName").description("Recipient firstName"),
                        fieldWithPath("recipients[].lastName").description("Recipient LastName"),
                        fieldWithPath("recipients[].bankName").description("Recipient bankName"),
                        fieldWithPath("recipients[].bankAddress").description("Recipient bankAddress"),
                        fieldWithPath("recipients[].accountNumber").description("Recipient accountNumber"),
                        fieldWithPath("accounts[]").description("The list of accounts associated with the user"),
                        fieldWithPath("accounts[].id").description("Account id"),
                        fieldWithPath("accounts[].bankId").description("Bank id"),
                        fieldWithPath("accounts[].number").description("Account number"),
                        fieldWithPath("accounts[].currency").description("Account currency")
                )
        );

        User user = given(this.documentationSpec)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .filter(document)
                .when()
                    .get("/api/v1/users/{id}", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
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
                    .get("/api/v1/users")
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
            .post("/api/v1/users/{id}/activation", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
        .then()
            .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void deactivateUser() {
        RestDocumentationFilter document = document("deactivateUser", requestPreprocessor,
                pathParameters(parameterWithName("id").description("User id"))
        );

        given(this.documentationSpec)
            .filter(document)
        .when()
            .post("/api/v1/users/{id}/deactivation", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
        .then()
            .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void updateUser() {
        RestDocumentationFilter document = document("updateUser", requestPreprocessor,
                pathParameters(parameterWithName("id").description("User id"))
        );

        User user = given(this.documentationSpec)
                .filter(document)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(userSample())
            .when()
                .patch("/api/v1/users/{id}", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
            .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(User.class);

        assertNotNull(user);

    }

    @Test
    public void createUserAccount() {
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

        Account account = given(this.documentationSpec)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(accountSample())
                    .filter(document)
                .when()
                    .post("/api/v1/users/{id}/accounts", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(Account.class);

        assertNotNull(account);
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

        Account[] accounts = given(this.documentationSpec)
                    .filter(document)
                .when()
                .get("/api/v1/users/{id}/accounts", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(Account[].class);

        assertNotNull(accounts);
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

        Account accounts = given(this.documentationSpec)
                    .filter(document)
                .when()
                    .get("/api/v1/users/{userId}/accounts/{accountId}",
                        "8822e1f8-8053-40ee-8b73-bc7e6785a371", "1822e1f8-8053-40ee-8b73-bc7e6785a370")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(Account.class);

        assertNotNull(accounts);
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

        Account updatedAccount = new Account();
        updatedAccount.setId(UUID.randomUUID());
        updatedAccount.setCurrency("USD");
        updatedAccount.setNumber("123");

        Account account = given(this.documentationSpec)
                    .filter(document)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(updatedAccount)
                .when()
                    .put("/api/v1/users/{userId}/accounts/{accountId}",
                            "8822e1f8-8053-40ee-8b73-bc7e6785a371", "1822e1f8-8053-40ee-8b73-bc7e6785a370")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(Account.class);

        assertNotNull(account);
    }

    @Test
    public void deleteUserAccount() {
        RestDocumentationFilter documentationFilter = document("deleteUserAccount",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("userId").description("User id"),
                        parameterWithName("accountId").description("Account id")
                ));

        given(this.documentationSpec)
            .filter(documentationFilter)
        .when()
            .delete("/api/v1/users/{userId}/accounts/{accountId}",
                "8822e1f8-8053-40ee-8b73-bc7e6785a371", "1822e1f8-8053-40ee-8b73-bc7e6785a370")
        .then()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createRecipient() {
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
                        fieldWithPath("accountNumber").description("Account number")
                ),
                responseFields(
                        fieldWithPath("id").description("Recipient id"),
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("bankName").description("Bank name"),
                        fieldWithPath("bankAddress").description("Address of the bank"),
                        fieldWithPath("accountNumber").description("Account number")
                )
        );

        Recipient recipient = given(this.documentationSpec)
                    .filter(documentationFilter)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(recipientSample())
                .when()
                    .post("/api/v1/users/{userId}/recipients", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
                .then()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                    .body()
                    .as(Recipient.class);
        assertNotNull(recipient);
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
                        fieldWithPath("accountNumber").description("Account number")
                ));

        Recipient recipient = given(this.documentationSpec)
                    .filter(documentationFilter)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                    .get("/api/v1/users/{userId}/recipients/{recipientId}",
                        "8822e1f8-8053-40ee-8b73-bc7e6785a370", "8822e1f8-8053-40ee-8b73-bc7e6785a371")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .body()
                    .as(Recipient.class);

        assertNotNull(recipient);
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
                        fieldWithPath("[].accountNumber").description("Account number")
                ));

        Recipient[] recipients = given(this.documentationSpec)
                    .filter(documentationFilter)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                    .get("/api/v1/users/{userId}/recipients","8822e1f8-8053-40ee-8b73-bc7e6785a370")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .body()
                    .as(Recipient[].class);

        assertNotNull(recipients);
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
                        fieldWithPath("accountNumber").description("Account number")
                ),
                responseFields(
                        fieldWithPath("id").description("Recipient id"),
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("bankName").description("Bank name"),
                        fieldWithPath("bankAddress").description("Address of the bank"),
                        fieldWithPath("accountNumber").description("Account number")
                ));
        Recipient newRecipient = new Recipient();
        newRecipient.setId(UUID.randomUUID());
        newRecipient.setAccountNumber("123");
        newRecipient.setBankAddress("sd");
        newRecipient.setBankName("bn");
        newRecipient.setFirstName("Ivan");
        newRecipient.setLastName("Ivanov");

        Recipient recipient = given(this.documentationSpec)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .filter(documentationFilter)
                    .body(newRecipient)
                .when()
                    .put("/api/v1/users/{userId}/recipients/{recipientId}",
                        "8822e1f8-8053-40ee-8b73-bc7e6785a370", "5822e1f8-8053-40ee-8b73-bc7e6785a370")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .body()
                    .as(Recipient.class);

        assertNotNull(recipient);
    }

    @Test
    public void deleteUserRecipient() {
        RestDocumentationFilter documentationFilter = document("deleteUserRecipient",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("userId").description("User id"),
                        parameterWithName("recipientId").description("Recipient id")
                ));

        given(this.documentationSpec)
                .filter(documentationFilter)
                .when()
                .delete("/api/v1/users/{userId}/accounts/{recipientId}",
                        "8822e1f8-8053-40ee-8b73-bc7e6785a371", "1822e1f8-8053-40ee-8b73-bc7e6785a370")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }


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
                "                \"bankId\": \"8822e1f8-8053-40ee-8b73-bc7e6785a371\",\n" +
                "                \"number\": \"11122\",\n" +
                "                \"currency\": \"EUR\"\n" +
                "            }";
    }
}
