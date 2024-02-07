package com.example.home_buh.mapper;

import com.example.home_buh.model.Expense;
import com.example.home_buh.model.dto.ExpenseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "id", source = "expense.id")
    @Mapping(target = "amount", source = "expense.amount")
    @Mapping(target = "date", source = "expense.date")
    @Mapping(target = "category", source = "expense.category")

    ExpenseDTO toExpenseDTO(Expense expense);

    List<ExpenseDTO> toExpenseDTOs(List<Expense> expenses);
}
