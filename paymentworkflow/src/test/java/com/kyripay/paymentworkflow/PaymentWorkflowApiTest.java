package com.kyripay.paymentworkflow;

import com.kyripay.paymentworkflow.dto.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.restdocs.JUnitRestDocumentation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentWorkflowApiTest {

    @LocalServerPort
    int port;

    @Rule
    public final JUnitRestDocumentation junitRestDocumentation = new JUnitRestDocumentation();

    private RequestSpecification documentationSpec;

    private final OperationRequestPreprocessor requestPreprocessor = preprocessRequest(modifyUris()
            .scheme("http")
            .host("payment-workflow")
            .removePort());

    @Before
    public void setup() {
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(junitRestDocumentation))
                .build();
    }

    @Test
    public void paymentTransfer() {
        RestDocumentationFilter documentationFilter = document("paymentTransfer",
                requestPreprocessor,
                requestFields(
                        fieldWithPath("id").description("Unique payment transfer id (UUID)"),
                        fieldWithPath("payment").description("Payment that should be send to the bank"),
                        fieldWithPath("payment.id").description("Payment id"),
                        fieldWithPath("payment.account").description("Sender account"),
                        fieldWithPath("payment.account.id").description("Account id"),
                        fieldWithPath("payment.account.bankId").description("Bank id of the account"),
                        fieldWithPath("payment.account.number").description("Account number"),
                        fieldWithPath("payment.account.currency").description("Account currency"),
                        fieldWithPath("payment.transactions[]").description("The list of payment transactions"),
                        fieldWithPath("payment.transactions[].currency").description("Transaction currency"),
                        fieldWithPath("payment.transactions[].amount").description("Amount"),
                        fieldWithPath("payment.transactions[].recipient").description("Recipient"),
                        fieldWithPath("payment.transactions[].recipient.id").description("Recipient unique id (UUID)"),
                        fieldWithPath("payment.transactions[].recipient.firstName").description("Recipient's first name"),
                        fieldWithPath("payment.transactions[].recipient.lastName").description("Recipient's last name"),
                        fieldWithPath("payment.transactions[].recipient.bankName").description("Recipient's bank name"),
                        fieldWithPath("payment.transactions[].recipient.bankAddress").description("Recipient's bank address"),
                        fieldWithPath("payment.transactions[].recipient.bankUrn").description("Recipient's bank URN"),
                        fieldWithPath("payment.transactions[].recipient.accountNumber").description("Recipient's account number")
                )
        );

        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setBankId(UUID.randomUUID());
        account.setCurrency("EUR");
        account.setNumber("123");
        Recipient recipient = new Recipient();
        recipient.setId(UUID.randomUUID());
        recipient.setAccountNumber("123");
        recipient.setBankName("prior");
        recipient.setBankAddress("minsk");
        recipient.setFirstName("fn");
        recipient.setBankUrn("0000/00222/0XXXX");
        recipient.setLastName("ln");
        Transaction transaction = new Transaction();
        transaction.setAmount(10f);
        transaction.setCurrency("USD");
        transaction.setRecipient(recipient);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setAccount(account);
        payment.setTransactions(transactions);

        PaymentTransfer paymentTransfer = new PaymentTransfer();
        paymentTransfer.setPayment(payment);

        given(this.documentationSpec)
            .port(port)
            .filter(documentationFilter)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body(paymentTransfer)
        .when()
            .post("/api/v1/payment-transfers")
        .then()
            .statusCode(SC_OK);
    }
}
