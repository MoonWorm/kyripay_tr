package com.kyripay.users;

import com.kyripay.users.dto.User;
import com.kyripay.users.dto.UserDetails;
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
public class UserDetailsApiTest
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
    public void getUserDetails() {
        RestDocumentationFilter document = document("getUserDetails",
                requestPreprocessor,
                pathParameters(
                    parameterWithName("id").description("User id")),
                responseFields(
                    fieldWithPath("firstName").description("First name"),
                    fieldWithPath("lastName").description("Last name"),
                    fieldWithPath("address").description("Address"),
                    fieldWithPath("email").description("Email"),
                    fieldWithPath("phoneNumber").description("Phone number"),
                    fieldWithPath("secretHash").description("md5 of the password"),
                    fieldWithPath("userGroup").description("Group of the use CUSTOMER|PRODUCT"),
                    fieldWithPath("active").description("Is user active")
                )
        );

        User user = TestsHelper.addUser(restTemplate);

        UserDetails userDetails = given(this.documentationSpec)
                    .port(port)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .filter(document)
                .when()
                    .get("/api/v1/users/{id}/details", user.getId())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .extract()
                    .body()
                    .as(UserDetails.class);

        assertNotNull(userDetails);
    }

    @Test
    public void updateUserDetails() {
        RestDocumentationFilter document = document("updateUserDetails",
                requestPreprocessor,
                pathParameters(
                        parameterWithName("id").description("User id")),
                requestFields(
                        fieldWithPath("firstName").description("First name"),
                        fieldWithPath("lastName").description("Last name"),
                        fieldWithPath("address").description("Address"),
                        fieldWithPath("email").description("Email"),
                        fieldWithPath("phoneNumber").description("Phone number"),
                        fieldWithPath("secretHash").description("md5 of the password"),
                        fieldWithPath("userGroup").description("Group of the use CUSTOMER|PRODUCT"),
                        fieldWithPath("active").description("Is user active")
                )
        );

        User user = TestsHelper.addUser(restTemplate);
        String updatedName = "updated";
        user.getUserDetails().setFirstName(updatedName);

        given(this.documentationSpec)
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body(user.getUserDetails())
            .filter(document)
        .when()
            .put("/api/v1/users/{id}/details", user.getId())
        .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);

        UserDetails updatedUserDetails = restTemplate.getForEntity(
                String.format("/api/v1/users/%s/details", user.getId().toString()), UserDetails.class).getBody();

        assertNotNull(updatedUserDetails);
        assertEquals(updatedName, updatedUserDetails.getFirstName());
    }

    @Test
    public void userNotFound() {
        given()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .when()
            .get("/api/v1/users/8822e1f8-8053-40ee-8b73-bc7e6785a371/details")
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void badRequest() {
        User user = TestsHelper.addUser(restTemplate);
        String updatedName = "updated";
        user.getUserDetails().setFirstName(updatedName);

        given()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body("{}")
        .when()
            .put("/api/v1/users/{id}/details", user.getId())
        .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}