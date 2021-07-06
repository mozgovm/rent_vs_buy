package rent_vs_buy;

import org.junit.jupiter.api.Test;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.entry;

public class GainTests {
    @Test
    void maxTaxDeductionTest() {
        Balance balance = new Balance(6000000,
                5400000,
                25,
                6.49F,
                25000,
                4F,
                1500000,
                8,
                false);
        // given
        Gain gain = new Gain(balance);
        double fullPrice = 5000000.0;
        double expectedResult = 260000.0;

        // when
        double actualResult = gain.calculateTaxDeduction(fullPrice);

        // then
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void taxDeductionTest() {
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
        Gain gain = new Gain(balance);
        double fullPrice = 1000000.0;
        double expectedResult = 130000.00;

        // when
        double actualResult = gain.calculateTaxDeduction(fullPrice);

        // then
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void shouldCalculateCompoundingEffectWithAnnuityPayments() {
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
        Gain gain = new Gain(balance);
        double annuityPayment = 36427.45;
        double expectedCompoundedSum = 50057835.96;

        // when
        double actualCompoundedSum = gain.calculateCompoundingEffectWithAnnuityPayments(annuityPayment);

        // then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void shouldCalculateCompoundingEffectWithoutPayments() {
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
        Gain gain = new Gain(balance);
        double expectedCompoundedSum = 13455098.37;

        // when
        double actualCompoundedSum = gain.calculateCompoundingEffectWithoutPayments();

        // then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void calculateCompoundingEffectWithoutFirstPayment() {
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
        Gain gain = new Gain(balance);
        double annuityPayment = 17075.0;
        double expectedCompoundedSum = 2900925.55;

        // when
        double actualCompoundedSum = gain.calculateCompoundingEffectWithoutFirstPayment(annuityPayment);

        //then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void calculateCompoundingEffectWithoutFirstPaymentWhenRentIsLowerThanAnnuityPayment() {
        // given
        Balance balance = new Balance(4200000,
                3700000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Gain gain = new Gain(balance);
        double annuityPayment = 32086.05;
        double expectedCompoundedSum = 5940889.36;

        // when
        double actualCompoundedSum = gain.calculateCompoundingEffectWithoutFirstPayment(annuityPayment);

        //then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void calculateCompoundingEffectWithoutFirstPaymentWhenPayedFullPrice() {
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
        Gain gain = new Gain(balance);
        double annuityPayment = 0;
        double expectedCompoundedSum = 12958449.14;

        // when
        double actualCompoundedSum = gain.calculateCompoundingEffectWithoutFirstPayment(annuityPayment);

        //then
        assertThat(expectedCompoundedSum).isEqualTo(actualCompoundedSum);
    }

    @Test
    void shouldCalculateTotalGainsForRentingWhenPaymentIsMoreThanRent() {
        // given
        Balance balance = new Balance(12000000,
                2000000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Gain gain = new Gain(balance);
        double annuityPayment = 45000.0;
        double expectedTotalGainsForRenting = 65278898.65;

        // when
        double actualTotalGainsForRenting = gain.calculateTotalGainsForRenting(annuityPayment);

        // then
        assertThat(expectedTotalGainsForRenting).isEqualTo(actualTotalGainsForRenting);
    }

    @Test
    void shouldCalculateTotalGainsForRentingWhenPaymentIsLessThanRent() {
        // given
        Balance balance = new Balance(12000000,
                2000000,
                20,
                8.49F,
                30000,
                4F,
                500000,
                8,
                false);
        Gain gain =new Gain(balance);
        double annuityPayment = 22000.0;
        double expectedTotalGainsForRenting = 51731429.09;

        // when
        double actualTotalGainsForRenting = gain.calculateTotalGainsForRenting(annuityPayment);

        // then
        assertThat(expectedTotalGainsForRenting).isEqualTo(actualTotalGainsForRenting);
    }

    @Test
    void shouldCalculateFinalRealEstatePrice() {
        // given
        Balance balance = new Balance(12000000,
                0,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Gain gain = new Gain(balance);
        double expectedFinalPrice = 14642280.48;

        // when
        double actualFinalPrice = gain.calculateFinalRealEstatePrice();

        // then
        assertThat(expectedFinalPrice).isEqualTo(actualFinalPrice);
    }

    @Test
    void shouldCalculateTotalGainsForBuyingWhenPayedFullPrice() {
        // given
        Balance balance = new Balance(12000000,
                0,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Gain gain = new Gain(balance);
        double annuityPayment = 17074.0;
        double expectedTotalGainsForBuying = 27860729.62;

        //when
        double actualTotalGainsForBuying = gain.calculateTotalGainsForBuying(annuityPayment);

        // then
        assertThat(expectedTotalGainsForBuying).isEqualTo(actualTotalGainsForBuying);
    }

    @Test
    void shouldCalculateTotalGainsForBuyingWhenPaymentIsMoreThanRent() {
        // given
        Balance balance = new Balance(12000000,
                0,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Gain gain = new Gain(balance);
        double annuityPayment = 30000;
        double expectedTotalGainsForBuying = 19614443.8;

        //when
        double actualTotalGainsForBuying = gain.calculateTotalGainsForBuying(annuityPayment);

        // then
        assertThat(expectedTotalGainsForBuying).isEqualTo(actualTotalGainsForBuying);
    }

    @Test
    void shouldCalculateTotalGainsForBuying() {
        // given
        Balance balance = new Balance(12000000,
                10000000,
                20,
                8.49F,
                22000,
                4F,
                500000,
                8,
                false);
        Gain gain = new Gain(balance);
        double annuityPayment = 30000;
        double expectedTotalGainsForBuying = 14902280.48;

        //when
        double actualTotalGainsForBuying = gain.calculateTotalGainsForBuying(annuityPayment);

        // then
        assertThat(expectedTotalGainsForBuying).isEqualTo(actualTotalGainsForBuying);
    }
}
