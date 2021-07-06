package rent_vs_buy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BalanceTests {
    private Balance balance;
    private Balance balanceWithPayedFullPrice;

    @BeforeEach
    void setUp() {
        balance = new Balance(6000000,
                5400000,
                25,
                6.49F,
                25000,
                4F,
                1500000,
                8,
                false);

        assertThat(balance.getLoanBody()).isEqualTo(5400000.0);
        balanceWithPayedFullPrice = new Balance(6000000,
                0,
                25,
                6.49F,
                25000,
                4F,
                1500000,
                8,
                false);

       assertThat(balance.getLoanBody()).isNotEqualTo(balanceWithPayedFullPrice.getLoanBody());
    }

    @Test
    void shouldRoundDouble() {
        // given
        double expected = 12.35;

        // when
        double actual = balance.round(12.3456789);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void shouldGetMonthlyInterestRate() {
        // given
        double expectedMonthlyInterestRate = 0.0070750000000000006;
        double interestRate = 8.49;


        // when
        double actualMonthlyInterestRate = balance.getMonthlyInterestRate(interestRate);

        // then
        assertThat(expectedMonthlyInterestRate).isEqualTo(actualMonthlyInterestRate);
    }

    @Test
    void shouldCalculateAnnuityPayment() {
        // given
        double expectedAnnuityPayment = 36427.45;

        // when
        double actualAnnuityPayment = balance.calculateAnnuityPayment();

        // then
        assertThat(expectedAnnuityPayment).isEqualTo(actualAnnuityPayment);
    }

    @Test
    void shouldSetAnnuityPaymentToMonthlyRentWhenPayedFullPrice() {
        // given

        double monthlyRent = 25000;

        // when
        double annuityPayment = balanceWithPayedFullPrice.calculateAnnuityPayment();

        // then
        assertThat(monthlyRent).isEqualTo(annuityPayment);
    }

    @Test
    void shouldCalculateMonthlyDifferentiatedPayment() {
        // given
        double expectedDifferentiatedPayment = 28649.01;
        double loanLeft = 1969000.0;

        // when
        double actualDifferentiatedPayment = balance.calculateMonthlyDifferentiatedPayment(loanLeft);

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
        double actualBalance = balance.calculateBalance(totalLoss, totalGains);

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
        double actualBalance = balance.calculateBalance(totalLoss, totalGains);

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
        double actualBalance = balance.calculateBalance(totalLoss, totalGains);

        // then
        assertThat(expectedBalance).isEqualTo(actualBalance);
    }
}
