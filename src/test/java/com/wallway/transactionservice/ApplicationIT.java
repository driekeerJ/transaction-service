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
        final UUID uuid = testHelper.saveAccount();
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
        final String name = "Test Name";
        final String email = "test@example.com";
        final BigDecimal amount = BigDecimal.valueOf(1000);

        final ResultActions resultActions = mockMvc.perform(post("/accounts/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", name)
                .param("email", email)
                .param("amount", amount.toString()));

        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/accounts/*"));
    }

    private String getHtmlResult(final String url) throws Exception {
        final var result = sendGetRequest(url)
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        return result.getResponse()
                .getContentAsString();
    }

    protected ResultActions sendGetRequest(final String url) throws Exception {
        return mockMvc.perform(get(url));
    }

}
