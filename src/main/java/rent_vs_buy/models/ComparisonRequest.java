package rent_vs_buy.models;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class ComparisonRequest {
    @NotNull(message = "Full price should not be empty")
    @PositiveOrZero
    private Double fullPrice;
    @NotNull(message = "Loan body should not be empty")
    @PositiveOrZero
    private Double loanBody;
    @NotNull(message = "Years of loan  should not be empty")
    @PositiveOrZero
    private Double yearsOfLoan;
    @NotNull(message = "Credit interest rate should not be empty")
    @PositiveOrZero
    private Double creditInterestRate;
    @NotNull(message = "Monthly rent should not be empty")
    @PositiveOrZero
    private Double monthlyRent;
    @NotNull(message = "Rent inflation rate should not be empty")
    private Double rentInflationRate;
    @NotNull(message = "Renovation cost should not be empty")
    @PositiveOrZero
    private Double renovationCost;
    @NotNull(message = "Debit interest rate should not be empty")
    private Double debitInterestRate;
    @NotNull(message = "Tax rate should not be empty")
    @PositiveOrZero
    private Double taxRate;
    @NotNull(message = "Insurance rate should not be empty")
    @PositiveOrZero
    private Double insuranceRate;
    @NotNull(message = "Should provide information about payment type")
    private Boolean isDifferentiatedPayment;

    public ComparisonRequest(Double fullPrice, Double loanBody, Double yearsOfLoan, Double creditInterestRate, Double monthlyRent, Double rentInflationRate, Double renovationCost, Double debitInterestRate, Double taxRate, Double insuranceRate, Boolean isDifferentiatedPayment) {
        this.fullPrice = fullPrice;
        this.loanBody = loanBody;
        this.yearsOfLoan = yearsOfLoan;
        this.creditInterestRate = creditInterestRate;
        this.monthlyRent = monthlyRent;
        this.rentInflationRate = rentInflationRate;
        this.renovationCost = renovationCost;
        this.debitInterestRate = debitInterestRate;
        this.taxRate = taxRate;
        this.insuranceRate = insuranceRate;
        this.isDifferentiatedPayment = isDifferentiatedPayment;
    }

    public double getFullPrice() {
        return fullPrice;
    }

    public double getLoanBody() {
        return loanBody;
    }

    public double getYearsOfLoan() {
        return yearsOfLoan;
    }

    public double getCreditInterestRate() {
        return creditInterestRate;
    }

    public double getMonthlyRent() { return monthlyRent; }

    public double getRentInflationRate() {
        return rentInflationRate;
    }

    public double getRenovationCost() {
        return renovationCost;
    }

    public double getDebitInterestRate() {
        return debitInterestRate;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getInsuranceRate() { return insuranceRate; }

    public boolean getIsDifferentiatedPayment() {
        return isDifferentiatedPayment;
    }
}
