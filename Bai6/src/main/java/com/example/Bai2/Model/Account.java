package com.example.Bai2.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "account")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String login_name;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "account_role",
        joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
