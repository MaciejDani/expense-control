package com.finance.repository;

import com.finance.model.Category;
import com.finance.model.Expense;
import com.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT e FROM Expense e WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month AND e.user.id = :userId")
    List<Expense> findByYearAndMonthAndUser(@Param("year") int year, @Param("month") int month, @Param("userId") Long userId);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month AND e.user.id = :userId")
    BigDecimal findTotalExpensesByYearAndMonthAndUser(@Param("year") int year, @Param("month") int month, @Param("userId") Long userId);

    List<Expense> findByUser(User user);
    Optional<Expense> findByIdAndUser(Long Id, User user);
}
