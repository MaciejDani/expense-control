package com.finance.repository;

import com.finance.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT e FROM Expense e WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month")
    List<Expense> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month")
    BigDecimal findTotalExpensesByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
