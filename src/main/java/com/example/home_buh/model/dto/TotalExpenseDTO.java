package com.example.home_buh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalExpenseDTO {
    private BigDecimal totalAmount;
    private List<ExpenseDTO> expenses;
}
