package com.tekerasoft.arzuamber.service;

import com.tekerasoft.arzuamber.dto.RateDto;
import com.tekerasoft.arzuamber.model.Comment;
import com.tekerasoft.arzuamber.model.Product;
import com.tekerasoft.arzuamber.model.Rate;
import com.tekerasoft.arzuamber.repository.RateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RateService {
    private final RateRepository rateRepository;

    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public Rate createRate(Product product, Comment comment, RateDto dto) {
        try {
            Rate rate = new Rate(
                    dto.getUserName(),
                    dto.getUserId(),
                    dto.getRate(),
                    LocalDateTime.now(),
                    null,
                    false,
                    product,
                    comment
            );
            return rateRepository.save(rate);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteRate(String id) {
        rateRepository.deleteById(UUID.fromString(id));
    }
}
