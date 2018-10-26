package com.zikovam.entity;

import javax.persistence.*;

@Entity
@Table (name = "account")
public class Account {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Column (name = "balance")
    private Long balance;

    public Account (User user, long balance) {
        this.user = user;
        this.balance = balance;
    }

    public Account () {
    }

    public Long getId () {
        return id;
    }

    public Long getBalance () {
        return balance;
    }

    public void setBalance (long balance) {
        this.balance = balance;
    }

    public User getUser () {
        return user;
    }

    public void setUser (User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id:" + id +
                ",balance:'" + balance +
                "'}";
    }
}
