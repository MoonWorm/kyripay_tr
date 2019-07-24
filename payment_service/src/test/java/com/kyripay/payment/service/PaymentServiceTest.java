package com.kyripay.payment.service;

import com.kyripay.payment.dao.PaymentRepository;
import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.dto.PaymentDetails;
import com.kyripay.payment.dto.PaymentRecipientInfo;
import com.kyripay.payment.dto.PaymentRequest;
import com.kyripay.payment.dto.PaymentResponse;
import com.kyripay.payment.service.exception.ServiceException;
import com.kyripay.payment.service.impl.PaymentServiceImpl;
import com.kyripay.payment.service.impl.PaymentValidator;
import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    private static final long USER_ID = 1L;

    @Mock
    private PaymentRepository repository;
    @Mock
    private PaymentValidator validator;
    @Mock
    private DozerBeanMapper mapper;
    @InjectMocks
    private PaymentServiceImpl sut;

    @Test
    public void create_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        PaymentRequest request = createPaymentRequest1();
        PaymentDetails pdDto = request.getPaymentDetails();
        PaymentRecipientInfo riDto = pdDto.getRecipientInfo();

        com.kyripay.payment.domain.PaymentRecipientInfo riDomain = new com.kyripay.payment.domain.PaymentRecipientInfo(
                riDto.getFirstName(), riDto.getLastName(), riDto.getBankName(), riDto.getBankAddress(),
                riDto.getAccountNumber());
        Payment pDomain = new Payment(pdDto.getAmount(), pdDto.getBankId(), pdDto.getAccountNumber(), Status.CREATED,
                riDomain, null);
        Payment pDomainCreated = new Payment(1L, pdDto.getAmount(), pdDto.getBankId(), pdDto.getAccountNumber(),
                Status.CREATED, riDomain, LocalDateTime.now());

        PaymentResponse expectedResponse = new PaymentResponse(pDomainCreated.getId(), pDomainCreated.getStatus(), pdDto,
                pDomainCreated.getCreatedOn().toInstant(ZoneOffset.UTC).toEpochMilli());

        when(mapper.map(request, Payment.class)).thenReturn(pDomain);
        when(repository.create(USER_ID, pDomain)).thenReturn(pDomainCreated);
        when(mapper.map(pDomainCreated, PaymentResponse.class)).thenReturn(expectedResponse);

        // when
        PaymentResponse actualResponse = sut.create(USER_ID, request);

        // then
        assertThat(actualResponse).isNotNull().isEqualTo(expectedResponse);
    }

    @Test(expected = ServiceException.class)
    public void create_setupFailureScenario_checkExceptionIsThrown() {
        // given
        PaymentRequest request = createPaymentRequest1();

        when(mapper.map(request, Payment.class)).thenThrow(new NullPointerException());

        // when
        sut.create(USER_ID, request);

        // then assert expected exception
    }

    @Test
    public void readAll_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        int limit = 3;
        int offset = 0;
        Payment payment1 = createPayment1();
        Payment payment2 = createPayment2();
        PaymentResponse paymentResponse1 = createPaymentResponse(payment1);
        PaymentResponse paymentResponse2 = createPaymentResponse(payment2);
        when(repository.readAll(USER_ID, limit, offset)).thenReturn(asList(payment1, payment2));
        when(mapper.map(payment1, PaymentResponse.class)).thenReturn(paymentResponse1);
        when(mapper.map(payment2, PaymentResponse.class)).thenReturn(paymentResponse2);

        // when
        List<PaymentResponse> actualResponse = sut.readAll(USER_ID, limit, offset);

        // then
        assertThat(actualResponse).isNotNull().doesNotContainNull();
        assertThat(actualResponse.get(0)).isEqualTo(paymentResponse1);
        assertThat(actualResponse.get(1)).isEqualTo(paymentResponse2);
    }

    @Test(expected = ServiceException.class)
    public void readAll_setupFailureScenario_checkExceptionIsThrown() {
        // given
        int limit = 3;
        int offset = 0;

        when(repository.readAll(USER_ID, limit, offset)).thenThrow(new RepositoryException(""));

        // when
        sut.readAll(USER_ID, limit, offset);

        // then assert expected exception
    }

    @Test
    public void readById_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        long paymentId = 1L;
        Payment payment = createPayment1();
        PaymentResponse paymentResponse = createPaymentResponse(payment);
        when(repository.readById(USER_ID, paymentId)).thenReturn(payment);
        when(mapper.map(payment, PaymentResponse.class)).thenReturn(paymentResponse);

        // when
        PaymentResponse actualResponse = sut.readById(USER_ID, paymentId);

        // then
        assertThat(actualResponse).isNotNull().isEqualTo(paymentResponse);
    }

    @Test(expected = ServiceException.class)
    public void readById_setupFailureScenario_checkExceptionIsThrown() {
        // given
        long paymentId = 1L;

        when(repository.readById(USER_ID, paymentId)).thenThrow(new RepositoryException(""));

        // when
        sut.readById(USER_ID, paymentId);

        // then assert expected exception
    }

    @Test
    public void getStatus_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        long paymentId = 1L;
        Status status = Status.PROCESSING;
        when(repository.getStatus(USER_ID, paymentId)).thenReturn(status);

        // when
        Status actualResponse = sut.getStatus(USER_ID, paymentId);

        // then
        assertThat(actualResponse).isNotNull().isEqualTo(status);
    }

    @Test(expected = ServiceException.class)
    public void getStatus_setupFailureScenario_checkExceptionIsThrown() {
        // given
        long paymentId = 1L;

        when(repository.getStatus(USER_ID, paymentId)).thenThrow(new RepositoryException(""));

        // when
        sut.getStatus(USER_ID, paymentId);

        // then assert expected exception
    }

    @Test
    public void updateStatus_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        long paymentId = 1L;
        Status status = Status.PROCESSING;
        when(repository.updateStatus(USER_ID, paymentId, status)).thenReturn(status);

        // when
        Status actualResponse = sut.updateStatus(USER_ID, paymentId, status);

        // then
        assertThat(actualResponse).isNotNull().isEqualTo(status);
    }

    @Test(expected = ServiceException.class)
    public void updateStatus_setupFailureScenario_checkExceptionIsThrown() {
        // given
        long paymentId = 1L;
        Status status = Status.PROCESSING;

        when(repository.updateStatus(USER_ID, paymentId, status)).thenThrow(new RepositoryException(""));

        // when
        sut.updateStatus(USER_ID, paymentId, status);

        // then assert expected exception
    }

    private PaymentResponse createPaymentResponse(Payment payment) {
        com.kyripay.payment.domain.PaymentRecipientInfo ri = payment.getRecipientInfo();
        long createdOn = payment.getCreatedOn().toInstant(ZoneOffset.UTC).toEpochMilli();
        return new PaymentResponse(
                payment.getId(),
                payment.getStatus(),
                new PaymentDetails(
                        payment.getAmount(),
                        payment.getBankId(),
                        payment.getAccountNumber(),
                        new PaymentRecipientInfo(
                                ri.getFirstName(),
                                ri.getLastName(),
                                ri.getBankName(),
                                ri.getBankAddress(),
                                ri.getAccountNumber()
                        )
                ),
                createdOn);
    }

    private Payment createPayment1() {
        com.kyripay.payment.domain.PaymentRecipientInfo recipientInfo1
                = new com.kyripay.payment.domain.PaymentRecipientInfo("Vasia", "Pupkin",
                "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        return new Payment(1L, new Amount(100L, Currency.BYN), 1L,
                "IBAN123", Status.CREATED, recipientInfo1, LocalDateTime.now());
    }

    private Payment createPayment2() {
        com.kyripay.payment.domain.PaymentRecipientInfo recipientInfo2
                = new com.kyripay.payment.domain.PaymentRecipientInfo("Ivan", "Ivanov",
                "Super Bank 2 Inc.", "Main str. 1-2", "IBAN432");
        return new Payment(2L, new Amount(200L, Currency.USD), 2L,
                "IBAN234", Status.CREATED, recipientInfo2, LocalDateTime.now());
    }

    private PaymentRequest createPaymentRequest1() {
        PaymentDetails paymentDetails = createPaymentDetails1();
        return new PaymentRequest(paymentDetails);
    }

    private PaymentDetails createPaymentDetails1() {
        PaymentRecipientInfo recipientInfo1 = new PaymentRecipientInfo("Vasia", "Pupkin",
                "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        return new PaymentDetails(new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo1);
    }

}
