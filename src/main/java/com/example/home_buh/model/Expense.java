package com.example.home_buh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal amount;

    private LocalDateTime date;

    private String category;

    public Expense(User user, BigDecimal amount, LocalDateTime date, String category) {
        this.user = user;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }
}
