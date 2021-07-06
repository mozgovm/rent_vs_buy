package rent_vs_buy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CostTests {
    @Test
    void shouldCalculateTotalDifferentiatedPayments() {
        // given
        Balance balance = new Balance(6000000,
                5400000,
                25,
                6.49F,
                25000,
                4F,
                1500000,
                8,
                false);
        Cost cost = new Cost(balance);
        double expectedTotalDifferentiatedPayments = 9795352.5;

        //when
        double actualTotalDifferentiatedPayments = cost.calculateTotalDifferentiatedPayments();

        // then
        assertThat(expectedTotalDifferentiatedPayments).isEqualTo(actualTotalDifferentiatedPayments);
    }

    @Test
    void shouldCalculateTotalDifferentiatedPaymentsWhenPayedFullPrice() {
        // given
        Balance balance = new Balance(6000000,
                0,
                25,
                6.49F,
                25000,
                4F,
                1500000,
                8,
                true);
        Cost cost = new Cost(balance);
        double expectedTotalDifferentiatedPayments = 0;

        //when
        double actualTotalDifferentiatedPayments = cost.calculateTotalDifferentiatedPayments();

        // then
        assertThat(expectedTotalDifferentiatedPayments).isEqualTo(actualTotalDifferentiatedPayments);
    }

    @Test
    void shouldCalculateTotalAnnuityPayments() {
        // given
        Balance balance = new Balance(4200000,
                1969000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Cost cost = new Cost(balance);
        double annuityPayment = 17075.0;
        double expectedTotalAnnuityPayments = 4098000.0;

        // when
        double actualTotalAnnuityPayments = cost.calculateTotalAnnuityPayments(annuityPayment);

        // then
        assertThat(expectedTotalAnnuityPayments).isEqualTo(actualTotalAnnuityPayments);
    }

    @Test
    void shouldCalculateTotalAnnuityPaymentsWhenPayedFullPrice() {
        // given
        Balance balance = new Balance(4200000,
                0,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Cost cost = new Cost(balance);
        double annuityPayment = 17075.0;
        double expectedTotalAnnuityPayments = 0;

        // when
        double actualTotalAnnuityPayments = cost.calculateTotalAnnuityPayments(annuityPayment);

        // then
        assertThat(expectedTotalAnnuityPayments).isEqualTo(actualTotalAnnuityPayments);
    }

    @Test
    void shouldCalculateRentForWholePeriod() {
        // given
        Balance balance = new Balance(4200000,
                0,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Cost cost = new Cost(balance);
        double expectedRentForWholePeriod = 7861412.74;

        // when
        double actualRentForWholePeriod = cost.calculateRentForWholePeriod();

        // then
        assertThat(expectedRentForWholePeriod).isEqualTo(actualRentForWholePeriod);
    }

    @Test
    void shouldCalculateAbsoluteMonthlyPercent() {
        // given
        Balance balance = new Balance(4200000,
                1969000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Cost cost = new Cost(balance);
        double loanLeft = 1600000.0;
        double expectedAbsoluteMonthlyPercent = 11320.0;

        //when
        double actualAbsoluteMonthlyPercent = cost.calculateAbsoluteMonthlyPercent(loanLeft,
                balance.getMonthlyInterestRate(8.49));

        // then
        assertThat(expectedAbsoluteMonthlyPercent).isEqualTo(actualAbsoluteMonthlyPercent);
    }

    @Test
    void shouldCalculateAbsoluteMonthlyPercentWhenPayedFullPrice() {
        // given
        Balance balance = new Balance(4200000,
                0,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Cost cost = new Cost(balance);
        double loanLeft = 1600000.0;
        double expectedAbsoluteMonthlyPercent = 0;

        //when
        double actualAbsoluteMonthlyPercent = cost.calculateAbsoluteMonthlyPercent(loanLeft,
                balance.getMonthlyInterestRate(8.49));

        // then
        assertThat(expectedAbsoluteMonthlyPercent).isEqualTo(actualAbsoluteMonthlyPercent);
    }

    @Test
    void shouldCalculateLoanBodyPayedPerMonth() {
        // given
        Balance balance = new Balance(4200000,
                1969000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Cost cost = new Cost(balance);
        double annuityPayment = 17075.0;
        double absoluteMonthlyPercent = 11320.0;
        double expectedLoanBodyPayedPerMonth = 5755.0;

        // when
        double actualLoanBodyPayedPerMonth = cost.calculateLoanBodyPayedPerMonth(annuityPayment,
                absoluteMonthlyPercent);

        // then
        assertThat(expectedLoanBodyPayedPerMonth).isEqualTo(actualLoanBodyPayedPerMonth);
    }

    @Test
    void shouldCalculateLoanBodyPayedPerMonthWhenPayedFullPrice() {
        // given
        Balance balance = new Balance(4200000,
                0,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Cost cost = new Cost(balance);
        double annuityPayment = 17075.0;
        double absoluteMonthlyPercent = 11320.0;
        double expectedLoanBodyPayedPerMonth = 0;

        // when
        double actualLoanBodyPayedPerMonth = cost.calculateLoanBodyPayedPerMonth(annuityPayment,
                absoluteMonthlyPercent);

        // then
        assertThat(expectedLoanBodyPayedPerMonth).isEqualTo(actualLoanBodyPayedPerMonth);
    }

    @Test
    void shouldCalculateInsurancePaymentForWholePeriodWithAnnuityPayment() {
        // given
        Balance balance = new Balance(4200000,
                3500000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Cost cost = new Cost(balance);
        double annuityPayment = 20000.0;
        double insurancePercent = 0.5;
        double expectedInsurancePaymentForWholePeriod = 451593.35;

        // when
        double actualInsurancePaymentForWholePeriod = cost.calculateInsurancePaymentForWholePeriod(annuityPayment,
                insurancePercent,
                false);

        // then
        assertThat(expectedInsurancePaymentForWholePeriod).isEqualTo(actualInsurancePaymentForWholePeriod);
    }

    @Test
    void shouldCalculateInsurancePaymentForWholePeriodWithDifferentiatedPayment() {
        // given
        Balance balance = new Balance(4200000,
                3500000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                true);
        Cost cost = new Cost(balance);
        double annuityPayment = 20000.0;
        double insurancePercent = 0.5;
        double expectedInsurancePaymentForWholePeriod = 183750.0;

        // when
        double actualInsurancePaymentForWholePeriod = cost.calculateInsurancePaymentForWholePeriod(annuityPayment,
                insurancePercent,
                true);

        // then
        assertThat(expectedInsurancePaymentForWholePeriod).isEqualTo(actualInsurancePaymentForWholePeriod);
    }

    @Test
    void shouldCalculateInsurancePaymentForWholePeriodWhenPayedFullPrice() {
        // given
        Balance balance = new Balance(4200000,
                0,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                true);
        Cost cost = new Cost(balance);
        double annuityPayment = 20000.0;
        double insurancePercent = 0.5;
        double expectedInsurancePaymentForWholePeriod = 0;

        // when
        double actualInsurancePaymentForWholePeriod = cost.calculateInsurancePaymentForWholePeriod(annuityPayment,
                insurancePercent,
                false);

        // then
        assertThat(expectedInsurancePaymentForWholePeriod).isEqualTo(actualInsurancePaymentForWholePeriod);
    }

    @Test
    void shouldCalculateTaxForWholePeriod() {
        // given
        Balance balance = new Balance(4200000,
                0,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                true);
        Cost cost = new Cost(balance);
        double expectedTaxForWholePeriod = 84000;

        // when
        double actualTaxForWholePeriod = cost.calculateTaxForWholePeriod(0.1);

        // then
        assertThat(expectedTaxForWholePeriod).isEqualTo(actualTaxForWholePeriod);
    }

    @Test
    void shouldCalculateTotalLossesWithAnnuityPayment() {
        // given
        Balance balance = new Balance(4200000,
                1969000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Cost cost = new Cost(balance);
        Gain gain = new Gain(balance);
        double expectedTotalLosses = 1918022.9;
        double finalRealEstatePrice = gain.calculateFinalRealEstatePrice();

        // when
        double actualTotalLosses = cost.calculateTotalLossesWithAnnuityPayment(0.1,
                0.5,
                finalRealEstatePrice);

        // then
        assertThat(expectedTotalLosses).isEqualTo(actualTotalLosses);
    }

    @Test
    void shouldCalculateTotalLossesWithDifferentiatedPayment() {
        // given
        Balance balance = new Balance(4200000,
                1969000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                true);
        Cost cost = new Cost(balance);
        Gain gain = new Gain(balance);
        double expectedTotalLosses = 1441220.63;
        double finalRealEstatePrice = gain.calculateFinalRealEstatePrice();

        // when
        double actualTotalLosses = cost.calculateTotalLossesWithDifferentiatedPayment(0.1,
                0.5,
                finalRealEstatePrice);

        // then
        assertThat(expectedTotalLosses).isEqualTo(actualTotalLosses);
    }

    @Test
    void shouldCalculateTotalCostsForRenting() {
        // given
        Balance balance = new Balance(4200000,
                1969000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                true);
        Cost cost = new Cost(balance);
        double expectedTotalCostsForRenting = 7861412.74;

        // when
        double actualTotalCostsForRenting = cost.calculateTotalCostsForRenting();

        // then
        assertThat(expectedTotalCostsForRenting).isEqualTo(actualTotalCostsForRenting);
    }
}
