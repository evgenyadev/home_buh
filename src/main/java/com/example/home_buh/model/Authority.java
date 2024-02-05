package com.example.home_buh.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "authorities")
@Data
public class Authority {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "authority")
    private String authority;
}
