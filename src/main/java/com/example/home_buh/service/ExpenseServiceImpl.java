package com.example.home_buh.service;

import com.example.home_buh.mapper.ExpenseMapper;
import com.example.home_buh.model.Expense;
import com.example.home_buh.model.User;
import com.example.home_buh.model.dto.ExpenseDTO;
import com.example.home_buh.model.dto.TotalExpenseDTO;
import com.example.home_buh.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public ExpenseDTO createExpense(User user, ExpenseDTO expenseDTO) {
        Expense expense = new Expense(user, expenseDTO.getAmount(), expenseDTO.getDate(), expenseDTO.getCategory());
        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.toExpenseDTO(savedExpense);
    }

    @Override
    public boolean deleteExpense(User user, Long expenseId) {
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);
        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.get();
            if (expense.getUser().equals(user)) {
                expenseRepository.delete(expense);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ExpenseDTO> getAllExpensesForUser(User user) {
        List<Expense> expenses = expenseRepository.findAllByUserId(user.getId());
        return expenseMapper.toExpenseDTOs(expenses);
    }

    @Override
    public TotalExpenseDTO getTotalExpensesBetweenDatesForUser(User user, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTimestamp = startDate.atStartOfDay();
        LocalDateTime endTimestamp = endDate.atTime(23, 59, 59);
        List<Expense> expenses = expenseRepository.findAllByUserIdAndDateBetween(user.getId(), startTimestamp, endTimestamp);

        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        TotalExpenseDTO totalExpenseDTO = new TotalExpenseDTO();
        totalExpenseDTO.setTotalAmount(totalAmount);
        totalExpenseDTO.setExpenses(expenseMapper.toExpenseDTOs(expenses));

        return totalExpenseDTO;
    }

    @Override
    public TotalExpenseDTO getTotalExpensesBetweenDatesByCategoryForUser(User user, LocalDate startDate, LocalDate endDate, String category) {
        LocalDateTime startTimestamp = startDate.atStartOfDay();
        LocalDateTime endTimestamp = endDate.atTime(23, 59, 59);

        List<Expense> expenses = expenseRepository.findAllByUserIdAndDateBetweenAndCategory(user.getId(), startTimestamp, endTimestamp, category);

        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        TotalExpenseDTO totalExpenseDTO = new TotalExpenseDTO();
        totalExpenseDTO.setTotalAmount(totalAmount);
        totalExpenseDTO.setExpenses(expenseMapper.toExpenseDTOs(expenses));

        return totalExpenseDTO;
    }
}