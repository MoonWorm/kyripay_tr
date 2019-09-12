package com.kyripay.payment.api;

import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.dto.*;
import com.kyripay.payment.service.exception.ServiceException;
import com.kyripay.payment.service.impl.PaymentServiceImpl;
import org.apache.commons.lang.exception.ExceptionUtils;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentController.class)
@ActiveProfiles("test")
public class PaymentRestControllerIntegrationTest {

    private static final UUID USER_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentServiceImpl paymentService;

    @Test
    public void create_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        PaymentRequest pr = createPaymentRequest1();

        PaymentResponse expectedResponse = new PaymentResponse(1L, Status.CREATED, pr.getPaymentDetails(),
                System.currentTimeMillis());

        when(paymentService.create(USER_ID, pr)).thenReturn(expectedResponse);

        ResultActions ra = mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\n" +
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
        assertPaymentResponse(ra, expectedResponse);
    }

    @Test
    public void create_sendInvalidRequest_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\n" +
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
        PaymentRequest paymentRequest = createPaymentRequest1();

        Throwable e = new ServiceException("");

        CustomGlobalExceptionHandler.ErrorsInfo expectedResponse = new CustomGlobalExceptionHandler.ErrorsInfo(
                new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), asList(ExceptionUtils.getMessage(e))
        );

        when(paymentService.create(USER_ID, paymentRequest)).thenThrow(e);

        mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\n" +
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
        PaymentDetails paymentDetails1 = createPaymentDetails1();

        PaymentDetails paymentDetails2 = createPaymentDetails2();

        PaymentResponse expectedResponse1 = new PaymentResponse(1L, Status.CREATED, paymentDetails1,
                System.currentTimeMillis());

        PaymentResponse expectedResponse2 = new PaymentResponse(2L, Status.PROCESSING, paymentDetails2,
                System.currentTimeMillis());

        int limit = 2;
        int offset = 0;

        when(paymentService.readAll(USER_ID, limit, offset)).thenReturn(asList(expectedResponse1, expectedResponse2));

        ResultActions ra = mockMvc.perform(get("/api/v1/payments")
                .param("limit", String.valueOf(limit))
                .param("offset", String.valueOf(offset))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID))
                .andExpect(status().isOk());
        assertPaymentResponse(ra, expectedResponse1, 0);
        assertPaymentResponse(ra, expectedResponse2, 1);
    }

    @Test
    public void readAll_sendInvalidRequestWithoutMandatoryUserIdHeaderValue_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/payments")
                //.header("userId", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void search_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        PaymentDetails paymentDetails1 = createPaymentDetails1();

        PaymentWithUserIdResponse expectedResponse = new PaymentWithUserIdResponse(1L, USER_ID, Status.PROCESSING, paymentDetails1,
                System.currentTimeMillis());

        int limit = 2;
        int offset = 0;

        SearchCriterias sc = new SearchCriterias();
        sc.setStatus(Status.PROCESSING);

        when(paymentService.search(sc, limit, offset)).thenReturn(asList(expectedResponse));

        ResultActions ra = mockMvc.perform(get("/api/v1/payments/search/result")
                .param("status", sc.getStatus().name())
                .param("limit", String.valueOf(limit))
                .param("offset", String.valueOf(offset))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        assertPaymentResponse(ra, expectedResponse, 0);
    }

    @Test
    public void search_sendInvalidRequestWithoutMandatoryRequestParam_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/payments/search/result")
                .param("limit", String.valueOf(2))
                .param("offset", "abc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void readById_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        long paymentId = 1L;
        PaymentDetails paymentDetails = createPaymentDetails1();

        PaymentResponse expectedResponse = new PaymentResponse(paymentId, Status.CREATED, paymentDetails,
                System.currentTimeMillis());

        when(paymentService.readById(USER_ID, paymentId)).thenReturn(expectedResponse);

        ResultActions ra = mockMvc.perform(get("/api/v1/payments/" + paymentId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID))
                .andExpect(status().isOk());
        assertPaymentResponse(ra, expectedResponse);
    }

    @Test
    public void readById_sendInvalidRequestWithoutMandatoryUserIdHeaderValue_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/payments/1")
                //.header("userId", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getPaymentStatus_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        long paymentId = 1L;
        Status status = Status.COMPLETED;
        when(paymentService.getStatus(USER_ID, paymentId)).thenReturn(status);

        mockMvc.perform(get("/api/v1/payments/" + paymentId + "/status")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(status.name()));
    }

    @Test
    public void getPaymentStatus_sendInvalidRequestWithWrongPathVarType_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/payments/abc/status")
                .header("userId", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateStatus_sendValidRequest_shouldReturn200andValidResponse() throws Exception {
        long paymentId = 1L;
        Status status = Status.PROCESSING;

        when(paymentService.updateStatus(USER_ID, paymentId, status)).thenReturn(status);

        mockMvc.perform(put("/api/v1/payments/" + paymentId + "/status")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\"status\": \"PROCESSING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(status.name()));
    }

    @Test
    public void updateStatus_sendInvalidRequest_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("userId", USER_ID)
                .content("{\n" +
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

    private PaymentRequest createPaymentRequest1() {
        PaymentDetails paymentDetails = createPaymentDetails1();
        return new PaymentRequest(paymentDetails);
    }

    private PaymentDetails createPaymentDetails1() {
        PaymentRecipientInfo recipientInfo1 = new PaymentRecipientInfo("Vasia", "Pupkin",
                "0000/00222/0XXXX", "Super Bank Inc.", "Main str. 1-1", "IBAN321");
        return new PaymentDetails(new Amount(100L, Currency.BYN), 1L,
                "IBAN123", recipientInfo1);
    }

    private PaymentDetails createPaymentDetails2() {
        PaymentRecipientInfo recipientInfo2 = new PaymentRecipientInfo("Ivan", "Ivanov",
                "0000/00222/0XXXX", "Super Bank 2 Inc.", "Main str. 1-2", "IBAN432");
        return new PaymentDetails(new Amount(200L, Currency.USD), 2L,
                "IBAN234", recipientInfo2);
    }

    private void assertPaymentResponse(ResultActions ra, PaymentResponse expectedResponse) throws Exception {
        PaymentDetails pd = expectedResponse.getPaymentDetails();
        Amount a = pd.getAmount();
        PaymentRecipientInfo ri = pd.getRecipientInfo();
        ra.andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus().name()))
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

    private void assertPaymentResponse(ResultActions ra, PaymentResponse expectedResponse, int index) throws Exception {
        PaymentDetails pd = expectedResponse.getPaymentDetails();
        Amount a = pd.getAmount();
        PaymentRecipientInfo ri = pd.getRecipientInfo();
        ra.andExpect(jsonPath("$[" + index + "].id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$[" + index + "].status").value(expectedResponse.getStatus().name()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.amount.amount").value(a.getAmount()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.amount.currency").value(a.getCurrency().name()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.bankId").value(pd.getBankId()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.accountNumber").value(pd.getAccountNumber()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.firstName").value(ri.getFirstName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.lastName").value(ri.getLastName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.bankUrn").value(ri.getBankUrn()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.bankName").value(ri.getBankName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.bankAddress").value(ri.getBankAddress()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.accountNumber").value(ri.getAccountNumber()))
                .andExpect(jsonPath("$[" + index + "].createdOn").value(expectedResponse.getCreatedOn()));
    }

    private void assertPaymentResponse(ResultActions ra, PaymentWithUserIdResponse expectedResponse, int index) throws Exception {
        PaymentDetails pd = expectedResponse.getPaymentDetails();
        Amount a = pd.getAmount();
        PaymentRecipientInfo ri = pd.getRecipientInfo();
        ra.andExpect(jsonPath("$[" + index + "].id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$[" + index + "].userId").value(expectedResponse.getUserId().toString()))
                .andExpect(jsonPath("$[" + index + "].status").value(expectedResponse.getStatus().name()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.amount.amount").value(a.getAmount()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.amount.currency").value(a.getCurrency().name()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.bankId").value(pd.getBankId()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.accountNumber").value(pd.getAccountNumber()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.firstName").value(ri.getFirstName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.lastName").value(ri.getLastName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.bankUrn").value(ri.getBankUrn()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.bankName").value(ri.getBankName()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.bankAddress").value(ri.getBankAddress()))
                .andExpect(jsonPath("$[" + index + "].paymentDetails.recipientInfo.accountNumber").value(ri.getAccountNumber()))
                .andExpect(jsonPath("$[" + index + "].createdOn").value(expectedResponse.getCreatedOn()));
    }

}