package rent_vs_buy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import rent_vs_buy.utils.Utils;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = Controller.class)
@ActiveProfiles("test")
public class ControllerTests {
    private Utils utils = new Utils();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Main app;

    @Test
    void compareRentAndBuy() throws Exception {
        String requestFilePath = "/ControllerTests/compareRentAndBuy/compareRentAndBuyRequest.json";
        String responseFilePath = "/ControllerTests/compareRentAndBuy/compareRentAndBuyResponse.json";
        String requestBody = utils.getFileContent(requestFilePath);
        String responseBody = utils.getFileContent(responseFilePath);

        mockMvc.perform(post("/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody, false));
    }

    @Test
    void compareRentAndBuyWithDifferentiatedPayment() throws Exception {
        String requestFilePath = "/ControllerTests/compareRentAndBuyWithDifferentiatedPayment/compareRentAndBuyRequest.json";
        String responseFilePath = "/ControllerTests/compareRentAndBuyWithDifferentiatedPayment/compareRentAndBuyResponse.json";
        String requestBody = utils.getFileContent(requestFilePath);
        String responseBody = utils.getFileContent(responseFilePath);

        mockMvc.perform(post("/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody, false));
    }

    @Test
    void compareRentAndBuyWithEmptyJsonObject() throws Exception {
        String errorsPrefix = "$.errors.";

        mockMvc.perform(post("/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath(errorsPrefix + "taxRate",
                        is("Tax rate should not be empty")))
                .andExpect(jsonPath(errorsPrefix + "isDifferentiatedPayment",
                        is("Should provide information about payment type")))
                .andExpect(jsonPath(errorsPrefix + "loanBody",
                        is("Loan body should not be empty")))
                .andExpect(jsonPath(errorsPrefix + "debitInterestRate",
                        is("Debit interest rate should not be empty")))
                .andExpect(jsonPath(errorsPrefix + "insuranceRate",
                        is("Insurance rate should not be empty")))
                .andExpect(jsonPath(errorsPrefix + "rentInflationRate",
                        is("Rent inflation rate should not be empty")))
                .andExpect(jsonPath(errorsPrefix + "renovationCost",
                        is("Renovation cost should not be empty")))
                .andExpect(jsonPath(errorsPrefix + "fullPrice",
                        is("Full price should not be empty")))
                .andExpect(jsonPath(errorsPrefix + "monthlyRent",
                        is("Monthly rent should not be empty")))
                .andExpect(jsonPath(errorsPrefix + "creditInterestRate",
                        is("Credit interest rate should not be empty")))
                .andExpect(jsonPath(errorsPrefix + "yearsOfLoan",
                        is("Years of loan  should not be empty")));
    }

    @Test
    void compareRentAndBuyWithEmptyBody() throws Exception {
        mockMvc.perform(post("/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }
}
