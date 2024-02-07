package com.example.home_buh.repository;

import com.example.home_buh.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findAllByUserId(Long userId);

    List<Expense> findAllByUserIdAndDateBetween(Long userId, LocalDateTime date, LocalDateTime date2);

    List<Expense> findAllByUserIdAndDateBetweenAndCategory(Long userId, LocalDateTime date, LocalDateTime date2, String category);
}