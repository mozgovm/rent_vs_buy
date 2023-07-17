package rent_vs_buy.balancecalculator;

import org.springframework.stereotype.Component;
import rent_vs_buy.models.ComparisonRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BalanceCalculator {

    public double round(double value) {
        BigDecimal result = new BigDecimal(Double.toString(value));
        return result.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public double getMonthlyInterestRate(double interestRate) {
        return interestRate / 100 / 12;
    }

    protected Double calculateFirstPayment(ComparisonRequest comparisonRequest) {
        return comparisonRequest.getFullPrice() - comparisonRequest.getLoanBody();
    }

    protected boolean fullPriceWasPayed(ComparisonRequest comparisonRequest) {
        return comparisonRequest.getFullPrice() - calculateFirstPayment(comparisonRequest) <= 0;
    }

    protected Double calculatePeriodInMonths(ComparisonRequest comparisonRequest) {
        return comparisonRequest.getYearsOfLoan() * 12;
    }

    public double calculateAnnuityPayment(ComparisonRequest comparisonRequest) {
        if (fullPriceWasPayed(comparisonRequest)) {
            return comparisonRequest.getMonthlyRent();
        }
        double monthlyInterestRate = getMonthlyInterestRate(comparisonRequest.getCreditInterestRate());
        double notRoundedResult = comparisonRequest.getLoanBody() * (monthlyInterestRate *
                Math.pow(1 + monthlyInterestRate, calculatePeriodInMonths(comparisonRequest)))
                / (Math.pow(1 + monthlyInterestRate, calculatePeriodInMonths(comparisonRequest)) - 1);
        return round(notRoundedResult);
    }

    public double calculateMonthlyDifferentiatedPayment(ComparisonRequest comparisonRequest, double loanLeft) {
        double monthlyInterestRate = getMonthlyInterestRate(comparisonRequest.getCreditInterestRate());
        return round(comparisonRequest.getLoanBody() / calculatePeriodInMonths(comparisonRequest)
                + loanLeft * monthlyInterestRate);
    }

    public double calculateBalance(double totalLoss, double totalGains) { return totalGains - totalLoss; }
}
