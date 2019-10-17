package com.kyripay.converter.contractTests;

import com.kyripay.converter.api.mq.ConverterAdapter;
import com.kyripay.converter.api.rest.ConverterController;
import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;
import com.kyripay.converter.dto.events.ConversionFinishedEvent;
import com.kyripay.converter.service.ConversionService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMessageVerifier
@ActiveProfiles("test")
public abstract class ContractTestBase
{

    @Autowired
    private ConverterController converterController;
    @Autowired
    private ConverterAdapter adapter;

    @MockBean
    ConversionService service;

    @Before
    public void setup() {
        when(service.getFormats()).thenReturn(Map.of(
            "TEST", FormatDetails.of("TEST", "Test converter", "For API testing")
        ));
        when(service.convert(any(Payment.class), any(String.class)))
            .thenAnswer((Answer) invocation -> {
                adapter.conversionFinished(new ConversionFinishedEvent(this, UUID.randomUUID().toString()));
                return null;
            });
        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.standaloneSetup(converterController));
    }
}