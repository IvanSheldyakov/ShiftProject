package ru.cft.freelanceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cft.freelanceservice.repository.model.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price,Long> {
    Price save(Price price);
}
