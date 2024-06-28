package com.wallway.transactionservice.api;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wallway.transactionservice.core.AccountService;
import com.wallway.transactionservice.domain.Account;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping("/{id}")
    public String getAccount(@PathVariable final UUID id, final Model model) {
        final Account account = service.getAccount(id);
        model.addAttribute("account", account);
        return "account/account-details";
    }

    @PostMapping("/add")
    public String addAccount(@ModelAttribute final Account account) {
        final UUID id = service.saveAccount(account);
        return String.format("redirect:/accounts/%s", id);
    }

    @GetMapping()
    public String getAccountPage() {
        return "account/account";
    }

    @GetMapping("/add")
    public String getAddPage(final Model model) {
        model.addAttribute("account", Account.builder()
                .build());
        return "account/add-account";
    }

    @GetMapping("/get")
    public String getGetPage() {
        return "account/get-account";
    }


}
