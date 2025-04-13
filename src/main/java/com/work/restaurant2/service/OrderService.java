package com.work.restaurant2.service;

import com.work.restaurant2.dto.OrderItemDTO;
import com.work.restaurant2.entity.CustomerOrder;
import com.work.restaurant2.entity.MenuItem;
import com.work.restaurant2.entity.OrderItem;
import com.work.restaurant2.entity.RestaurantTable;
import com.work.restaurant2.repository.CustomerOrderRepository;
import com.work.restaurant2.repository.MenuItemRepository;
import com.work.restaurant2.repository.RestaurantTableRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final CustomerOrderRepository orderRepository;
    private final RestaurantTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(CustomerOrderRepository orderRepository,
                        RestaurantTableRepository tableRepository,
                        MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public CustomerOrder createOrder(Long tableId, List<Map<String, Object>> itemsData) {
        Optional<RestaurantTable> tableOpt = tableRepository.findById(tableId);
        if (tableOpt.isEmpty()) {
            logger.error("Столик з id {} не знайдено", tableId);
            throw new RuntimeException("Столик не знайдено");
        }
        RestaurantTable restaurantTable = tableOpt.get();
        logger.info("Створення замовлення для столика: {}", restaurantTable.getNumber());
        CustomerOrder order = new CustomerOrder();
        order.setRestaurantTable(restaurantTable);

        List<OrderItem> orderItems = new ArrayList<>();
        for (Map<String, Object> itemData : itemsData) {
            Long menuItemId = Long.valueOf(itemData.get("menuItemId").toString());
            int quantity = Integer.parseInt(itemData.get("quantity").toString());
            MenuItem menuItem = menuItemRepository.findById(menuItemId)
                    .orElseThrow(() -> {
                        logger.error("Не знайдено страву з id: {}", menuItemId);
                        return new RuntimeException("Не знайдено страву з id: " + menuItemId);
                    });
            logger.info("Додавання страви {} ({} шт.)", menuItem.getName(), quantity);
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(quantity);
            orderItem.setCustomerOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        logger.info("Замовлення створено");
        return orderRepository.save(order);
    }

    @Transactional
    public List<OrderItemDTO> getOrderItemDTOsForTable(Long tableId) {
        Optional<RestaurantTable> tableOpt = tableRepository.findById(tableId);
        if (tableOpt.isEmpty()) {
            logger.error("Не знайдено столик з id {} при отриманні замовлення", tableId);
            throw new RuntimeException("Столик не знайдено");
        }
        RestaurantTable table = tableOpt.get();
        List<CustomerOrder> orders = orderRepository.findByRestaurantTable(table);
        if (!orders.isEmpty()) {
            List<OrderItemDTO> dtos = new ArrayList<>();
            for (OrderItem item : orders.get(0).getOrderItems()) {
                MenuItem menuItem = item.getMenuItem();
                logger.debug("Обробка рядка замовлення для страви {}", menuItem.getName());
                dtos.add(new OrderItemDTO(
                        menuItem.getId(),
                        menuItem.getName(),
                        menuItem.getPrice(),
                        item.getQuantity()
                ));
            }
            return dtos;
        }
        return List.of();
    }

    @Transactional
    public void confirmPayment(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Столик не знайдено"));
        List<CustomerOrder> orders = orderRepository.findByRestaurantTable(table);
        if (orders.isEmpty()) {
            logger.warn("Для столика {} немає замовлень", table.getNumber());
            throw new RuntimeException("Замовлень за цим столиком немає");
        }
        // Видаляємо всі замовлення за столиком
        orderRepository.deleteAll(orders);
        logger.info("Підтверджено оплату та видалено замовлення для столика {}", table.getNumber());
    }
}