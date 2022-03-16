package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "convert")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Convert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "from_valute")
    private String fromValute;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "to_valute")
    private String toValute;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "result")
    private BigDecimal result;
}
