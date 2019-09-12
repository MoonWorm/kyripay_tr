package com.kyripay.payment.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.kyripay.payment.config.DozerConfig;
import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.dao.impl.jooq.JooqPaymentTemplateRepository;
import com.kyripay.payment.dao.impl.jooq.config.JooqConfig;
import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.PaymentTemplateRecipientInfo;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JooqTest(
        includeFilters = {
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JooqPaymentTemplateRepository.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = DozerConfig.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JooqConfig.class)
        }
)
@DBRider
@ActiveProfiles("test")
public class PaymentTemplateRepositoryTest {

    private static final UUID USER_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
    private static final long FIRST_PAYMENT_TEMPLATE_SCRIPT_ID = 1L;
    private static final long SECOND_PAYMENT_TEMPLATE_SCRIPT_ID = 2L;

    @Autowired
    private JooqPaymentTemplateRepository sut;

    @ClassRule
    public static final PostgreSQLContainer postgres = new PostgreSQLContainer();

    @Test
    @DataSet(value = {"datasets/clear_payment_templates.xml"})
    public void create_noRecordsInDB_shouldCreate() {
        PaymentTemplate createdPaymentTemplate = sut.create(USER_ID, createFirstPaymentTemplate());

        assertFirstPaymentTemplate(createdPaymentTemplate);
    }

    @Test
    @DataSet(value = {"datasets/clear_payment_templates.xml"})
    public void readAll_noRecordsInDB_shouldReturnEmptyList() {
        List<PaymentTemplate> payments = sut.readAll(USER_ID, 1, 0);

        assertThat(payments).isNotNull().isEmpty();
    }

    @Test
    @DataSet(value = {"datasets/insert_first_payment_template.xml", "datasets/insert_second_payment_template.xml"})
    public void readAll_recordsExistInDB_shouldReturnAllRecords() {
        List<PaymentTemplate> payments = sut.readAll(USER_ID, 3, 0);

        assertThat(payments)
                .isNotNull()
                .size()
                .isEqualTo(2);
        assertFirstPaymentTemplate(
                payments.stream().filter(p -> p.getId().equals(FIRST_PAYMENT_TEMPLATE_SCRIPT_ID)).findFirst().get()
        );
        assertSecondPaymentTemplate(
                payments.stream().filter(p -> p.getId().equals(SECOND_PAYMENT_TEMPLATE_SCRIPT_ID)).findFirst().get()
        );
    }

