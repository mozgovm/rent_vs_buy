package rent_vs_buy.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComparisonResponse {
    private double sumOfAnnuityPayments;
    private double sumOfDifferentiatedPayments;
    private double totalAnnuityCosts;
    private double totalDifferentiatedCosts;
    private double payedForRent;
    private double totalRentCosts;
    private double totalRentGains;
    private double totalBuyGains;
    private double finalRealEstatePrice;
    private double rentBalance;
    private double buyBalance;

    public ComparisonResponse(double sumOfAnnuityPayments,
                              double sumOfDifferentiatedPayments,
                              double totalAnnuityCosts,
                              double totalDifferentiatedCosts,
                              double payedForRent,
                              double totalRentCosts,
                              double totalRentGains,
                              double totalBuyGains,
                              double finalRealEstatePrice,
                              double rentBalance,
                              double buyBalance) {
        this.sumOfAnnuityPayments = sumOfAnnuityPayments;
        this.sumOfDifferentiatedPayments = sumOfDifferentiatedPayments;
        this.totalAnnuityCosts = totalAnnuityCosts;
        this.totalDifferentiatedCosts = totalDifferentiatedCosts;
        this.payedForRent = payedForRent;
        this.totalRentCosts = totalRentCosts;
        this.totalRentGains = totalRentGains;
        this.totalBuyGains = totalBuyGains;
        this.finalRealEstatePrice = finalRealEstatePrice;
        this.rentBalance = rentBalance;
        this.buyBalance = buyBalance;
    }

    public double getSumOfAnnuityPayments() {
        return sumOfAnnuityPayments;
    }

    public double getSumOfDifferentiatedPayments() {
        return sumOfDifferentiatedPayments;
    }

    public double getTotalAnnuityCosts() {
        return totalAnnuityCosts;
    }

    public double getTotalDifferentiatedCosts() {
        return totalDifferentiatedCosts;
    }

    public double getPayedForRent() {
        return payedForRent;
    }

    public double getTotalRentCosts() {
        return totalRentCosts;
    }

    public double getTotalRentGains() {
        return totalRentGains;
    }

    public double getTotalBuyGains() {
        return totalBuyGains;
    }

    public double getFinalRealEstatePrice() {
        return finalRealEstatePrice;
    }

    public double getRentBalance() {
        return rentBalance;
    }

    public double getBuyBalance() {
        return buyBalance;
    }
}
