package com.kyripay.payment.api;

import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.port.in.payment.impl.PaymentTemplatesImpl;
import com.kyripay.payment.infrastructure.adapter.in.payment.CustomGlobalExceptionHandler;
import com.kyripay.payment.infrastructure.adapter.in.payment.PaymentTemplateController;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.*;
import com.kyripay.payment.domain.port.in.payment.ServiceException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentTemplateController.class)
@ActiveProfiles("test")
public class PaymentTemplateRestControllerIntegrationTest {

    private static final UUID USER_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentTemplatesImpl paymentTemplateService;

    @MockBean
    private DozerBeanMapper mapper;

    @Test
    public void create_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        PaymentTemplateRecipientInfo recipientInfo = new PaymentTemplateRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");

        PaymentTemplateDetails paymentTemplateDetails = new PaymentTemplateDetails(new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo);

        PaymentTemplateRequest request = new PaymentTemplateRequest("Template 1", paymentTemplateDetails);

        PaymentTemplateResponse expectedResponse = new PaymentTemplateResponse(1L, "Template 1", paymentTemplateDetails,
                System.currentTimeMillis());

        PaymentTemplate paymentTemplateToCreate = mock(PaymentTemplate.class);
        when(mapper.map(request, PaymentTemplate.class)).thenReturn(paymentTemplateToCreate);
        PaymentTemplate paymentTemplateCreated = mock(PaymentTemplate.class);
        when(paymentTemplateService.create(USER_ID, paymentTemplateToCreate)).thenReturn(paymentTemplateCreated);
        when(mapper.map(paymentTemplateCreated, PaymentTemplateResponse.class)).thenReturn(expectedResponse);

        ResultActions ra = mockMvc.perform(post("/api/v1/paymenttemplates")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\n" +
                        "  \"name\": \"Template 1\",\n" +
                        "  \"paymentDetails\": {\n" +
                        "    \"amount\": {\n" +
                        "      \"amount\": 100,\n" +
                        "      \"currency\": \"BYN\"\n" +
                        "    },\n" +
                        "    \"bankId\": 1,\n" +
                        "    \"accountNumber\": \"IBAN123\",\n" +
                        "    \"recipientInfo\": {\n" +
                        "      \"firstName\": \"Vasia\",\n" +
                        "      \"lastName\": \"Pupkin\",\n" +
                        "      \"bankUrn\": \"0000/00222/0XXXX\",\n" +
                        "      \"bankName\": \"Super Bank Inc.\",\n" +
                        "      \"bankAddress\": \"Main str. 1-1\",\n" +
                        "      \"accountNumber\": \"IBAN321\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isOk());
        assertPaymentTemplateResponse(ra, expectedResponse);
    }

    @Test
    public void create_sendInvalidRequest_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/paymenttemplates")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\n" +
                        "  \"name\": \"Template 1\",\n" +
                        "  \"paymentDetails\": {\n" +
                        "    \"amount\": {\n" +
                        "      \"amount\": null,\n" +
                        "      \"currency\": \"\"\n" +
                        "    },\n" +
                        "    \"bankId\": null,\n" +
                        "    \"accountNumber\": \"\\n \\t \",\n" +
                        "    \"recipientInfo\": {\n" +
                        "      \"firstName\": \"\",\n" +
                        "      \"lastName\": null,\n" +
                        "      \"bankName\": \"Super Bank Inc.\",\n" +
                        "      \"accountNumber\": \" \"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_sendValidRequestButServiceThrowException_shouldReturnErrorResponse() throws Exception {
        PaymentTemplateRecipientInfo recipientInfo = new PaymentTemplateRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");

        PaymentTemplateDetails paymentTemplateDetails = new PaymentTemplateDetails(new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo);

        PaymentTemplateRequest request = new PaymentTemplateRequest("Template 1", paymentTemplateDetails);

        Throwable e = new ServiceException("");

