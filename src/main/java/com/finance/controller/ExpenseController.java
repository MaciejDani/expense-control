package com.finance.controller;

import com.finance.dto.ExpenseDto;
import com.finance.model.Expense;
import com.finance.security.UserPrincipal;
import com.finance.service.ExpenseService;
import com.finance.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExportService exportService;

    @PostMapping("/add")
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseDto expenseDTO,
                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
       Expense expense = expenseService.saveExpense(expenseDTO, userPrincipal);

        return ResponseEntity.ok(expense);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Expense>> getAllExpenses(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Expense> expenses = expenseService.getAllExpenses(userPrincipal);

        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Optional<Expense> expense = expenseService.getExpenseById(id, userPrincipal);

        return expense.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id,
                              @AuthenticationPrincipal UserPrincipal userPrincipal) {
        expenseService.deleteExpense(id, userPrincipal);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/export/excel")
    public void exportExpensesToExcel(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      HttpServletResponse response) throws IOException {
        List<Expense> expenses = expenseService.getAllExpenses(userPrincipal);
        exportService.exportExpensesToExcel(expenses, response);
    }
}
