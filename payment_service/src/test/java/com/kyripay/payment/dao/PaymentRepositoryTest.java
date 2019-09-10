package com.kyripay.payment.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.kyripay.payment.config.DozerConfig;
import com.kyripay.payment.dao.exception.RepositoryException;
import com.kyripay.payment.dao.impl.jooq.JooqPaymentRepository;
import com.kyripay.payment.dao.impl.jooq.config.JooqConfig;
import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.PaymentRecipientInfo;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Currency;
import com.kyripay.payment.domain.vo.Status;
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
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JooqPaymentRepository.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = DozerConfig.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JooqConfig.class)
        }
)
@DBRider
@ActiveProfiles("test")
public class PaymentRepositoryTest {

    private static final UUID USER_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
    private static final long FIRST_PAYMENT_SCRIPT_ID = 1L;
    private static final long SECOND_PAYMENT_SCRIPT_ID = 2L;

    @Autowired
    private JooqPaymentRepository sut;

    @ClassRule
    public static final PostgreSQLContainer postgres = new PostgreSQLContainer();

    @Test
    @DataSet(value = {"datasets/clear_payments.xml"})
    public void create_noRecordsInDB_shouldCreate() {
        Payment createdPayment = sut.create(USER_ID, createFirstPayment());

        assertFirstPayment(createdPayment);
    }

    @Test
    @DataSet(value = {"datasets/clear_payments.xml"})
    public void readAll_noRecordsInDB_shouldReturnEmptyList() {
        List<Payment> payments = sut.readAll(USER_ID, 1, 0);

        assertThat(payments).isNotNull().isEmpty();
    }

    @Test
    @DataSet(value = {"datasets/insert_first_payment.xml", "datasets/insert_second_payment.xml"})
    public void readAll_recordsExistInDB_shouldReturnAllRecords() {
        List<Payment> payments = sut.readAll(USER_ID, 3, 0);

        assertThat(payments)
                .isNotNull()
                .size()
                .isEqualTo(2);
        assertFirstPayment(payments.stream().filter(p -> p.getId().equals(FIRST_PAYMENT_SCRIPT_ID)).findFirst().get());
        assertSecondPayment(payments.stream().filter(p -> p.getId().equals(SECOND_PAYMENT_SCRIPT_ID)).findFirst().get());
    }

