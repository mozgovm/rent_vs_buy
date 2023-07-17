package rent_vs_buy.balancecalculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rent_vs_buy.models.ComparisonRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("testing")
class GainsCalculatorTest {

    @Autowired
    private GainsCalculator gainsCalculator;

    @Test
    void maxTaxDeductionTest() {
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
        // given

        double fullPrice = 5000000.0;
        double expectedResult = 260000.0;

        // when
        double actualResult = gainsCalculator.calculateTaxDeduction(fullPrice);

        // then
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void taxDeductionTest() {
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

        double fullPrice = 1000000.0;
        double expectedResult = 130000.00;

        // when
        double actualResult = gainsCalculator.calculateTaxDeduction(fullPrice);

        // then
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void shouldCalculateCompoundingEffectWithAnnuityPayments() {
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

        double annuityPayment = 36427.45;
        double expectedCompoundedSum = 50057835.96;

        // when
        double actualCompoundedSum = gainsCalculator.calculateCompoundingEffectWithAnnuityPayments(comparisonRequest,
                annuityPayment);

        // then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void shouldCalculateCompoundingEffectWithoutPayments() {
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

        double expectedCompoundedSum = 13455098.37;

        // when
        double actualCompoundedSum = gainsCalculator.calculateCompoundingEffectWithoutPayments(comparisonRequest);

        // then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void calculateCompoundingEffectWithoutFirstPayment() {
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
        double expectedCompoundedSum = 2900925.55;

        // when
        double actualCompoundedSum = gainsCalculator.calculateCompoundingEffectWithoutFirstPayment(comparisonRequest,
                annuityPayment);

        //then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void calculateCompoundingEffectWithoutFirstPaymentWhenRentIsLowerThanAnnuityPayment() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(4200000D,
                3700000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double annuityPayment = 32086.05;
        double expectedCompoundedSum = 5940889.36;

        // when
        double actualCompoundedSum = gainsCalculator.calculateCompoundingEffectWithoutFirstPayment(comparisonRequest,
                annuityPayment);

        //then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void calculateCompoundingEffectWithoutFirstPaymentWhenPayedFullPrice() {
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

        double annuityPayment = 0;
        double expectedCompoundedSum = 12958449.14;

        // when
        double actualCompoundedSum = gainsCalculator.calculateCompoundingEffectWithoutFirstPayment(comparisonRequest,
                annuityPayment);

        //then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void shouldCalculateTotalGainsForRentingWhenPaymentIsMoreThanRent() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(12000000D,
                7000000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double expectedTotalGainsForRenting = 49894466.76;

        // when
        double actualTotalGainsForRenting = gainsCalculator.calculateTotalGainsForRenting(comparisonRequest);

        // then
        assertThat(expectedTotalGainsForRenting).isEqualTo(actualTotalGainsForRenting);
    }

    @Test
    void shouldCalculateTotalGainsForRentingWhenPaymentIsLessThanRent() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(12000000D,
                2000000D,
                20D,
                8.49D,
                30000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double expectedTotalGainsForRenting = 51731429.09;

        // when
        double actualTotalGainsForRenting = gainsCalculator.calculateTotalGainsForRenting(comparisonRequest);

        // then
        assertThat(expectedTotalGainsForRenting).isEqualTo(actualTotalGainsForRenting);
    }

    @Test
    void shouldCalculateFinalRealEstatePrice() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(12000000D,
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

        double expectedFinalPrice = 14642280.48;

        // when
        double actualFinalPrice = gainsCalculator.calculateFinalRealEstatePrice(comparisonRequest);

        // then
        assertThat(expectedFinalPrice).isEqualTo(actualFinalPrice);
    }

    @Test
    void shouldCalculateTotalGainsForBuyingWhenPayedFullPrice() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(12000000D,
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

        double expectedTotalGainsForBuying = 27860729.62;

        //when
        double actualTotalGainsForBuying = gainsCalculator.calculateTotalGainsForBuying(comparisonRequest);

        // then
        assertThat(expectedTotalGainsForBuying).isEqualTo(actualTotalGainsForBuying);
    }

    @Test
    void shouldCalculateTotalGainsForBuyingWhenPaymentIsMoreThanRent() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(12000000D,
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

        double expectedTotalGainsForBuying = 14902280.48;

        //when
        double actualTotalGainsForBuying = gainsCalculator.calculateTotalGainsForBuying(comparisonRequest);

        // then
        assertThat(expectedTotalGainsForBuying).isEqualTo(actualTotalGainsForBuying);
    }

    @Test
    void shouldCalculateTotalGainsForBuying() {
        // given
        ComparisonRequest comparisonRequest = new ComparisonRequest(12000000D,
                10000000D,
                20D,
                8.49D,
                22000D,
                4D,
                500000D,
                8D,
                0.1,
                0.5,
                false);

        double annuityPayment = 30000;
        double expectedTotalGainsForBuying = 14902280.48;

        //when
        double actualTotalGainsForBuying = gainsCalculator.calculateTotalGainsForBuying(comparisonRequest);

        // then
        assertThat(expectedTotalGainsForBuying).isEqualTo(actualTotalGainsForBuying);
    }
}