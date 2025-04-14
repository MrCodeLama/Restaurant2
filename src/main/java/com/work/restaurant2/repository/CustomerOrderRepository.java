package com.work.restaurant2.repository;

import com.work.restaurant2.entity.CustomerOrder;
import com.work.restaurant2.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByRestaurantTable(RestaurantTable restaurantTable);
}
