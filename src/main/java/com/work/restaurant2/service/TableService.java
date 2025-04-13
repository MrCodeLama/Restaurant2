package com.work.restaurant2.service;

import com.work.restaurant2.dto.TableDTO;
import com.work.restaurant2.entity.CustomerOrder;
import com.work.restaurant2.entity.RestaurantTable;
import com.work.restaurant2.repository.CustomerOrderRepository;
import com.work.restaurant2.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableService {

    private final RestaurantTableRepository tableRepository;
    private final CustomerOrderRepository orderRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public TableService(RestaurantTableRepository tableRepository, CustomerOrderRepository orderRepository) {
        this.tableRepository = tableRepository;
        this.orderRepository = orderRepository;
    }

    public List<TableDTO> getAllTablesWithOrderStatus() {
        logger.info("Перегляд замовлень усіх столиків");
        List<RestaurantTable> tables = tableRepository.findAll();
        List<TableDTO> tableDTOs = new ArrayList<>();
        for (RestaurantTable table : tables) {
            List<CustomerOrder> orders = orderRepository.findByRestaurantTable(table);
            boolean hasOrder = !orders.isEmpty();
            logger.info("Столик {} {}", table.getNumber(), (hasOrder) ? "Має замовлення" : "Без замовлення");
            tableDTOs.add(new TableDTO(table.getId(), table.getNumber(), hasOrder));
        }
        return tableDTOs;
    }
}
