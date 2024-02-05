package com.example.home_buh.repository;

import com.example.home_buh.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findAllByUserId(Long userId);

    List<Expense> findAllByUserIdAndDateBetween(Long userId, Timestamp startDate, Timestamp endDate);

    List<Expense> findAllByUserIdAndDateBetweenAndCategory(Long userId, Timestamp startDate, Timestamp endDate, String category);
}