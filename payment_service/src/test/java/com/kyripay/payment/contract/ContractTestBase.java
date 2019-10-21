package com.kyripay.payment.contract;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.PaymentRecipientInfo;
import com.kyripay.payment.domain.port.in.payment.PaymentService;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.infrastructure.adapter.in.payment.PaymentController;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentDetails;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentRequest;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentResponse;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMessageVerifier
@ActiveProfiles("test")
public abstract class ContractTestBase {

    @Autowired
    private PaymentController controller;
    @MockBean
    private PaymentService service;
    @MockBean
    private DozerBeanMapper mapper;

    @Before
    public void setup() {
        PaymentRequest request = paymentRequest();
        Payment paymentToBeCreated = paymentToBeCreated();
        when(mapper.map(request, Payment.class)).thenReturn(paymentToBeCreated);
        Payment paymentCreated = createdPayment();
        when(service.create(any(UUID.class), any(Payment.class))).thenReturn(paymentCreated);
        PaymentResponse response = paymentResponse();
        when(mapper.map(paymentCreated, PaymentResponse.class)).thenReturn(response);

        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.standaloneSetup(controller));
    }

    private PaymentRequest paymentRequest() {
        PaymentDetails paymentDetails = paymentDetails();
        return new PaymentRequest(paymentDetails);
    }

    private PaymentDetails paymentDetails() {
        com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentRecipientInfo ri
                = new com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentRecipientInfo(
                "Vasia", "Pupkin", "0000/00222/0XXXX", "Fake Bank Inc",
                "Main str. 1-1", "IBAN321");
        return new PaymentDetails(new Amount(500L, Currency.USD), 1L,
                "IBAN123", ri);
    }

    private Payment createdPayment() {
        return createPayment(1L, LocalDateTime.of(2019, 2, 12, 8, 0));
    }

    private Payment paymentToBeCreated() {
        return createPayment(null, null);
    }

    private Payment createPayment(Long id, LocalDateTime createdOn) {
        return new Payment(
                id,
                UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"),
                new Amount(500L, Currency.USD),
                1L,
                "IBAN123",
                Status.CREATED,
                paymentRecipientInfo(),
                createdOn
        );
    }

    private PaymentRecipientInfo paymentRecipientInfo() {
        return new PaymentRecipientInfo(
                "Vasia",
                "Pupkin",
                "0000/00222/0XXXX",
                "Fake Bank Inc",
                "Main str. 1-1",
                "IBAN321"
        );
    }

    private PaymentResponse paymentResponse() {
        com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentRecipientInfo recipientInfo = new com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentRecipientInfo("Vasia",
                "Pupkin", "0000/00222/0XXXX", "Fake Bank Inc", "Main str. 1-1", "IBAN321");
        long createdOn = LocalDateTime.of(2019, 2, 12, 8, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
        PaymentDetails paymentDetails = new PaymentDetails(new Amount(500L, Currency.USD), 1L,
                "IBAN123", recipientInfo);
        return new PaymentResponse(1L, Status.CREATED, paymentDetails, createdOn);
    }


}
