package com.example.demo.repository;

import com.example.demo.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findAllByDate(Date date);

    ExchangeRate findFirstByNameAndDate(String name, Date date);
}
