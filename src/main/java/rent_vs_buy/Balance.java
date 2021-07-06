package rent_vs_buy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Balance {
    private double fullPrice;
    private double loanBody;
    private double firstPayment;
    private double periodInYears;
    private double creditInterestRate;
    private double periodInMonths;
    private double monthlyRent;
    private double rentInflationRate;
    private double renovationCost;
    private double debitInterestRate;
    private boolean payedFullPrice;
    private boolean differentiatedPayment;

    public Balance(double fullPrice,
                   double loanBody,
                   double yearsOfLoan,
                   double creditInterestRate,
                   double monthlyRent,
                   double rentInflationRate,
                   double renovationCost,
                   double debitInterestRate,
                   boolean differentiatedPayment) {

        this.fullPrice = fullPrice;
        this.loanBody = loanBody;
        this.periodInYears = yearsOfLoan;
        this.creditInterestRate = creditInterestRate;
        this.periodInMonths = this.getPeriodInYears() * 12;
        this.monthlyRent = monthlyRent;
        this.rentInflationRate = rentInflationRate;
        this.firstPayment = this.getFullPrice() - this.getLoanBody();
        this.renovationCost = renovationCost;
        this.debitInterestRate = debitInterestRate;
        this.payedFullPrice = this.getFullPrice() - this.getFirstPayment() <= 0;
        this.differentiatedPayment = differentiatedPayment;
    }

    public double getFullPrice() {
        return fullPrice;
    }

    public double getLoanBody() {
        return loanBody;
    }

    public double getFirstPayment() {
        return firstPayment;
    }

    public double getPeriodInYears() {
        return periodInYears;
    }

    public double getCreditInterestRate() {
        return creditInterestRate;
    }

    public double getPeriodInMonths() {
        return periodInMonths;
    }

    public double getMonthlyRent() {
        return monthlyRent;
    }

    public double getRentInflationRate() {
        return rentInflationRate;
    }

    public double getRenovationCost() {
        return renovationCost;
    }

    public double getDebitInterestRate() {
        return debitInterestRate;
    }

    public boolean isPayedFullPrice() {
        return payedFullPrice;
    }

    public boolean isDifferentiatedPayment() {
        return differentiatedPayment;
    }

    protected double round(double value) {
        BigDecimal result = new BigDecimal(Double.toString(value));
        return result.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    protected double getMonthlyInterestRate(double interestRate) {
        return interestRate / 100 / 12;
    }

    public double calculateAnnuityPayment() {
        if (isPayedFullPrice()) {
            return getMonthlyRent();
        }
        double monthlyInterestRate = getMonthlyInterestRate(getCreditInterestRate());
        double notRoundedResult = getLoanBody() * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, getPeriodInMonths()))
                / (Math.pow(1 + monthlyInterestRate, getPeriodInMonths()) - 1);
        return round(notRoundedResult);
    }

    public double calculateMonthlyDifferentiatedPayment(double loanLeft) {
        double monthlyInterestRate = getMonthlyInterestRate(getCreditInterestRate());
        return round(getLoanBody() / getPeriodInMonths() + loanLeft * monthlyInterestRate);
    }

    public double calculateBalance(double totalLoss, double totalGains) { return totalGains - totalLoss; }


}