    @Test
    @DataSet(value = {"datasets/clear_payments.xml"})
    public void readById_noRecordsInDB_shouldReturnNull() {
        assertThat(sut.readById(UUID.fromString("aaeeaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), -1L)).isNull();
    }

    @Test
    @DataSet(value = {"datasets/insert_first_payment.xml", "datasets/insert_second_payment.xml"})
    public void readById_recordsExistInDB_shouldReturnRecords() {
        assertFirstPayment(sut.readById(USER_ID, FIRST_PAYMENT_SCRIPT_ID));
        assertSecondPayment(sut.readById(USER_ID, SECOND_PAYMENT_SCRIPT_ID));
    }

    @Test
    @DataSet(value = {"datasets/clear_payments.xml"})
    public void getStatus_noRecordsInDB_shouldReturnNull() {
        assertThat(sut.getStatus(UUID.fromString("aaeeaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), -1L)).isNull();
    }

    @Test
    @DataSet(value = {"datasets/insert_first_payment.xml", "datasets/insert_second_payment.xml"})
    public void getStatus_recordsExistInDB_shouldReturnValidStatuses() {
        assertThat(sut.getStatus(USER_ID, FIRST_PAYMENT_SCRIPT_ID)).isNotNull().isEqualTo(Status.CREATED);
        assertThat(sut.getStatus(USER_ID, SECOND_PAYMENT_SCRIPT_ID)).isNotNull().isEqualTo(Status.COMPLETED);
    }

    @Test(expected = RepositoryException.class)
    @DataSet(value = {"datasets/clear_payments.xml"})
    public void updateStatus_noRecordsInDB_shouldThrownException() {
        assertThat(sut.updateStatus(UUID.fromString("aaeeaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), -1L, Status.PROCESSING));
    }

    @Test
    @DataSet(value = {"datasets/insert_first_payment.xml"})
    public void updateStatus_recordsExistInDB_shouldReturnValidStatuses() {
        assertThat(sut.updateStatus(USER_ID, FIRST_PAYMENT_SCRIPT_ID, Status.CREATED))
                .isNotNull()
                .isEqualTo(Status.CREATED);
        assertThat(sut.updateStatus(USER_ID, FIRST_PAYMENT_SCRIPT_ID, Status.PROCESSING))
                .isNotNull()
                .isEqualTo(Status.PROCESSING);
    }

    @Test
    @DataSet("datasets/insert_second_payment.xml")
    public void sequentScenarioOfCreateReadUpdateActions() {
        Payment firstPaymentCreated = sut.create(USER_ID, createFirstPayment());

        List<Payment> payments = sut.readAll(USER_ID, 3, 0);

        assertThat(payments)
                .isNotNull()
                .size()
                .isEqualTo(2);
        Payment firstPayment = payments.stream().filter(p -> p.getId().equals(firstPaymentCreated.getId())).findFirst()
                .get();
        Payment secondPayment = payments.stream().filter(p -> p.getId().equals(SECOND_PAYMENT_SCRIPT_ID)).findFirst()
                .get();
        assertFirstPayment(firstPayment);
        assertSecondPayment(secondPayment);

        long firstPaymentId = firstPayment.getId();
        Status newStatus = Status.PROCESSING;
        sut.updateStatus(USER_ID, firstPaymentId, newStatus);

        Payment updatedFirstPayment = sut.readById(USER_ID, firstPaymentId);
        assertFirstPayment(updatedFirstPayment, newStatus);

        Status readNewStatus = sut.getStatus(USER_ID, firstPaymentId);
        assertThat(readNewStatus).isNotNull().isEqualTo(newStatus);
    }

    private Payment createFirstPayment() {
        return new Payment(
                new Amount(500L, Currency.USD),
                1L,
                "IBAN123",
                Status.CREATED,
                new PaymentRecipientInfo(
                        "Vasia",
                        "Pupkin",
                        "0000/00222/0XXXX",
                        "Fake Bank Inc",
                        "Main str. 1-1",
                        "IBAN321"
                ),
                null
        );
    }

    private void assertFirstPayment(Payment payment) {
        assertFirstPayment(payment, Status.CREATED);
    }

    private void assertFirstPayment(Payment payment, Status status) {
        assertThat(payment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("status", status)
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
        assertThat(payment.getId()).isNotNull();
    }

    private void assertSecondPayment(Payment payment) {
        assertThat(payment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("status", Status.COMPLETED)
                .hasFieldOrPropertyWithValue("bankId", 2L)
                .hasFieldOrPropertyWithValue("accountNumber", "IBAN234")
                .hasFieldOrPropertyWithValue("amount.amount", 300L)
                .hasFieldOrPropertyWithValue("amount.currency", Currency.EUR)
                .hasFieldOrPropertyWithValue("recipientInfo.firstName", "Vasia2")
                .hasFieldOrPropertyWithValue("recipientInfo.lastName", "Pupkin2")
                .hasFieldOrPropertyWithValue("recipientInfo.bankUrn", "0000/00222/0XXXY")
                .hasFieldOrPropertyWithValue("recipientInfo.bankName", "Fake Bank Inc 2")
                .hasFieldOrPropertyWithValue("recipientInfo.bankAddress", "Main str. 1-2")
                .hasFieldOrPropertyWithValue("recipientInfo.accountNumber", "IBAN432")
                .hasFieldOrPropertyWithValue("id", SECOND_PAYMENT_SCRIPT_ID);
    }
}
