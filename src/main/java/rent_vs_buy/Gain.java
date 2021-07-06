package rent_vs_buy;

public class Gain extends Balance {

    public Gain(Balance balance) {
        super(balance.getFullPrice(),
                balance.getLoanBody(),
                balance.getPeriodInYears(),
                balance.getCreditInterestRate(),
                balance.getMonthlyRent(),
                balance.getRentInflationRate(),
                balance.getRenovationCost(),
                balance.getDebitInterestRate(),
                balance.isDifferentiatedPayment());
    }

    public double calculateTaxDeduction(double fullPrice) {
        long maxDeductibleSum = 2000000;
        if (fullPrice > maxDeductibleSum) { return maxDeductibleSum * 13 / 100; }

        return fullPrice * 13 / 100;
    }

    public double calculateCompoundingEffectWithAnnuityPayments(double annuityPayment) {

        double result = round(annuityPayment *
                (Math.pow((1 + getDebitInterestRate() / 100 / 12), getPeriodInMonths()) - 1) *
                12 / (getDebitInterestRate() / 100) +
                (getFirstPayment() + getRenovationCost()) * Math.pow((1 + getDebitInterestRate() / 100 / 12), getPeriodInMonths()));
        return result;
    }

    public double calculateCompoundingEffectWithoutPayments() {
        return round((getFirstPayment() + getRenovationCost())
                * Math.pow((1 + getDebitInterestRate() / 100 / 12), getPeriodInMonths()));
    }

    public double calculateCompoundingEffectWithoutFirstPayment(double annuityPayment) {
        double replenishmentPayment = getMonthlyRent() - annuityPayment;
        if (isPayedFullPrice()) { replenishmentPayment = getMonthlyRent(); }

        if (annuityPayment > getMonthlyRent()) {
            replenishmentPayment = annuityPayment - getMonthlyRent();
        }

        return round(replenishmentPayment *
                (Math.pow((1 + getDebitInterestRate() / 100 / 12), getPeriodInMonths()) - 1) *
                12 / (getDebitInterestRate() / 100));
    }

    public double calculateTotalGainsForRenting(double annuityPayment) {
        if (annuityPayment > getMonthlyRent()) {
            return round(calculateCompoundingEffectWithoutFirstPayment(annuityPayment) +
                    calculateCompoundingEffectWithoutPayments());
        }

        return round(calculateCompoundingEffectWithoutPayments());
    }

    public double calculateFinalRealEstatePrice() {
        return round(getFullPrice() * Math.pow(1 + 1.00 / 100 / 1, getPeriodInYears()));
    }

    public double calculateTotalGainsForBuying(double annuityPayment) {
        if (isPayedFullPrice() || annuityPayment < getMonthlyRent()) {
            return round(calculateTaxDeduction(getFullPrice()) +
                    calculateCompoundingEffectWithoutFirstPayment(annuityPayment) +
                    calculateFinalRealEstatePrice());
        }

        return round(calculateTaxDeduction(getFullPrice()) + calculateFinalRealEstatePrice());

    }
}
