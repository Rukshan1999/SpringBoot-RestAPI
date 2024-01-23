package com.ijse.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ijse.database.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
