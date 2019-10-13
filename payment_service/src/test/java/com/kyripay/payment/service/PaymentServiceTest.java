package com.kyripay.payment.service;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.SearchCriterias;
import com.kyripay.payment.domain.Trace;
import com.kyripay.payment.domain.port.in.payment.ServiceException;
import com.kyripay.payment.domain.port.in.payment.impl.PaymentValidator;
import com.kyripay.payment.domain.port.in.payment.impl.PaymentsImpl;
import com.kyripay.payment.domain.port.out.payment.Payments;
import com.kyripay.payment.domain.port.out.payment.RepositoryException;
import com.kyripay.payment.domain.port.out.traces.Traces;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import com.kyripay.payment.domain.vo.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    private static final UUID USER_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

    @Mock
    private Payments repository;
    @Mock
    private PaymentValidator validator;
    @Mock
    private Traces traces;
    @InjectMocks
    private PaymentsImpl sut;

    @Test
    public void create_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        Payment paymentToCreate = createPayment1();
        Payment paymentCreated = createPayment1(1L);

        when(repository.create(USER_ID, paymentToCreate)).thenReturn(paymentCreated);
        ArgumentCaptor<Trace> traceCapture = ArgumentCaptor.forClass(Trace.class);
        doNothing().when(traces).createTrace(traceCapture.capture());

        // when
        Payment actualResult = sut.create(USER_ID, paymentToCreate);

        // then
        assertThat(actualResult).isNotNull().isEqualTo(paymentCreated);
        Trace trace = traceCapture.getValue();
        assertThat(trace).isNotNull().hasFieldOrPropertyWithValue("paymentId", paymentCreated.getId());
        assertThat(trace.getHeaders()).isNotNull().containsEntry("userId", USER_ID.toString());
    }

    @Test(expected = ServiceException.class)
    public void create_setupFailureScenario_checkExceptionIsThrown() {
        // given
        Payment paymentToCreate = createPayment1();

        when(repository.create(USER_ID, paymentToCreate)).thenThrow(new NullPointerException());

        // when
        sut.create(USER_ID, paymentToCreate);

        // then assert expected exception
    }

    @Test
    public void readAll_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        int limit = 3;
        int offset = 0;
        Payment payment1 = createPayment1();
        Payment payment2 = createPayment2();
        when(repository.readAll(USER_ID, limit, offset)).thenReturn(asList(payment1, payment2));

        // when
        List<Payment> actualResult = sut.readAll(USER_ID, limit, offset);

        // then
        assertThat(actualResult).isNotNull().doesNotContainNull();
        assertThat(actualResult.get(0)).isEqualTo(payment1);
        assertThat(actualResult.get(1)).isEqualTo(payment2);
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
    public void search_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        int limit = 2;
        int offset = 0;
        Payment payment1 = createPayment1();
        SearchCriterias sc = new SearchCriterias();
        sc.setStatus(Status.CREATED);
        when(repository.search(sc, limit, offset)).thenReturn(asList(payment1));

        // when
        List<Payment> actualResult = sut.search(sc, limit, offset);

        // then
        assertThat(actualResult).isNotNull().doesNotContainNull().size().isEqualTo(1);
        assertThat(actualResult.get(0)).isEqualTo(payment1);
    }

    @Test(expected = ServiceException.class)
    public void search_setupFailureScenario_checkExceptionIsThrown() {
        // given
        int limit = 3;
        int offset = 0;

        SearchCriterias sc = new SearchCriterias();
        sc.setStatus(Status.CREATED);

        when(repository.search(sc, limit, offset)).thenThrow(new RepositoryException(""));

        // when
        sut.search(sc, limit, offset);

        // then assert expected exception
    }

    @Test
    public void readById_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        long paymentId = 1L;
        Payment payment = createPayment1();
        when(repository.readById(USER_ID, paymentId)).thenReturn(payment);

        // when
        Payment actualResult = sut.readById(USER_ID, paymentId);

        // then
        assertThat(actualResult).isNotNull().isEqualTo(payment);
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
        Status actualResult = sut.getStatus(USER_ID, paymentId);

        // then
        assertThat(actualResult).isNotNull().isEqualTo(status);
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
        Status actualResult = sut.updateStatus(USER_ID, paymentId, status);

        // then
        assertThat(actualResult).isNotNull().isEqualTo(status);
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

    private Payment createPayment1() {
        com.kyripay.payment.domain.PaymentRecipientInfo recipientInfo1
                = new com.kyripay.payment.domain.PaymentRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        return new Payment(1L, USER_ID, new Amount(100L, Currency.BYN), 1L,
                "IBAN123", Status.CREATED, recipientInfo1, LocalDateTime.now());
    }

    private Payment createPayment1(long id) {
        com.kyripay.payment.domain.PaymentRecipientInfo recipientInfo1
                = new com.kyripay.payment.domain.PaymentRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        return new Payment(id, USER_ID, new Amount(100L, Currency.BYN), 1L,
                "IBAN123", Status.CREATED, recipientInfo1, LocalDateTime.now());
    }

    private Payment createPayment2() {
        com.kyripay.payment.domain.PaymentRecipientInfo recipientInfo2
                = new com.kyripay.payment.domain.PaymentRecipientInfo("Ivan", "Ivanov",
                "0000/00222/0XXXY", "Super Bank 2 Inc.", "Main str. 1-2", "IBAN432");
        return new Payment(2L, USER_ID, new Amount(200L, Currency.USD), 2L,
                "IBAN234", Status.CREATED, recipientInfo2, LocalDateTime.now());
    }

}
