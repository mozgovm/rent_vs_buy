package rent_vs_buy.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import rent_vs_buy.balancecalculator.BalanceCalculator;
import rent_vs_buy.balancecalculator.CostsCalculator;
import rent_vs_buy.balancecalculator.GainsCalculator;
import rent_vs_buy.models.ComparisonRequest;
import rent_vs_buy.models.ComparisonResponse;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class CompareController {

    private final BalanceCalculator balanceCalculator;
    private final CostsCalculator costsCalculator;
    private final GainsCalculator gainsCalculator;

    public CompareController(BalanceCalculator balanceCalculator, CostsCalculator costsCalculator,
                             GainsCalculator gainsCalculator) {
        this.balanceCalculator = balanceCalculator;
        this.costsCalculator = costsCalculator;
        this.gainsCalculator = gainsCalculator;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json",
            value = "/compare")
    public ComparisonResponse compareRentAndBuy(@Valid @RequestBody ComparisonRequest body) {
        double payment = costsCalculator.calculateAnnuityPayment(body);
        double finalRealEstatePrice = gainsCalculator.calculateFinalRealEstatePrice(body);
        double totalRentCosts = costsCalculator.calculateTotalCostsForRenting(body);
        double totalRentGains = gainsCalculator
                .calculateTotalGainsForRenting(body);
        double totalAnnuityCosts = costsCalculator.calculateTotalLossesWithAnnuityPayment(body, finalRealEstatePrice);
        double totalBuyGains = gainsCalculator.calculateTotalGainsForBuying(body);
        double totalDifferentiatedCosts = costsCalculator.calculateTotalLossesWithDifferentiatedPayment(body,
                finalRealEstatePrice);
        double buyBalance;
        if (body.getIsDifferentiatedPayment()) {
            buyBalance = balanceCalculator.calculateBalance(totalDifferentiatedCosts, totalBuyGains);
        } else {
            buyBalance = balanceCalculator.calculateBalance(totalAnnuityCosts, totalBuyGains);
        }

        return new ComparisonResponse(
                costsCalculator.calculateTotalAnnuityPayments(body, payment),
                costsCalculator.calculateTotalDifferentiatedPayments(body),
                totalAnnuityCosts,
                totalDifferentiatedCosts,
                costsCalculator.calculateRentForWholePeriod(body),
                totalRentCosts,
                totalRentGains,
                totalBuyGains,
                gainsCalculator.calculateFinalRealEstatePrice(body),
                balanceCalculator.calculateBalance(totalRentCosts, totalRentGains),
                buyBalance
        );
    }

    @ControllerAdvice
    public static class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

        // error handle for @Valid
        @Override
        protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                               @NotNull HttpHeaders headers,
                                                                               HttpStatus status,
                                                                               @NotNull WebRequest request) {

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", new Date());
            body.put("status", status.value());

            //Get all errors
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });

            body.put("errors", errors);

            return new ResponseEntity<>(body, headers, status);

        }
    }
}
