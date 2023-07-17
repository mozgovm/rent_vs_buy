package rent_vs_buy.balancecalculator;

import org.springframework.stereotype.Component;
import rent_vs_buy.models.ComparisonRequest;

@Component
public class CostsCalculator extends BalanceCalculator {

    public double calculateTotalDifferentiatedPayments(ComparisonRequest comparisonRequest) {
        if (fullPriceWasPayed(comparisonRequest)) {
            return 0;
        }
        double loanLeft = comparisonRequest.getLoanBody();
        double notRoundedResult = 0;

        for (double i = calculatePeriodInMonths(comparisonRequest); i >= 1; i--) {
            double payment = calculateMonthlyDifferentiatedPayment(comparisonRequest, loanLeft);
            loanLeft = loanLeft - comparisonRequest.getLoanBody() / calculatePeriodInMonths(comparisonRequest);
            notRoundedResult += payment;
        }

        return round(notRoundedResult);
    }

    public double calculateTotalAnnuityPayments(ComparisonRequest comparisonRequest, double monthlyPayment) {
        if (fullPriceWasPayed(comparisonRequest)) {
            return 0;
        }
        return round(monthlyPayment * calculatePeriodInMonths(comparisonRequest));
    }

    public double calculateRentForWholePeriod(ComparisonRequest comparisonRequest) {
        double adjustedMonthlyRent = comparisonRequest.getMonthlyRent();
        double result = 0;
        for (int i = 1; i <= comparisonRequest.getYearsOfLoan(); i++) {
            result += adjustedMonthlyRent * 12;
            adjustedMonthlyRent += adjustedMonthlyRent * comparisonRequest.getRentInflationRate() / 100;
        }

        return round(result);
    }

    public double calculateAbsoluteMonthlyPercent(ComparisonRequest comparisonRequest, double loanLeft,
                                                  double monthlyInterestRate) {
        if (fullPriceWasPayed(comparisonRequest)) {
            return 0;
        }
        return loanLeft * monthlyInterestRate;
    }

    public double calculateLoanBodyPayedPerMonth(ComparisonRequest comparisonRequest, double monthlyPayment,
                                                 double absoluteMonthlyPercent) {
        if (fullPriceWasPayed(comparisonRequest)) {
            return 0;
        }
        return monthlyPayment - absoluteMonthlyPercent;
    }

    public double calculateInsurancePaymentForWholePeriod(ComparisonRequest comparisonRequest,
                                                          double insurancePercent) {
        return calculateInsurancePaymentForWholePeriod(comparisonRequest,0, insurancePercent, true);
    }

    public double calculateInsurancePaymentForWholePeriod(ComparisonRequest comparisonRequest, double monthlyPayment,
                                                          double insurancePercent) {
        return calculateInsurancePaymentForWholePeriod(comparisonRequest, monthlyPayment, insurancePercent, false);
    }

    public double calculateInsurancePaymentForWholePeriod(ComparisonRequest comparisonRequest,
                                                          double monthlyPayment,
                                                          double insurancePercent,
                                                          boolean differentiatedPayment) {
        if (fullPriceWasPayed(comparisonRequest)) {
            return 0;
        }

        double notRoundedResult = 0;
        double monthlyInterestRate = getMonthlyInterestRate(comparisonRequest.getCreditInterestRate());
        double loanLeft = comparisonRequest.getLoanBody();
        for (double i = comparisonRequest.getYearsOfLoan(); i >= 1; i--) {
            notRoundedResult += loanLeft * insurancePercent / 100;
            for (int j = 0; j < 12; j++) {

                if (differentiatedPayment) {
                    monthlyPayment = calculateMonthlyDifferentiatedPayment(comparisonRequest, loanLeft);
                }

                double absoluteMonthlyPercent = calculateAbsoluteMonthlyPercent(comparisonRequest, loanLeft,
                        monthlyInterestRate);
                double loanPayedBodyByMonth = calculateLoanBodyPayedPerMonth(comparisonRequest, monthlyPayment,
                        absoluteMonthlyPercent);
                loanLeft = loanLeft - loanPayedBodyByMonth;
            }
        }

        return round(notRoundedResult);
    }

    public double calculateTaxForWholePeriod(ComparisonRequest comparisonRequest, double taxRate) {
        return comparisonRequest.getFullPrice() * taxRate / 100 * comparisonRequest.getYearsOfLoan();
    }

    public double calculateTotalLossesWithAnnuityPayment(ComparisonRequest comparisonRequest,
                                                         double finalRealEstatePrice) {
        double monthlyPayment = calculateAnnuityPayment(comparisonRequest);
        double annuityPayments = calculateTotalAnnuityPayments(comparisonRequest, monthlyPayment);
        double taxForWholePeriod = calculateTaxForWholePeriod(comparisonRequest, comparisonRequest.getTaxRate());
        double insurancePaymentForWholePeriod = calculateInsurancePaymentForWholePeriod(comparisonRequest,
                monthlyPayment, comparisonRequest.getInsuranceRate());
        return round(annuityPayments + taxForWholePeriod + insurancePaymentForWholePeriod +
                comparisonRequest.getRenovationCost() + calculateFirstPayment(comparisonRequest)
                - finalRealEstatePrice);
    }

    public double calculateTotalLossesWithDifferentiatedPayment(ComparisonRequest comparisonRequest,
                                                                double finalRealEstatePrice) {
        return round(calculateTotalDifferentiatedPayments(comparisonRequest) +
                calculateTaxForWholePeriod(comparisonRequest, comparisonRequest.getTaxRate()) +
                calculateInsurancePaymentForWholePeriod(comparisonRequest, comparisonRequest.getInsuranceRate()) +
                comparisonRequest.getRenovationCost() + calculateFirstPayment(comparisonRequest) - finalRealEstatePrice);
    }

    public double calculateTotalCostsForRenting(ComparisonRequest comparisonRequest) {
        return round(calculateRentForWholePeriod(comparisonRequest));
    }
}
