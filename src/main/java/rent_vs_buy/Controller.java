package rent_vs_buy;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import rent_vs_buy.models.compareModel;

import javax.validation.Valid;
import java.util.*;

@RestController
public class Controller {
    @RequestMapping(method = RequestMethod.POST, produces = "application/json",
            value = "/compare")
    public LinkedHashMap compareRentAndBuy(@Valid @RequestBody compareModel body) {

        Balance balance = new Balance(body.getFullPrice(),
                body.getLoanBody(),
                body.getYearsOfLoan(),
                body.getCreditInterestRate(),
                body.getMonthlyRent(),
                body.getRentInflationRate(),
                body.getRenovationCost(),
                body.getDebitInterestRate(),
                body.getIsDifferentiatedPayment());

        Cost cost = new Cost(balance);
        Gain gain = new Gain(balance);

        double payment = cost.calculateAnnuityPayment();
        double finalRealEstatePrice = gain.calculateFinalRealEstatePrice();
        double totalRentCosts = cost.calculateTotalCostsForRenting();
        double totalRentGains = gain.calculateTotalGainsForRenting(gain.calculateAnnuityPayment());
        double totalAnnuityCosts = cost.calculateTotalLossesWithAnnuityPayment(body.getTaxRate(),
                body.getInsuranceRate(), finalRealEstatePrice);
        double totalBuyGains = gain.calculateTotalGainsForBuying(gain.calculateAnnuityPayment());
        double totalDifferentiatedCosts = cost.calculateTotalLossesWithDifferentiatedPayment(body.getTaxRate(),
                body.getInsuranceRate(), finalRealEstatePrice);

        LinkedHashMap<String, Double> res = new LinkedHashMap<>();
        res.put("sumOfAnnuityPayments", cost.calculateTotalAnnuityPayments(payment));
        res.put("sumOfDifferentiatedPayments", cost.calculateTotalDifferentiatedPayments());
        res.put("totalAnnuityCosts", totalAnnuityCosts);
        res.put("totalDifferentiatedCosts", totalDifferentiatedCosts);
        res.put("payedForRent", cost.calculateRentForWholePeriod());
        res.put("totalRentCosts", totalRentCosts);
        res.put("totalRentGains", totalRentGains);
        res.put("totalBuyGains", totalBuyGains);
        res.put("finalRealEstatePrice", gain.calculateFinalRealEstatePrice());
        res.put("rentBalance", balance.calculateBalance(totalRentCosts, totalRentGains));
        res.put("buyBalance", balance.calculateBalance(totalAnnuityCosts, totalBuyGains));
        if (body.getIsDifferentiatedPayment()) {
            res.put("buyBalance", balance.calculateBalance(totalDifferentiatedCosts, totalBuyGains));
        }

        return res;
    }

    @ControllerAdvice
    public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

        // error handle for @Valid
        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatus status, WebRequest request) {

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
