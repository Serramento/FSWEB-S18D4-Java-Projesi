package com.workintech.s18d4.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Data
@Table(name= "account", schema = "fsweb")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private long id;

    @Column(name= "account_name")
    private String accountName;

    @Column(name= "money_amount")
    private double moneyAmount;

    @JsonIgnoreProperties("accounts")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name= "customer_id")
    private Customer customer;
}
