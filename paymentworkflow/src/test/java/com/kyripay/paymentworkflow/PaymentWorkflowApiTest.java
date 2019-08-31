package com.kyripay.paymentworkflow;

import com.kyripay.paymentworkflow.stream.ConverterStreams;
import com.kyripay.paymentworkflow.stream.PaymentStreams;
import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentWorkflowApiTest {

    @LocalServerPort
    int port;

    @Autowired
    MessageCollector messageCollector;

    @Autowired
    PaymentStreams paymentStreams;

    @Autowired
    ConverterStreams converterStreams;

    @Test
    public void processPayment() {
        Payment payment = new Payment(1, UUID.randomUUID(), Payment.Status.CREATED, null, 111);
        paymentStreams.process().send(MessageBuilder.withPayload(payment).build());
        Object payload = messageCollector.forChannel(converterStreams.convertPayment()).poll().getPayload().toString();
    }


//    @Rule
//    public final JUnitRestDocumentation junitRestDocumentation = new JUnitRestDocumentation();
//
//    private RequestSpecification documentationSpec;
//
//    private final OperationRequestPreprocessor requestPreprocessor = preprocessRequest(modifyUris()
//            .scheme("http")
//            .host("payment-workflow")
//            .removePort());
//
//    @Before
//    public void setup() {
//        this.documentationSpec = new RequestSpecBuilder()
//                .addFilter(documentationConfiguration(junitRestDocumentation))
//                .build();
//    }
//
//    @Test
//    public void paymentTransfer() {
//        RestDocumentationFilter documentationFilter = document("paymentTransfer",
//                requestPreprocessor,
//                requestFields(
//                        fieldWithPath("id").description("Unique payment transfer id (UUID)"),
//                        fieldWithPath("payment").description("Payment that should be send to the bank"),
//                        fieldWithPath("payment.id").description("Payment id"),
//                        fieldWithPath("payment.account").description("Sender account"),
//                        fieldWithPath("payment.account.id").description("Account id"),
//                        fieldWithPath("payment.account.bankId").description("Bank id of the account"),
//                        fieldWithPath("payment.account.number").description("Account number"),
//                        fieldWithPath("payment.account.currency").description("Account currency"),
//                        fieldWithPath("payment.transactions[]").description("The list of payment transactions"),
//                        fieldWithPath("payment.transactions[].currency").description("Transaction currency"),
//                        fieldWithPath("payment.transactions[].amount").description("Amount"),
//                        fieldWithPath("payment.transactions[].recipient").description("Recipient"),
//                        fieldWithPath("payment.transactions[].recipient.id").description("Recipient unique id (UUID)"),
//                        fieldWithPath("payment.transactions[].recipient.firstName").description("Recipient's first name"),
//                        fieldWithPath("payment.transactions[].recipient.lastName").description("Recipient's last name"),
//                        fieldWithPath("payment.transactions[].recipient.bankName").description("Recipient's bank name"),
//                        fieldWithPath("payment.transactions[].recipient.bankAddress").description("Recipient's bank address"),
//                        fieldWithPath("payment.transactions[].recipient.bankUrn").description("Recipient's bank URN"),
//                        fieldWithPath("payment.transactions[].recipient.accountNumber").description("Recipient's account number")
//                )
//        );
//
//        Account account = new Account();
//        account.setId(UUID.randomUUID());
//        account.setBankId(UUID.randomUUID());
//        account.setCurrency("EUR");
//        account.setNumber("123");
//        Recipient recipient = new Recipient();
//        recipient.setId(UUID.randomUUID());
//        recipient.setAccountNumber("123");
//        recipient.setBankName("prior");
//        recipient.setBankAddress("minsk");
//        recipient.setFirstName("fn");
//        recipient.setBankUrn("0000/00222/0XXXX");
//        recipient.setLastName("ln");
//        Transaction transaction = new Transaction();
//        transaction.setAmount(10f);
//        transaction.setCurrency("USD");
//        transaction.setRecipient(recipient);
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction);
//        Payment payment = new Payment();
//        payment.setId(UUID.randomUUID());
//        payment.setAccount(account);
//        payment.setTransactions(transactions);
//
//        PaymentTransfer paymentTransfer = new PaymentTransfer();
//        paymentTransfer.setPayment(payment);
//
//        given(this.documentationSpec)
//            .port(port)
//            .filter(documentationFilter)
//            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//            .body(paymentTransfer)
//        .when()
//            .post("/api/v1/payment-transfers")
//        .then()
//            .statusCode(SC_OK);
//    }
}
