package rent_vs_buy.balancecalculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rent_vs_buy.models.ComparisonRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BalanceCalculatorTest {

    @Autowired
    private BalanceCalculator balanceCalculator;
    private ComparisonRequest comparisonRequest;
    private ComparisonRequest comparisonRequestWithPayedFullPrice;

    @BeforeEach
    void setUp() {
        comparisonRequest = new ComparisonRequest(6000000D,
                5400000D,
                25D,
                6.49D,
                25000D,
                4D,
                1500000D,
                8D,
                0.1,
                0.5,
                false);

        assertThat(comparisonRequest.getLoanBody()).isEqualTo(5400000.0);

        comparisonRequestWithPayedFullPrice = new ComparisonRequest(6000000D,
                0D,
                25D,
                6.49D,
                25000D,
                4D,
                1500000D,
                8D,
                0.1,
                0.5,
                false);

        assertThat(comparisonRequest.getLoanBody())
                .isNotEqualTo(comparisonRequestWithPayedFullPrice.getLoanBody());
    }

    @Test
    void shouldRoundDouble() {
        // given
        double expected = 12.35;

        // when
        double actual = balanceCalculator.round(12.3456789);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void shouldGetMonthlyInterestRate() {
        // given
        double expectedMonthlyInterestRate = 0.0070750000000000006;
        double interestRate = 8.49;


        // when
        double actualMonthlyInterestRate = balanceCalculator.getMonthlyInterestRate(interestRate);

        // then
        assertThat(expectedMonthlyInterestRate).isEqualTo(actualMonthlyInterestRate);
    }

    @Test
    void shouldCalculateAnnuityPayment() {
        // given
        double expectedAnnuityPayment = 36427.45;

        // when
        double actualAnnuityPayment = balanceCalculator.calculateAnnuityPayment(comparisonRequest);

        // then
        assertThat(expectedAnnuityPayment).isEqualTo(actualAnnuityPayment);
    }

    @Test
    void shouldSetAnnuityPaymentToMonthlyRentWhenPayedFullPrice() {
        // given

        double monthlyRent = 25000;

        // when
        double annuityPayment = balanceCalculator.calculateAnnuityPayment(comparisonRequestWithPayedFullPrice);

        // then
        assertThat(monthlyRent).isEqualTo(annuityPayment);
    }

    @Test
    void shouldCalculateMonthlyDifferentiatedPayment() {
        // given
        double expectedDifferentiatedPayment = 28649.01;
        double loanLeft = 1969000.0;

        // when
        double actualDifferentiatedPayment = balanceCalculator.calculateMonthlyDifferentiatedPayment(comparisonRequest,
                loanLeft);

        // then
        assertThat(expectedDifferentiatedPayment).isEqualTo(actualDifferentiatedPayment);
    }

    @Test
    void shouldCalculatePositiveBalance() {
        // given
        double totalLoss = 2000000.0;
        double totalGains = 4000000.0;
        double expectedBalance = 2000000.0;

        // when
        double actualBalance = balanceCalculator.calculateBalance(totalLoss, totalGains);

        // then
        assertThat(expectedBalance).isEqualTo(actualBalance);
    }

    @Test
    void shouldCalculateZeroBalance() {
        // given
        double totalLoss = 2000000.0;
        double totalGains = 2000000.0;
        double expectedBalance = 0.0;

        // when
        double actualBalance = balanceCalculator.calculateBalance(totalLoss, totalGains);

        // then
        assertThat(expectedBalance).isEqualTo(actualBalance);
    }

    @Test
    void shouldCalculateNegativeBalance() {
        // given
        double totalLoss = 4000000.0;
        double totalGains = 2000000.0;
        double expectedBalance = -2000000.0;

        // when
        double actualBalance = balanceCalculator.calculateBalance(totalLoss, totalGains);

        // then
        assertThat(expectedBalance).isEqualTo(actualBalance);
    }
}