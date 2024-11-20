package com.dm.projectSpring.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    public User() {
    }
    public User(int userid){
        this.userid = userid;
    }

    @Id
    private int userid;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
@Column(nullable = false)
private AccountType accountType;

public enum AccountType {
    INFLUENCER, PERSONAL
}


    
    // Constructors, getters, and setters


    // Add relationships with other entities if necessary

    // Getters and setters
}
