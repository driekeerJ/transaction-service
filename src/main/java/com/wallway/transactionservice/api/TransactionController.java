package com.wallway.transactionservice.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wallway.transactionservice.core.TransactionService;
import com.wallway.transactionservice.domain.Transaction;
import com.wallway.transactionservice.domain.TransactionResultType;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping()
    public String getTransactionPage(final Model model) {
        model.addAttribute("transaction", Transaction.builder()
                .build());
        return "transaction/transaction";
    }

    @PostMapping()
    public String performTransaction(@ModelAttribute final Transaction transaction, final Model model) {
        final TransactionResultType transactionResultType = service.performTransaction(transaction);
        model.addAttribute("result", transactionResultType.name());
        return "transaction/transaction-result";
    }
}
