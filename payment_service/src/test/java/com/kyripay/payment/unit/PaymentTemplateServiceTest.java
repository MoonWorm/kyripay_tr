package com.kyripay.payment.unit;

import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.port.in.payment.ServiceException;
import com.kyripay.payment.domain.port.in.payment.impl.PaymentTemplateValidator;
import com.kyripay.payment.domain.port.in.payment.impl.PaymentTemplatesImpl;
import com.kyripay.payment.domain.port.out.payment.PaymentTemplates;
import com.kyripay.payment.domain.port.out.payment.RepositoryException;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentTemplateServiceTest {

    private static final UUID USER_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

    @Mock
    private PaymentTemplates repository;
    @Mock
    private PaymentTemplateValidator validator;
    @InjectMocks
    private PaymentTemplatesImpl sut;

    @Test
    public void create_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        PaymentTemplate paymentTemplateToCreate = createPaymentTemplate1();
        PaymentTemplate paymentTemplateCreated = createPaymentTemplate1(1L);

        when(repository.create(USER_ID, paymentTemplateToCreate)).thenReturn(paymentTemplateCreated);

        // when
        PaymentTemplate actualResult = sut.create(USER_ID, paymentTemplateToCreate);

        // then
        assertThat(actualResult).isNotNull().isEqualTo(paymentTemplateCreated);
    }

    @Test(expected = ServiceException.class)
    public void create_setupFailureScenario_checkExceptionIsThrown() {
        // given
        PaymentTemplate paymentTemplateToCreate = createPaymentTemplate1();

        when(repository.create(USER_ID, paymentTemplateToCreate)).thenThrow(new NullPointerException());

        // when
        sut.create(USER_ID, paymentTemplateToCreate);

        // then assert expected exception
    }

    @Test
    public void readAll_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        int limit = 3;
        int offset = 0;
        PaymentTemplate paymentTemplate1 = createPaymentTemplate1();
        PaymentTemplate paymentTemplate2 = createPaymentTemplate2();
        when(repository.readAll(USER_ID, limit, offset)).thenReturn(asList(paymentTemplate1, paymentTemplate2));

        // when
        List<PaymentTemplate> actualResult = sut.readAll(USER_ID, limit, offset);

        // then
        assertThat(actualResult).isNotNull().doesNotContainNull();
        assertThat(actualResult.get(0)).isEqualTo(paymentTemplate1);
        assertThat(actualResult.get(1)).isEqualTo(paymentTemplate2);
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
        long id = 1L;
        PaymentTemplate payment = createPaymentTemplate1();
        when(repository.readById(USER_ID, id)).thenReturn(payment);

        // when
        PaymentTemplate actualResult = sut.readById(USER_ID, id);

        // then
        assertThat(actualResult).isNotNull().isEqualTo(payment);
    }

    @Test(expected = ServiceException.class)
    public void readById_setupFailureScenario_checkExceptionIsThrown() {
        // given
        long id = 1L;

        when(repository.readById(USER_ID, id)).thenThrow(new RepositoryException(""));

        // when
        sut.readById(USER_ID, id);

        // then assert expected exception
    }

    @Test
    public void update_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        long id = 1L;

        PaymentTemplate paymentTemplate = createPaymentTemplate1(id);
        PaymentTemplate paymentTemplateUpdated = createPaymentTemplate1(id);

        when(repository.update(USER_ID, id, paymentTemplate)).thenReturn(paymentTemplateUpdated);

        // when
        PaymentTemplate actualResult = sut.update(USER_ID, id, paymentTemplate);

        // then
        assertThat(actualResult).isNotNull().isEqualTo(paymentTemplateUpdated);
    }

    @Test(expected = ServiceException.class)
    public void update_setupFailureScenario_checkExceptionIsThrown() {
        // given
        long id = 1L;
        PaymentTemplate paymentTemplate = createPaymentTemplate1(id);

        when(repository.update(USER_ID, id, paymentTemplate)).thenThrow(new NullPointerException());

        // when
        sut.update(USER_ID, id, paymentTemplate);

        // then assert expected exception
    }

    @Test
    public void delete_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        long id = 1L;

        // when
        sut.delete(USER_ID, id);

        // then
        verify(repository).delete(USER_ID, id);
    }

    @Test(expected = ServiceException.class)
    public void delete_setupFailureScenario_checkExceptionIsThrown() {
        // given
        long id = 1L;

        doThrow(new RepositoryException("")).when(repository).delete(USER_ID, id);

        // when
        sut.delete(USER_ID, id);

        // then assert expected exception
    }

    private PaymentTemplate createPaymentTemplate1() {
        com.kyripay.payment.domain.PaymentTemplateRecipientInfo recipientInfo1
                = new com.kyripay.payment.domain.PaymentTemplateRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        return new PaymentTemplate(null, "Template 1", new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo1, LocalDateTime.now());
    }

    private PaymentTemplate createPaymentTemplate1(long id) {
        com.kyripay.payment.domain.PaymentTemplateRecipientInfo recipientInfo1
                = new com.kyripay.payment.domain.PaymentTemplateRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        return new PaymentTemplate(id, "Template 1", new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo1, LocalDateTime.now());
    }

    private PaymentTemplate createPaymentTemplate2() {
        com.kyripay.payment.domain.PaymentTemplateRecipientInfo recipientInfo2
                = new com.kyripay.payment.domain.PaymentTemplateRecipientInfo("Ivan", "Ivanov",
                "0000/00222/0XXXY", "Super Bank 2 Inc.", "Main str. 1-2", "IBAN432");
        return new PaymentTemplate(2L, "Template 2", new Amount(200L, Currency.USD), 2L,
                "IBAN234", recipientInfo2, LocalDateTime.now());
    }


}
