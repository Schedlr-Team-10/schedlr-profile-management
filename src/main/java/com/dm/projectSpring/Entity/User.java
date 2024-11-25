package com.dm.projectSpring.Entity;

import jakarta.persistence.*;
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

    @Column(nullable = false, length = 100,unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Lob
    private byte[] profilepic;

    @Lob
    private String bio;

    public enum AccountType {
        INFLUENCER, PERSONAL
    }

}
