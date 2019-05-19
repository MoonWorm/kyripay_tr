package com.kyripay.paymentworkflow;

import com.kyripay.paymentworkflow.dto.Account;
import com.kyripay.paymentworkflow.dto.PaymentTransfer;
import com.kyripay.paymentworkflow.dto.Recipient;
import com.kyripay.paymentworkflow.dto.Transaction;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PaymentWorkflowApiTest {

    @Rule
    public final JUnitRestDocumentation junitRestDocumentation = new JUnitRestDocumentation();

    private RequestSpecification documentationSpec;

    private final OperationRequestPreprocessor requestPreprocessor = preprocessRequest(modifyUris()
            .scheme("http")
            .host("userservice")
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
                        fieldWithPath("account").description("Sender account"),
                        fieldWithPath("account.id").description("Account id"),
                        fieldWithPath("account.bankId").description("Bank id of the account"),
                        fieldWithPath("account.number").description("Account number"),
                        fieldWithPath("account.currency").description("Account currency"),
                        fieldWithPath("transactions[]").description("The list of payment transactions"),
                        fieldWithPath("transactions[].currency").description("Transaction currency"),
                        fieldWithPath("transactions[].amount").description("Amount"),
                        fieldWithPath("transactions[].recipient").description("Recipient"),
                        fieldWithPath("transactions[].recipient.id").description("Recipient unique id (UUID)"),
                        fieldWithPath("transactions[].recipient.firstName").description("Recipient's first name"),
                        fieldWithPath("transactions[].recipient.lastName").description("Recipient's last name"),
                        fieldWithPath("transactions[].recipient.bankName").description("Recipient's bank name"),
                        fieldWithPath("transactions[].recipient.bankAddress").description("Recipient's bank address"),
                        fieldWithPath("transactions[].recipient.accountNumber").description("Recipient's account number")
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
        recipient.setLastName("ln");
        Transaction transaction = new Transaction();
        transaction.setAmount(10f);
        transaction.setCurrency("USD");
        transaction.setRecipient(recipient);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        PaymentTransfer paymentTransfer = new PaymentTransfer();
        paymentTransfer.setAccount(account);
        paymentTransfer.setTransactions(transactions);

        given(this.documentationSpec)
            .filter(documentationFilter)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body(paymentTransfer)
        .when()
            .post("/v1/payment-transfers")
        .then()
            .statusCode(SC_CREATED);
    }
}