        CustomGlobalExceptionHandler.ErrorsInfo expectedResponse = new CustomGlobalExceptionHandler.ErrorsInfo(
                new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), asList(ExceptionUtils.getMessage(e))
        );

        PaymentTemplate paymentTemplateToCreate = mock(PaymentTemplate.class);
        when(mapper.map(request, PaymentTemplate.class)).thenReturn(paymentTemplateToCreate);
        when(paymentTemplateService.create(USER_ID, paymentTemplateToCreate)).thenThrow(e);

        mockMvc.perform(post("/api/v1/paymenttemplates")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\n" +
                        "  \"name\": \"Template 1\",\n" +
                        "  \"paymentDetails\": {\n" +
                        "    \"amount\": {\n" +
                        "      \"amount\": 100,\n" +
                        "      \"currency\": \"BYN\"\n" +
                        "    },\n" +
                        "    \"bankId\": 1,\n" +
                        "    \"accountNumber\": \"IBAN123\",\n" +
                        "    \"recipientInfo\": {\n" +
                        "      \"firstName\": \"Vasia\",\n" +
                        "      \"lastName\": \"Pupkin\",\n" +
                        "      \"bankUrn\": \"0000/00222/0XXXX\",\n" +
                        "      \"bankName\": \"Super Bank Inc.\",\n" +
                        "      \"bankAddress\": \"Main str. 1-1\",\n" +
                        "      \"accountNumber\": \"IBAN321\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andExpect(jsonPath("$.errors[0]").value(expectedResponse.getErrors().get(0)));
    }

    @Test
    public void readAll_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        PaymentTemplateRecipientInfo recipientInfo1 = new PaymentTemplateRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        PaymentTemplateDetails paymentDetails1 = new PaymentTemplateDetails(new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo1);

        PaymentTemplateRecipientInfo recipientInfo2 = new PaymentTemplateRecipientInfo("Ivan", "Ivanov",
                "0000/00222/0XXXX", "Super Bank 2 Inc.", "Main str. 1-2", "IBAN432");
        PaymentTemplateDetails paymentDetails2 = new PaymentTemplateDetails(new Amount(200L, Currency.USD), 2L,
                "IBAN234", recipientInfo2);

        PaymentTemplateResponse expectedResponse1 = new PaymentTemplateResponse(1L, "Template 1", paymentDetails1,
                System.currentTimeMillis());

        PaymentTemplateResponse expectedResponse2 = new PaymentTemplateResponse(2L, "Template 2", paymentDetails2,
                System.currentTimeMillis());

        int limit = 2;
        int offset = 0;

        PaymentTemplate paymentTemplate1 = mock(PaymentTemplate.class);
        PaymentTemplate paymentTemplate2 = mock(PaymentTemplate.class);
        when(paymentTemplateService.readAll(USER_ID, limit, offset)).thenReturn(asList(paymentTemplate1, paymentTemplate2));
        when(mapper.map(paymentTemplate1, PaymentTemplateResponse.class)).thenReturn(expectedResponse1);
        when(mapper.map(paymentTemplate2, PaymentTemplateResponse.class)).thenReturn(expectedResponse2);

        ResultActions ra = mockMvc.perform(get("/api/v1/paymenttemplates")
                .param("limit", String.valueOf(limit))
                .param("offset", String.valueOf(offset))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID))
                .andExpect(status().isOk());
        assertPaymentTemplateResponse(ra, expectedResponse1, 0);
        assertPaymentTemplateResponse(ra, expectedResponse2, 1);
    }

    @Test
    public void readAll_sendInvalidRequestWithoutMandatoryUserIdHeaderValue_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/paymenttemplates")
                //.header("userId", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void readById_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        long paymentTemplateId = 1L;
        PaymentTemplateRecipientInfo recipientInfo = new PaymentTemplateRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        PaymentTemplateDetails paymentDetails = new PaymentTemplateDetails(new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo);

        PaymentTemplateResponse expectedResponse = new PaymentTemplateResponse(paymentTemplateId, "Template 1", paymentDetails,
                System.currentTimeMillis());

        PaymentTemplate paymentTemplate = mock(PaymentTemplate.class);
        when(paymentTemplateService.readById(USER_ID, paymentTemplateId)).thenReturn(paymentTemplate);
        when(mapper.map(paymentTemplate, PaymentTemplateResponse.class)).thenReturn(expectedResponse);

        ResultActions ra = mockMvc.perform(get("/api/v1/paymenttemplates/" + paymentTemplateId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID))
                .andExpect(status().isOk());
        assertPaymentTemplateResponse(ra, expectedResponse);
    }

    @Test
    public void readById_sendInvalidRequestWithoutMandatoryUserIdHeaderValue_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/paymenttemplates/1")
                //.header("userId", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        long paymentTemplateId = 1L;
        
        PaymentTemplateRecipientInfo recipientInfo = new PaymentTemplateRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");

        PaymentTemplateDetails paymentTemplateDetails = new PaymentTemplateDetails(new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo);

        PaymentTemplateRequest request = new PaymentTemplateRequest("Template 1", paymentTemplateDetails);

        PaymentTemplateResponse expectedResponse = new PaymentTemplateResponse(paymentTemplateId, "Template 1", paymentTemplateDetails,
                System.currentTimeMillis());

        PaymentTemplate paymentTemplateToUpdate = mock(PaymentTemplate.class);
        when(mapper.map(request, PaymentTemplate.class)).thenReturn(paymentTemplateToUpdate);
        PaymentTemplate paymentTemplateUpdated = mock(PaymentTemplate.class);
        when(paymentTemplateService.update(USER_ID, paymentTemplateId, paymentTemplateToUpdate)).thenReturn(paymentTemplateUpdated);
        when(mapper.map(paymentTemplateUpdated, PaymentTemplateResponse.class)).thenReturn(expectedResponse);

        ResultActions ra = mockMvc.perform(put("/api/v1/paymenttemplates/" + paymentTemplateId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\n" +
                        "  \"name\": \"Template 1\",\n" +
                        "  \"paymentDetails\": {\n" +
                        "    \"amount\": {\n" +
                        "      \"amount\": 100,\n" +
                        "      \"currency\": \"BYN\"\n" +
                        "    },\n" +
                        "    \"bankId\": 1,\n" +
                        "    \"accountNumber\": \"IBAN123\",\n" +
                        "    \"recipientInfo\": {\n" +
                        "      \"firstName\": \"Vasia\",\n" +
                        "      \"lastName\": \"Pupkin\",\n" +
                        "      \"bankUrn\": \"0000/00222/0XXXX\",\n" +
                        "      \"bankName\": \"Super Bank Inc.\",\n" +
                        "      \"bankAddress\": \"Main str. 1-1\",\n" +
                        "      \"accountNumber\": \"IBAN321\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isOk());
        assertPaymentTemplateResponse(ra, expectedResponse);
    }

    @Test
    public void update_sendInvalidRequest_shouldReturn400() throws Exception {
        mockMvc.perform(put("/api/v1/paymenttemplates/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\n" +
                        "  \"name\": \"Template 1\",\n" +
                        "  \"paymentDetails\": {\n" +
                        "    \"amount\": {\n" +
                        "      \"amount\": null,\n" +
                        "      \"currency\": \"\"\n" +
                        "    },\n" +
                        "    \"bankId\": null,\n" +
                        "    \"accountNumber\": \"\\n \\t \",\n" +
                        "    \"recipientInfo\": {\n" +
                        "      \"firstName\": \"\",\n" +
                        "      \"lastName\": null,\n" +
                        "      \"bankName\": \"Super Bank Inc.\",\n" +
                        "      \"accountNumber\": \" \"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        long paymentTemplateId = 1L;
        mockMvc.perform(delete("/api/v1/paymenttemplates/" + paymentTemplateId)
                .header("userId", USER_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_sendInvalidRequestWithWrongPathVarType_shouldReturn400() throws Exception {
        mockMvc.perform(delete("/api/v1/paymenttemplates/abc")
                .header("userId", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    private void assertPaymentTemplateResponse(ResultActions ra, PaymentTemplateResponse expectedResponse) throws Exception {
        PaymentTemplateDetails pd = expectedResponse.getPaymentDetails();
        Amount a = pd.getAmount();
        PaymentTemplateRecipientInfo ri = pd.getRecipientInfo();
        ra.andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.paymentDetails.amount.amount").value(a.getAmount()))
                .andExpect(jsonPath("$.paymentDetails.amount.currency").value(a.getCurrency().name()))
                .andExpect(jsonPath("$.paymentDetails.bankId").value(pd.getBankId()))
                .andExpect(jsonPath("$.paymentDetails.accountNumber").value(pd.getAccountNumber()))
                .andExpect(jsonPath("$.paymentDetails.recipientInfo.firstName").value(ri.getFirstName()))
                .andExpect(jsonPath("$.paymentDetails.recipientInfo.lastName").value(ri.getLastName()))
                .andExpect(jsonPath("$.paymentDetails.recipientInfo.bankUrn").value(ri.getBankUrn()))
                .andExpect(jsonPath("$.paymentDetails.recipientInfo.bankName").value(ri.getBankName()))
                .andExpect(jsonPath("$.paymentDetails.recipientInfo.bankAddress").value(ri.getBankAddress()))
                .andExpect(jsonPath("$.paymentDetails.recipientInfo.accountNumber").value(ri.getAccountNumber()))
                .andExpect(jsonPath("$.createdOn").value(expectedResponse.getCreatedOn()));
    }

    private void assertPaymentTemplateResponse(ResultActions ra, PaymentTemplateResponse expectedResponse, int index) throws Exception {
        PaymentTemplateDetails ptd = expectedResponse.getPaymentDetails();
        Amount a = ptd.getAmount();
        PaymentTemplateRecipientInfo ri = ptd.getRecipientInfo();
        ra.andExpect(jsonPath("$[" + index + "].id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$[" + index + "].name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.amount.amount").value(a.getAmount()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.amount.currency").value(a.getCurrency().name()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.bankId").value(ptd.getBankId()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.accountNumber").value(ptd.getAccountNumber()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.firstName").value(ri.getFirstName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.lastName").value(ri.getLastName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.bankUrn").value(ri.getBankUrn()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.bankName").value(ri.getBankName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.bankAddress").value(ri.getBankAddress()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.accountNumber").value(ri.getAccountNumber()))
                .andExpect(jsonPath("$[" + index + "].createdOn").value(expectedResponse.getCreatedOn()));
    }

}