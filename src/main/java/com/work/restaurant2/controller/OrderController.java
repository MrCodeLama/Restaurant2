package com.work.restaurant2.controller;

import com.work.restaurant2.dto.OrderItemDTO;
import com.work.restaurant2.entity.CustomerOrder;
import com.work.restaurant2.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> requestData) {
        try {
            Long tableId = Long.valueOf(requestData.get("tableId").toString());

            List<Map<String, Object>> items = (List<Map<String, Object>>) requestData.get("menuItemIds");

            CustomerOrder order = orderService.createOrder(tableId, items);

            return ResponseEntity.ok(Map.of("success", true, "orderId", order.getId()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Не вдалося створити замовлення: " + e.getMessage()));
        }
    }

    @GetMapping("/table-order-details")
    public ResponseEntity<?> getOrderDetails(@RequestParam Long tableId) {
        try {
            List<OrderItemDTO> orderItemsDTO = orderService.getOrderItemDTOsForTable(tableId);
            return ResponseEntity.ok(orderItemsDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestParam Long tableId) {
        try {
            orderService.confirmPayment(tableId);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Не вдалося видалити замовлення: " + e.getMessage()));
        }
    }
}
