package com.immunopass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.immunopass.entity.OrderEntity;


public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
