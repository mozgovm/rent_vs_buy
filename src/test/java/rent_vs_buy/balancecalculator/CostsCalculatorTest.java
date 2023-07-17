package rent_vs_buy.balancecalculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rent_vs_buy.models.ComparisonRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("testing")
class CostsCalculatorTest {

    @Autowired
    private CostsCalculator costsCalculator;

    @Test
    void shouldCalculateTotalDifferentiatedPayments() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(6000000D,
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

        double expectedTotalDifferentiatedPayments = 9795352.5;

        //when
        double actualTotalDifferentiatedPayments =
                costsCalculator.calculateTotalDifferentiatedPayments(comparisonRequest);

        // then
        assertThat(expectedTotalDifferentiatedPayments).isEqualTo(actualTotalDifferentiatedPayments);
    }

    @Test
    void shouldCalculateTotalDifferentiatedPaymentsWhenPayedFullPrice() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(6000000D,
                0D,
                25D,
                6.49D,
                25000D,
                4D,
                1500000D,
                8D,
                0.1,
                0.5,
                true);

        double expectedTotalDifferentiatedPayments = 0;

        //when
        double actualTotalDifferentiatedPayments =
                costsCalculator.calculateTotalDifferentiatedPayments(comparisonRequest);

        // then
        assertThat(expectedTotalDifferentiatedPayments).isEqualTo(actualTotalDifferentiatedPayments);
    }

    @Test
    void shouldCalculateTotalAnnuityPayments() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                1969000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double annuityPayment = 17075.0;
        double expectedTotalAnnuityPayments = 4098000.0;

        // when
        double actualTotalAnnuityPayments =
                costsCalculator.calculateTotalAnnuityPayments(comparisonRequest, annuityPayment);

        // then
        assertThat(expectedTotalAnnuityPayments).isEqualTo(actualTotalAnnuityPayments);
    }

    @Test
    void shouldCalculateTotalAnnuityPaymentsWhenPayedFullPrice() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                0D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double annuityPayment = 17075.0;
        double expectedTotalAnnuityPayments = 0;

        // when
        double actualTotalAnnuityPayments =
                costsCalculator.calculateTotalAnnuityPayments(comparisonRequest, annuityPayment);

        // then
        assertThat(expectedTotalAnnuityPayments).isEqualTo(actualTotalAnnuityPayments);
    }

    @Test
    void shouldCalculateRentForWholePeriod() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                0D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double expectedRentForWholePeriod = 7861412.74;

        // when
        double actualRentForWholePeriod = costsCalculator.calculateRentForWholePeriod(comparisonRequest);

        // then
        assertThat(expectedRentForWholePeriod).isEqualTo(actualRentForWholePeriod);
    }

    @Test
    void shouldCalculateAbsoluteMonthlyPercent() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                1969000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double loanLeft = 1600000.0;
        double expectedAbsoluteMonthlyPercent = 11320.0;

        //when
        double actualAbsoluteMonthlyPercent = costsCalculator.calculateAbsoluteMonthlyPercent(comparisonRequest,
                loanLeft, costsCalculator.getMonthlyInterestRate(8.49));

        // then
        assertThat(expectedAbsoluteMonthlyPercent).isEqualTo(actualAbsoluteMonthlyPercent);
    }

    @Test
    void shouldCalculateAbsoluteMonthlyPercentWhenPayedFullPrice() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                0D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double loanLeft = 1600000.0;
        double expectedAbsoluteMonthlyPercent = 0;

        //when
        double actualAbsoluteMonthlyPercent = costsCalculator.calculateAbsoluteMonthlyPercent(comparisonRequest,
                loanLeft, costsCalculator.getMonthlyInterestRate(8.49));

        // then
        assertThat(expectedAbsoluteMonthlyPercent).isEqualTo(actualAbsoluteMonthlyPercent);
    }

    @Test
    void shouldCalculateLoanBodyPayedPerMonth() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                1969000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double annuityPayment = 17075.0;
        double absoluteMonthlyPercent = 11320.0;
        double expectedLoanBodyPayedPerMonth = 5755.0;

        // when
        double actualLoanBodyPayedPerMonth = costsCalculator.calculateLoanBodyPayedPerMonth(comparisonRequest,
                annuityPayment, absoluteMonthlyPercent);

        // then
        assertThat(expectedLoanBodyPayedPerMonth).isEqualTo(actualLoanBodyPayedPerMonth);
    }

    @Test
    void shouldCalculateLoanBodyPayedPerMonthWhenPayedFullPrice() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                0D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double annuityPayment = 17075.0;
        double absoluteMonthlyPercent = 11320.0;
        double expectedLoanBodyPayedPerMonth = 0;

        // when
        double actualLoanBodyPayedPerMonth = costsCalculator.calculateLoanBodyPayedPerMonth(comparisonRequest,
                annuityPayment, absoluteMonthlyPercent);

        // then
        assertThat(expectedLoanBodyPayedPerMonth).isEqualTo(actualLoanBodyPayedPerMonth);
    }

    @Test
    void shouldCalculateInsurancePaymentForWholePeriodWithAnnuityPayment() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                3500000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double annuityPayment = 20000.0;
        double insurancePercent = 0.5;
        double expectedInsurancePaymentForWholePeriod = 451593.37;

        // when
        double actualInsurancePaymentForWholePeriod = costsCalculator.calculateInsurancePaymentForWholePeriod(comparisonRequest,
                annuityPayment, insurancePercent, false);

        // then
        assertThat(expectedInsurancePaymentForWholePeriod).isEqualTo(actualInsurancePaymentForWholePeriod);
    }

    @Test
    void shouldCalculateInsurancePaymentForWholePeriodWithDifferentiatedPayment() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                3500000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                true);

        double annuityPayment = 20000.0;
        double insurancePercent = 0.5;
        double expectedInsurancePaymentForWholePeriod = 183750.0;

        // when
        double actualInsurancePaymentForWholePeriod =
                costsCalculator.calculateInsurancePaymentForWholePeriod(comparisonRequest, annuityPayment,
                insurancePercent, true);

        // then
        assertThat(expectedInsurancePaymentForWholePeriod).isEqualTo(actualInsurancePaymentForWholePeriod);
    }

    @Test
    void shouldCalculateInsurancePaymentForWholePeriodWhenPayedFullPrice() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                0D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                true);

        double annuityPayment = 20000.0;
        double insurancePercent = 0.5;
        double expectedInsurancePaymentForWholePeriod = 0;

        // when
        double actualInsurancePaymentForWholePeriod =
                costsCalculator.calculateInsurancePaymentForWholePeriod(comparisonRequest,
                annuityPayment, insurancePercent, false);

        // then
        assertThat(expectedInsurancePaymentForWholePeriod).isEqualTo(actualInsurancePaymentForWholePeriod);
    }

    @Test
    void shouldCalculateTaxForWholePeriod() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                0D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                true);

        double expectedTaxForWholePeriod = 84000;

        // when
        double actualTaxForWholePeriod = costsCalculator.calculateTaxForWholePeriod(comparisonRequest, 0.1);

        // then
        assertThat(expectedTaxForWholePeriod).isEqualTo(actualTaxForWholePeriod);
    }

    @Test
    void shouldCalculateTotalLossesWithAnnuityPayment() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                1969000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double expectedTotalLosses = 1918022.9;
        double finalRealEstatePrice = 5124798.17;

        // when
        double actualTotalLosses = costsCalculator.calculateTotalLossesWithAnnuityPayment(comparisonRequest,
                finalRealEstatePrice);

        // then
        assertThat(expectedTotalLosses).isEqualTo(actualTotalLosses);
    }

    @Test
    void shouldCalculateTotalLossesWithDifferentiatedPayment() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                1969000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                true);

        double expectedTotalLosses = 1441220.65;
        double finalRealEstatePrice = 5124798.17;

        // when
        double actualTotalLosses = costsCalculator.calculateTotalLossesWithDifferentiatedPayment(comparisonRequest,
                finalRealEstatePrice);

        // then
        assertThat(expectedTotalLosses).isEqualTo(actualTotalLosses);
    }

    @Test
    void shouldCalculateTotalCostsForRenting() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                1969000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                true);

        double expectedTotalCostsForRenting = 7861412.74;

        // when
        double actualTotalCostsForRenting = costsCalculator.calculateTotalCostsForRenting(comparisonRequest);

        // then
        assertThat(expectedTotalCostsForRenting).isEqualTo(actualTotalCostsForRenting);
    }
}