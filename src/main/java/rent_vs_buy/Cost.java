package rent_vs_buy;

public class Cost extends Balance {

    public Cost(Balance balance) {
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

    public double calculateTotalDifferentiatedPayments() {
        if (isPayedFullPrice()) {
            return 0;
        }
        double loanLeft = getLoanBody();
        double notRoundedResult = 0;

        for (double i = getPeriodInMonths(); i >= 1; i--) {
            double payment = calculateMonthlyDifferentiatedPayment(loanLeft);
            loanLeft = loanLeft - getLoanBody() / getPeriodInMonths();
            notRoundedResult += payment;
        }

        return round(notRoundedResult);
    }

    public double calculateTotalAnnuityPayments(double monthlyPayment) {
        if (isPayedFullPrice()) {
            return 0;
        }
        return round(monthlyPayment * getPeriodInMonths());
    }

    public double calculateRentForWholePeriod() {
        double adjustedMonthlyRent = getMonthlyRent();
        double result = 0;
        for (int i = 1; i <= getPeriodInYears(); i++) {
            result += adjustedMonthlyRent * 12;
            adjustedMonthlyRent += adjustedMonthlyRent * getRentInflationRate() / 100;
        }

        return round(result);
    }

    public double calculateAbsoluteMonthlyPercent(double loanLeft, double monthlyInterestRate) {
        if (isPayedFullPrice()) {
            return 0;
        }
        return loanLeft * monthlyInterestRate;
    }

    public double calculateLoanBodyPayedPerMonth(double monthlyPayment, double absoluteMonthlyPercent) {
        if (isPayedFullPrice()) {
            return 0;
        }
        return monthlyPayment - absoluteMonthlyPercent;
    }

    public double calculateInsurancePaymentForWholePeriod(double insurancePercent) {
        return calculateInsurancePaymentForWholePeriod(0, insurancePercent, true);
    }

    public double calculateInsurancePaymentForWholePeriod(double monthlyPayment, double insurancePercent) {
        return calculateInsurancePaymentForWholePeriod(monthlyPayment, insurancePercent, false);
    }

    public double calculateInsurancePaymentForWholePeriod(double monthlyPayment,
                                                          double insurancePercent,
                                                          boolean differentiatedPayment) {
        if (isPayedFullPrice()) {
            return 0;
        }

        double notRoundedResult = 0;
        double monthlyInterestRate = getMonthlyInterestRate(getCreditInterestRate());
        double loanLeft = getLoanBody();
        for (double i = getPeriodInYears(); i >= 1; i--) {
            double yearPayment = 0;
            notRoundedResult += loanLeft * insurancePercent / 100;
            for (int j = 0; j < 12; j++) {

                if (differentiatedPayment) {
                    monthlyPayment = calculateMonthlyDifferentiatedPayment(loanLeft);
                }

                double absoluteMonthlyPercent = calculateAbsoluteMonthlyPercent(loanLeft, monthlyInterestRate);
                double loanPayedBodyByMonth = calculateLoanBodyPayedPerMonth(monthlyPayment, absoluteMonthlyPercent);
                yearPayment += loanPayedBodyByMonth;
                loanLeft = loanLeft - loanPayedBodyByMonth;
            }
        }

        return round(notRoundedResult);
    }

    public double calculateTaxForWholePeriod(double taxRate) {
        return getFullPrice() * taxRate / 100 * getPeriodInYears();
    }

    public double calculateTotalLossesWithAnnuityPayment(double taxRate,
                                                         double insuranceRate,
                                                         double finalRealEstatePrice) {
        double monthlyPayment = calculateAnnuityPayment();
        double ann = calculateTotalAnnuityPayments(monthlyPayment);
        double tax = calculateTaxForWholePeriod(taxRate);
        double ins = calculateInsurancePaymentForWholePeriod(monthlyPayment, insuranceRate);
        return round(calculateTotalAnnuityPayments(monthlyPayment) + calculateTaxForWholePeriod(taxRate) +
                calculateInsurancePaymentForWholePeriod(monthlyPayment, insuranceRate) +
                getRenovationCost() + getFirstPayment() - finalRealEstatePrice);
    }

    public double calculateTotalLossesWithDifferentiatedPayment(double taxRate,
                                                                double insuranceRate,
                                                                double finalRealEstatePrice) {
        return round(calculateTotalDifferentiatedPayments() + calculateTaxForWholePeriod(taxRate) +
                calculateInsurancePaymentForWholePeriod(insuranceRate) +
                getRenovationCost() + getFirstPayment() - finalRealEstatePrice);
    }

    public double calculateTotalCostsForRenting() {
        return round(calculateRentForWholePeriod());
    }

}
