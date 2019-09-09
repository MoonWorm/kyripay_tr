package com.kyripay.payment.service;

import com.kyripay.payment.dao.PaymentTemplateRepository;
import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import com.kyripay.payment.dto.PaymentTemplateDetails;
import com.kyripay.payment.dto.PaymentTemplateRecipientInfo;
import com.kyripay.payment.dto.PaymentTemplateRequest;
import com.kyripay.payment.dto.PaymentTemplateResponse;
import com.kyripay.payment.service.exception.ServiceException;
import com.kyripay.payment.service.impl.PaymentTemplateServiceImpl;
import com.kyripay.payment.service.impl.PaymentTemplateValidator;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentTemplateServiceTest {

    private static final long USER_ID = 1L;

    @Mock
    private PaymentTemplateRepository repository;
    @Mock
    private PaymentTemplateValidator validator;
    @Mock
    private DozerBeanMapper mapper;
    @InjectMocks
    private PaymentTemplateServiceImpl sut;

    @Test
    public void create_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        PaymentTemplateRequest request = createPaymentTemplateRequest1();
        PaymentTemplateDetails pdDto = request.getPaymentDetails();
        PaymentTemplateRecipientInfo riDto = pdDto.getRecipientInfo();

        com.kyripay.payment.domain.PaymentTemplateRecipientInfo riDomain
                = new com.kyripay.payment.domain.PaymentTemplateRecipientInfo(riDto.getFirstName(), riDto.getLastName(),
                riDto.getBankName(), riDto.getBankAddress(), riDto.getAccountNumber());
        PaymentTemplate ptDomain = new PaymentTemplate(null, request.getName(), pdDto.getAmount(), pdDto.getBankId(),
                pdDto.getAccountNumber(), riDomain, null);
        PaymentTemplate ptDomainCreated = new PaymentTemplate(1L, request.getName(), pdDto.getAmount(),
                pdDto.getBankId(), pdDto.getAccountNumber(), riDomain, LocalDateTime.now());

        PaymentTemplateResponse expectedResponse = new PaymentTemplateResponse(ptDomainCreated.getId(), ptDomain.getName(),
                pdDto, ptDomainCreated.getCreatedOn().toInstant(ZoneOffset.UTC).toEpochMilli());

        when(mapper.map(request, PaymentTemplate.class)).thenReturn(ptDomain);
        when(repository.create(USER_ID, ptDomain)).thenReturn(ptDomainCreated);
        when(mapper.map(ptDomainCreated, PaymentTemplateResponse.class)).thenReturn(expectedResponse);

        // when
        PaymentTemplateResponse actualResponse = sut.create(USER_ID, request);

        // then
        assertThat(actualResponse).isNotNull().isEqualTo(expectedResponse);
    }

    @Test(expected = ServiceException.class)
    public void create_setupFailureScenario_checkExceptionIsThrown() {
        // given
        PaymentTemplateRequest request = createPaymentTemplateRequest1();

        when(mapper.map(request, PaymentTemplate.class)).thenThrow(new NullPointerException());

        // when
        sut.create(USER_ID, request);

        // then assert expected exception
    }

    @Test
    public void readAll_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        int limit = 3;
        int offset = 0;
        PaymentTemplate pt1 = createPaymentTemplate1();
        PaymentTemplate pt2 = createPaymentTemplate2();
        PaymentTemplateResponse pr1 = createPaymentResponse(pt1);
        PaymentTemplateResponse pr2 = createPaymentResponse(pt2);
        when(repository.readAll(USER_ID, limit, offset)).thenReturn(asList(pt1, pt2));
        when(mapper.map(pt1, PaymentTemplateResponse.class)).thenReturn(pr1);
        when(mapper.map(pt2, PaymentTemplateResponse.class)).thenReturn(pr2);

        // when
        List<PaymentTemplateResponse> actualResponse = sut.readAll(USER_ID, limit, offset);

        // then
        assertThat(actualResponse).isNotNull().doesNotContainNull();
        assertThat(actualResponse.get(0)).isEqualTo(pr1);
        assertThat(actualResponse.get(1)).isEqualTo(pr2);
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
        PaymentTemplate payment = createPaymentTemplate1();
        PaymentTemplateResponse paymentResponse = createPaymentResponse(payment);
        when(repository.readById(USER_ID, paymentId)).thenReturn(payment);
        when(mapper.map(payment, PaymentTemplateResponse.class)).thenReturn(paymentResponse);

        // when
        PaymentTemplateResponse actualResponse = sut.readById(USER_ID, paymentId);

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
    public void update_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        long ptId = 1L;
        PaymentTemplateRequest request = createPaymentTemplateRequest1();
        PaymentTemplateDetails pdDto = request.getPaymentDetails();
        PaymentTemplateRecipientInfo riDto = pdDto.getRecipientInfo();

        com.kyripay.payment.domain.PaymentTemplateRecipientInfo riDomain
                = new com.kyripay.payment.domain.PaymentTemplateRecipientInfo(riDto.getFirstName(), riDto.getLastName(),
                riDto.getBankName(), riDto.getBankAddress(), riDto.getAccountNumber());
        PaymentTemplate ptDomain = new PaymentTemplate(ptId, request.getName(), pdDto.getAmount(), pdDto.getBankId(),
                pdDto.getAccountNumber(), riDomain, null);
        PaymentTemplate ptDomainUpdated = new PaymentTemplate(ptId, request.getName(), pdDto.getAmount(),
                pdDto.getBankId(), pdDto.getAccountNumber(), riDomain, LocalDateTime.now());

        PaymentTemplateResponse expectedResponse = new PaymentTemplateResponse(ptDomainUpdated.getId(), ptDomain.getName(),
                pdDto, ptDomainUpdated.getCreatedOn().toInstant(ZoneOffset.UTC).toEpochMilli());

        when(mapper.map(request, PaymentTemplate.class)).thenReturn(ptDomain);
        when(repository.update(USER_ID, ptId, ptDomain)).thenReturn(ptDomainUpdated);
        when(mapper.map(ptDomainUpdated, PaymentTemplateResponse.class)).thenReturn(expectedResponse);

        // when
        PaymentTemplateResponse actualResponse = sut.update(USER_ID, ptId, request);

        // then
        assertThat(actualResponse).isNotNull().isEqualTo(expectedResponse);
    }

    @Test(expected = ServiceException.class)
    public void update_setupFailureScenario_checkExceptionIsThrown() {
        // given
        long ptId = 1L;
        PaymentTemplateRequest request = createPaymentTemplateRequest1();

        when(mapper.map(request, PaymentTemplate.class)).thenThrow(new NullPointerException());

        // when
        sut.update(USER_ID, ptId, request);

        // then assert expected exception
    }

    @Test
    public void delete_setupSuccessScenario_checkAllInvocationsAndResult() {
        // given
        long ptId = 1L;

        // when
        sut.delete(USER_ID, ptId);

        // then
        verify(repository).delete(USER_ID, ptId);
    }

    @Test(expected = ServiceException.class)
    public void delete_setupFailureScenario_checkExceptionIsThrown() {
        // given
        long ptId = 1L;

        doThrow(new RepositoryException("")).when(repository).delete(USER_ID, ptId);

        // when
        sut.delete(USER_ID, ptId);

        // then assert expected exception
    }

    private PaymentTemplateResponse createPaymentResponse(PaymentTemplate pt) {
        com.kyripay.payment.domain.PaymentTemplateRecipientInfo ri = pt.getRecipientInfo();
        long createdOn = pt.getCreatedOn().toInstant(ZoneOffset.UTC).toEpochMilli();
        return new PaymentTemplateResponse(
                pt.getId(),
                pt.getName(),
                new PaymentTemplateDetails(
                        pt.getAmount(),
                        pt.getBankId(),
                        pt.getAccountNumber(),
                        new PaymentTemplateRecipientInfo(
                                ri.getFirstName(),
                                ri.getLastName(),
                                ri.getBankName(),
                                ri.getBankAddress(),
                                ri.getAccountNumber()
                        )
                ),
                createdOn);
    }

    private PaymentTemplate createPaymentTemplate1() {
        com.kyripay.payment.domain.PaymentTemplateRecipientInfo recipientInfo1
                = new com.kyripay.payment.domain.PaymentTemplateRecipientInfo("Vasia", "Pupkin",
                "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        return new PaymentTemplate(1L, "Template 1", new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo1, LocalDateTime.now());
    }

    private PaymentTemplate createPaymentTemplate2() {
        com.kyripay.payment.domain.PaymentTemplateRecipientInfo recipientInfo2
                = new com.kyripay.payment.domain.PaymentTemplateRecipientInfo("Ivan", "Ivanov",
                "Super Bank 2 Inc.", "Main str. 1-2", "IBAN432");
        return new PaymentTemplate(2L, "Template 2", new Amount(200L, Currency.USD), 2L,
                "IBAN234", recipientInfo2, LocalDateTime.now());
    }

    private PaymentTemplateRequest createPaymentTemplateRequest1() {
        PaymentTemplateDetails paymentDetails = createPaymentTemplateDetails1();
        return new PaymentTemplateRequest("Template 1", paymentDetails);
    }

    private PaymentTemplateDetails createPaymentTemplateDetails1() {
        PaymentTemplateRecipientInfo recipientInfo1 = new PaymentTemplateRecipientInfo("Vasia", "Pupkin",
                "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        return new PaymentTemplateDetails(new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo1);
    }

}
