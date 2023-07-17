package rent_vs_buy.balancecalculator;

import org.springframework.stereotype.Component;
import rent_vs_buy.models.ComparisonRequest;

@Component
public class GainsCalculator extends BalanceCalculator {
    public double calculateTaxDeduction(double fullPrice) {
        long maxDeductibleSum = 2000000;
        if (fullPrice > maxDeductibleSum) { return (double) (maxDeductibleSum * 13) / 100; }

        return fullPrice * 13 / 100;
    }

    public double calculateCompoundingEffectWithAnnuityPayments(ComparisonRequest comparisonRequest,
                                                                double annuityPayment) {

        double result = round(annuityPayment *
                (Math.pow((1 + comparisonRequest.getDebitInterestRate() / 100 / 12),
                        calculatePeriodInMonths(comparisonRequest)) - 1) *
                12 / (comparisonRequest.getDebitInterestRate() / 100) +
                (calculateFirstPayment(comparisonRequest) + comparisonRequest.getRenovationCost())
                        * Math.pow((1 + comparisonRequest.getDebitInterestRate() / 100 / 12),
                        calculatePeriodInMonths(comparisonRequest)));
        return result;
    }

    public double calculateCompoundingEffectWithoutPayments(ComparisonRequest comparisonRequest) {
        return round((calculateFirstPayment(comparisonRequest) + comparisonRequest.getRenovationCost())
                * Math.pow((1 + comparisonRequest.getDebitInterestRate() / 100 / 12),
                calculatePeriodInMonths(comparisonRequest)));
    }

    public double calculateCompoundingEffectWithoutFirstPayment(ComparisonRequest comparisonRequest,
                                                                double annuityPayment) {
        double replenishmentPayment = comparisonRequest.getMonthlyRent() - annuityPayment;
        if (fullPriceWasPayed(comparisonRequest)) { replenishmentPayment = comparisonRequest.getMonthlyRent(); }

        if (annuityPayment > comparisonRequest.getMonthlyRent()) {
            replenishmentPayment = annuityPayment - comparisonRequest.getMonthlyRent();
        }

        return round(replenishmentPayment *
                (Math.pow((1 + comparisonRequest.getDebitInterestRate() / 100 / 12),
                        calculatePeriodInMonths(comparisonRequest)) - 1) *
                12 / (comparisonRequest.getDebitInterestRate() / 100));
    }

    public double calculateTotalGainsForRenting(ComparisonRequest comparisonRequest) {
        double annuityPayment = calculateAnnuityPayment(comparisonRequest);
        if (annuityPayment > comparisonRequest.getMonthlyRent()) {
            return round(calculateCompoundingEffectWithoutFirstPayment(comparisonRequest, annuityPayment) +
                    calculateCompoundingEffectWithoutPayments(comparisonRequest));
        }

        return round(calculateCompoundingEffectWithoutPayments(comparisonRequest));
    }

    public double calculateFinalRealEstatePrice(ComparisonRequest comparisonRequest) {
        return round(comparisonRequest.getFullPrice() * Math.pow(1 + 1.00 / 100 / 1,
                comparisonRequest.getYearsOfLoan()));
    }

    public double calculateTotalGainsForBuying(ComparisonRequest comparisonRequest) {
        double annuityPayment = calculateAnnuityPayment(comparisonRequest);
        if (fullPriceWasPayed(comparisonRequest) || annuityPayment < comparisonRequest.getMonthlyRent()) {
            return round(calculateTaxDeduction(comparisonRequest.getFullPrice()) +
                    calculateCompoundingEffectWithoutFirstPayment(comparisonRequest, annuityPayment) +
                    calculateFinalRealEstatePrice(comparisonRequest));
        }

        return round(calculateTaxDeduction(comparisonRequest.getFullPrice())
                + calculateFinalRealEstatePrice(comparisonRequest));

    }
}