    @Test
    @DataSet(value = {"datasets/clear_payment_templates.xml"})
    public void readById_noRecordsInDB_shouldReturnNull() {
        assertThat(sut.readById(UUID.fromString("aaeeaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), -1L)).isNull();
    }

    @Test
    @DataSet(value = {"datasets/insert_first_payment_template.xml", "datasets/insert_second_payment_template.xml", "datasets/clear_payment_templates.xml"})
    public void readById_recordsExistInDB_shouldReturnRecords() {
        assertFirstPaymentTemplate(sut.readById(USER_ID, FIRST_PAYMENT_TEMPLATE_SCRIPT_ID));
        assertSecondPaymentTemplate(sut.readById(USER_ID, SECOND_PAYMENT_TEMPLATE_SCRIPT_ID));
    }

    @Test(expected = RepositoryException.class)
    @DataSet(value = {"datasets/clear_payment_templates.xml"})
    public void update_noRecordsInDB_shouldThrownException() {
        assertThat(sut.update(UUID.fromString("aaeeaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), -1L, createFirstPaymentTemplate()));
    }

    @Test
    @DataSet(value = {"datasets/insert_first_payment_template.xml", "datasets/clear_payment_templates.xml"})
    public void update_recordsExistInDB_shouldReturnValidRecord() {
        PaymentTemplate paymentTemplateWithNewData = createPaymentTemplateWithNewData();
        PaymentTemplate paymentTemplateUpdated = sut.update(USER_ID, FIRST_PAYMENT_TEMPLATE_SCRIPT_ID, paymentTemplateWithNewData);

        paymentTemplateWithNewData.setId(paymentTemplateUpdated.getId());
        paymentTemplateWithNewData.setCreatedOn(paymentTemplateUpdated.getCreatedOn());
        assertThat(paymentTemplateUpdated).isNotNull().isEqualTo(paymentTemplateWithNewData);
    }

    @Test
    @DataSet(value = {"datasets/clear_payment_templates.xml"})
    public void delete_noRecordsInDB_shouldNotFail() {
        assertThat(sut.readAll(USER_ID, 3, 0)).isNotNull().isEmpty();
        sut.delete(USER_ID, FIRST_PAYMENT_TEMPLATE_SCRIPT_ID);
        sut.delete(USER_ID, SECOND_PAYMENT_TEMPLATE_SCRIPT_ID);
    }

    @Test
    @DataSet(value = {"datasets/insert_first_payment_template.xml", "datasets/insert_second_payment_template.xml", "datasets/clear_payment_templates.xml"})
    public void delete_recordsExistInDB_shouldDeleteAllRecords() {
        assertThat(sut.readAll(USER_ID, 3, 0)).isNotNull().size().isEqualTo(2);
        sut.delete(USER_ID, FIRST_PAYMENT_TEMPLATE_SCRIPT_ID);
        assertThat(sut.readAll(USER_ID, 3, 0)).isNotNull().size().isEqualTo(1);
        sut.delete(USER_ID, SECOND_PAYMENT_TEMPLATE_SCRIPT_ID);
        assertThat(sut.readAll(USER_ID, 3, 0)).isNotNull().isEmpty();
    }

    @Test
    @DataSet(value = {"datasets/insert_second_payment_template.xml"})
    public void sequentScenarioOfCreateReadUpdateDeleteActions() {
        PaymentTemplate firstPaymentTemplateCreated = sut.create(USER_ID, createFirstPaymentTemplate());

        List<PaymentTemplate> paymentTemplates = sut.readAll(USER_ID, 3, 0);

        assertThat(paymentTemplates)
                .isNotNull()
                .size()
                .isEqualTo(2);
        PaymentTemplate firstPayment = paymentTemplates.stream()
                .filter(p -> p.getId().equals(firstPaymentTemplateCreated.getId())).findFirst().get();
        PaymentTemplate secondPayment = paymentTemplates.stream()
                .filter(p -> p.getId().equals(SECOND_PAYMENT_TEMPLATE_SCRIPT_ID)).findFirst().get();
        assertFirstPaymentTemplate(firstPayment);
        assertSecondPaymentTemplate(secondPayment);

        long firstPaymentTemplateId = firstPayment.getId();

        PaymentTemplate paymentTemplateWithNewData = createPaymentTemplateWithNewData();

        PaymentTemplate paymentTemplateUpdated = sut.update(USER_ID, firstPaymentTemplateId, paymentTemplateWithNewData);
        paymentTemplateWithNewData.setId(paymentTemplateUpdated.getId());
        paymentTemplateWithNewData.setCreatedOn(paymentTemplateUpdated.getCreatedOn());
        assertThat(paymentTemplateUpdated).isNotNull().isEqualTo(paymentTemplateWithNewData);

        PaymentTemplate paymentTemplateUpdatedRead = sut.readById(USER_ID, firstPaymentTemplateId);
        assertThat(paymentTemplateUpdatedRead).isNotNull().isEqualTo(paymentTemplateWithNewData);

        sut.delete(USER_ID, firstPaymentTemplateId);
        assertThat(sut.readById(USER_ID, firstPaymentTemplateId)).isNull();
        paymentTemplates = sut.readAll(USER_ID, 2, 0);
        assertThat(paymentTemplates).isNotNull().size().isEqualTo(1);
        assertSecondPaymentTemplate(paymentTemplates.get(0));

        sut.delete(USER_ID, SECOND_PAYMENT_TEMPLATE_SCRIPT_ID);
        assertThat(sut.readById(USER_ID, SECOND_PAYMENT_TEMPLATE_SCRIPT_ID)).isNull();
        assertThat(sut.readAll(USER_ID, 2, 0)).isNotNull().isEmpty();
    }

    private PaymentTemplate createFirstPaymentTemplate() {
        PaymentTemplateRecipientInfo ri = new PaymentTemplateRecipientInfo();
        ri.setFirstName("Vasia");
        ri.setLastName("Pupkin");
        ri.setBankUrn("0000/00222/0XXXX");
        ri.setBankName("Fake Bank Inc");
        ri.setBankAddress("Main str. 1-1");
        ri.setAccountNumber("IBAN321");

        PaymentTemplate pt = new PaymentTemplate("Template 1");
        pt.setBankId(1L);
        pt.setAccountNumber("IBAN123");
        pt.setAmount(new Amount(500L, Currency.USD));
        pt.setRecipientInfo(ri);

        return pt;
    }

    private PaymentTemplate createPaymentTemplateWithNewData() {
        PaymentTemplateRecipientInfo ri = new PaymentTemplateRecipientInfo();
        ri.setFirstName("Ivan");
        ri.setLastName("Ivanov");
        ri.setBankUrn("0000/00222/0XXXZ");
        ri.setBankName("Fake Bank Inc 2");
        ri.setBankAddress("Main str. 1-3");
        ri.setAccountNumber("IBAN543");

        PaymentTemplate pt = new PaymentTemplate("Template Updated");
        pt.setBankId(3L);
        pt.setAccountNumber("IBAN345");
        pt.setAmount(new Amount(400L, Currency.BYN));
        pt.setRecipientInfo(ri);

        return pt;
    }

    private void assertFirstPaymentTemplate(PaymentTemplate paymentTemplate) {
        assertThat(paymentTemplate)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Template 1")
                .hasFieldOrPropertyWithValue("bankId", 1L)
                .hasFieldOrPropertyWithValue("accountNumber", "IBAN123")
                .hasFieldOrPropertyWithValue("amount.amount", 500L)
                .hasFieldOrPropertyWithValue("amount.currency", Currency.USD)
                .hasFieldOrPropertyWithValue("recipientInfo.firstName", "Vasia")
                .hasFieldOrPropertyWithValue("recipientInfo.lastName", "Pupkin")
                .hasFieldOrPropertyWithValue("recipientInfo.bankUrn", "0000/00222/0XXXX")
                .hasFieldOrPropertyWithValue("recipientInfo.bankName", "Fake Bank Inc")
                .hasFieldOrPropertyWithValue("recipientInfo.bankAddress", "Main str. 1-1")
                .hasFieldOrPropertyWithValue("recipientInfo.accountNumber", "IBAN321")
                .hasFieldOrProperty("id");
        assertThat(paymentTemplate.getId()).isNotNull();
    }

    private void assertSecondPaymentTemplate(PaymentTemplate paymentTemplate) {
        assertThat(paymentTemplate)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Template 2")
                .hasFieldOrPropertyWithValue("bankId", 2L)
                .hasFieldOrPropertyWithValue("accountNumber", "IBAN234")
                .hasFieldOrPropertyWithValue("amount.amount", 300L)
                .hasFieldOrPropertyWithValue("amount.currency", Currency.EUR)
                .hasFieldOrPropertyWithValue("recipientInfo.firstName", "Vasia2")
                .hasFieldOrPropertyWithValue("recipientInfo.bankName", "Fake Bank Inc 2")
                .hasFieldOrPropertyWithValue("recipientInfo.bankUrn", "0000/00222/0XXXY")
                .hasFieldOrPropertyWithValue("recipientInfo.lastName", "Pupkin2")
                .hasFieldOrPropertyWithValue("recipientInfo.bankAddress", "Main str. 1-2")
                .hasFieldOrPropertyWithValue("recipientInfo.accountNumber", "IBAN432")
                .hasFieldOrPropertyWithValue("id", SECOND_PAYMENT_TEMPLATE_SCRIPT_ID);
    }
}
