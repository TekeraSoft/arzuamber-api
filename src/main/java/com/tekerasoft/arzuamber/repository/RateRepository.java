package com.tekerasoft.arzuamber.repository;

import com.tekerasoft.arzuamber.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RateRepository extends JpaRepository<Rate, UUID> {
}
