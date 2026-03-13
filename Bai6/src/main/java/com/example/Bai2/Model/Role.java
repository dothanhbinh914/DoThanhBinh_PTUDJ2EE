package com.example.Bai2.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;
}
