package com.finance.controller;

import com.finance.dto.BudgetDto;
import com.finance.model.Budget;
import com.finance.security.UserPrincipal;
import com.finance.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    @PostMapping("/add")
    public ResponseEntity<Budget> setMonthlyBudget(@RequestBody BudgetDto budgetDto,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Budget budget = budgetService.setMonthlyBudget(budgetDto, userPrincipal);

        return ResponseEntity.ok(budget);
    }

    @GetMapping
    public ResponseEntity<Budget> getMonthlyBudget(@RequestParam int year, @RequestParam int month,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Optional<Budget> budget = budgetService.getMonthlyBudget(year, month, userPrincipal);

        return budget.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
