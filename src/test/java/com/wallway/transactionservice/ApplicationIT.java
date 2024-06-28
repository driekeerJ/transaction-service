package com.wallway.transactionservice;

import static com.wallway.transactionservice.ApplicationITTestHelper.AMOUNT;
import static com.wallway.transactionservice.ApplicationITTestHelper.MAIL;
import static com.wallway.transactionservice.ApplicationITTestHelper.NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ApplicationIT {

    private static final String UNKNOWN_ID = "d71499c0-10f1-4973-90c5-bda8585235a0";
    private static final String ERROR_TITLE = "Oops, something happened";
    private static final String ACCOUNT_NAME = "Test Name";
    private static final String ACCOUNT_EMAIL = "test@example.com";
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    ApplicationITTestHelper testHelper;

    @Test
    void when_the_rood_url_is_called_then_the_index_page_is_returned() throws Exception {
        final var htmlResult = getHtmlResult("/");
        assertThat(htmlResult).doesNotContain(ERROR_TITLE);
        assertThat(htmlResult).contains("Transaction service");
    }

    @Test
    void when_get_accounts_is_called_then_the_account_page_is_returned() throws Exception {
        final var contentString = getHtmlResult("/accounts");
        assertThat(contentString).doesNotContain(ERROR_TITLE);
        assertThat(contentString).contains("What would you like to do?");
    }

    @Test
    void when_accounts_get_is_called_then_the_get_accounts_page_is_loaded() throws Exception {
        final var contentString = getHtmlResult("/accounts/get");
        assertThat(contentString).doesNotContain(ERROR_TITLE);
        assertThat(contentString).contains("Get account");
    }

    @Test
    void when_accounts_add_is_called_then_the_add_accounts_page_is_loaded() throws Exception {
        final var contentString = getHtmlResult("/accounts/add");
        assertThat(contentString).doesNotContain(ERROR_TITLE);
        assertThat(contentString).contains("Add account");
    }

    @Test
    void when_account_details_for_a_known_account_is_called_then_the_account_details_are_shown() throws Exception {
        final UUID uuid = testHelper.saveAccount(AMOUNT);
        final var contentString = getHtmlResult(String.format("/accounts/%s", uuid));
        assertThat(contentString).doesNotContain(ERROR_TITLE);
        assertThat(contentString).contains("Account details");
        assertThat(contentString).contains(NAME);
        assertThat(contentString).contains(AMOUNT.toString());
        assertThat(contentString).contains(MAIL);
    }

    @Test
    void when_account_details_is_called_for_a_unknown_account_then_the_error_page_is_shown() throws Exception {
        final var contentString = getHtmlResult(String.format("/accounts/%s", UNKNOWN_ID));
        assertThat(contentString).contains(ERROR_TITLE);
        assertThat(contentString).contains(String.format("The account for %s was not found", UNKNOWN_ID));
    }

    @Test
    void when_add_account_is_called_then_the_account_is_created_and_redirected() throws Exception {
        final ResultActions resultActions = postAccount("Unique name", "Unique email");

        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/accounts/*"));
    }

    @Test
    void when_an_account_is_being_created_that_already_exists_then_the_error_page_is_shown() throws Exception {
        final ResultActions resultActions = postAccount(ACCOUNT_NAME, ACCOUNT_EMAIL);
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/accounts/*"));


        final String content = postAccount(ACCOUNT_NAME, ACCOUNT_EMAIL).andReturn().getResponse().getContentAsString();
        assertThat(content).contains(ERROR_TITLE);
        assertThat(content).contains(String.format("An account with name %s and email %s, already exists.", ACCOUNT_NAME, ACCOUNT_EMAIL));
    }

    @Test
    void when_transactions_is_called_then_the_transaction_page_is_returned() throws Exception {
        final var contentString = getHtmlResult("/transactions");
        assertThat(contentString).doesNotContain(ERROR_TITLE);
        assertThat(contentString).contains("From Account Number");
        assertThat(contentString).contains("To Account Number");
    }

    @Test
    void when_a_transaction_is_performed_then_the_transactions_are_saved_properly_and_a_message_is_shown() throws Exception {
        final UUID uuidAccountOne = testHelper.saveAccount(AMOUNT);
        final UUID uuidAccountTwo = testHelper.saveAccount(AMOUNT.add(BigDecimal.valueOf(200)));
        final BigDecimal transferAmount = AMOUNT.subtract(BigDecimal.valueOf(30));

        final String result = postTransaction(uuidAccountOne, uuidAccountTwo, transferAmount);

        assertThat(result).doesNotContain(ERROR_TITLE);
        assertThat(result).contains("Transaction Result");
        assertThat(result).contains("Transaction was successful!");

        final var accountOneDetails = getHtmlResult(String.format("/accounts/%s", uuidAccountOne));
        assertThat(accountOneDetails).contains(AMOUNT.subtract(transferAmount)
                .toString());
        final var accountTwoDetails = getHtmlResult(String.format("/accounts/%s", uuidAccountTwo));
        assertThat(accountTwoDetails).contains(AMOUNT.add(transferAmount)
                .toString());
    }

    @Test
    void when_a_transaction_is_performed_but_there_is_no_sufficient_funds_then_the_a_message_is_shown() throws Exception {
        final BigDecimal accountTwoStartingAmount = AMOUNT.add(BigDecimal.valueOf(200));
        final UUID uuidAccountOne = testHelper.saveAccount(AMOUNT);
        final UUID uuidAccountTwo = testHelper.saveAccount(accountTwoStartingAmount);
        final BigDecimal transferAmount = AMOUNT.add(BigDecimal.valueOf(30));

        final String result = postTransaction(uuidAccountOne, uuidAccountTwo, transferAmount);

        assertThat(result).doesNotContain(ERROR_TITLE);
        assertThat(result).contains("Transaction Result");
        assertThat(result).contains("Transaction failed due to insufficient funds.");

        final var accountOneDetails = getHtmlResult(String.format("/accounts/%s", uuidAccountOne));
        assertThat(accountOneDetails).contains(AMOUNT.toString());
        final var accountTwoDetails = getHtmlResult(String.format("/accounts/%s", uuidAccountTwo));
        assertThat(accountTwoDetails).contains(accountTwoStartingAmount.toString());
    }

    private ResultActions postAccount(final String accountName, final String accountEmail) throws Exception {
        return mockMvc.perform(post("/accounts/add").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", accountName)
                .param("email", accountEmail)
                .param("amount", BigDecimal.valueOf(1000).toString()));

    }

    private String postTransaction(final UUID uuidAccountOne, final UUID uuidAccountTwo, final BigDecimal transferAmount) throws Exception {
        return mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fromAccountNumber", uuidAccountOne.toString())
                        .param("toAccountNumber", uuidAccountTwo.toString())
                        .param("amount", transferAmount.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private String getHtmlResult(final String url) throws Exception {
        final var result = sendGetRequest(url).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        return result.getResponse()
                .getContentAsString();
    }

    protected ResultActions sendGetRequest(final String url) throws Exception {
        return mockMvc.perform(get(url));
    }

}
