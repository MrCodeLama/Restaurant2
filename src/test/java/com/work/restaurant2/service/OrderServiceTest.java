package com.work.restaurant2.service;

import com.work.restaurant2.dto.OrderItemDTO;
import com.work.restaurant2.entity.CustomerOrder;
import com.work.restaurant2.entity.MenuItem;
import com.work.restaurant2.entity.OrderItem;
import com.work.restaurant2.entity.RestaurantTable;
import com.work.restaurant2.entity.Category;
import com.work.restaurant2.repository.CustomerOrderRepository;
import com.work.restaurant2.repository.MenuItemRepository;
import com.work.restaurant2.repository.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CustomerOrderRepository orderRepository;

    @Mock
    private RestaurantTableRepository tableRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrderSuccessful() {
        Long tableId = 1L;
        RestaurantTable table = new RestaurantTable();
        table.setId(tableId);
        table.setNumber(5);

        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("menuItemId", 10L);
        itemData.put("quantity", 2);
        List<Map<String, Object>> itemsData = List.of(itemData);

        MenuItem menuItem = new MenuItem("Pizza", "Cheese pizza", 9.99, Category.MAIN);
        menuItem.setId(10L);
        when(menuItemRepository.findById(10L)).thenReturn(Optional.of(menuItem));

        CustomerOrder savedOrder = new CustomerOrder();
        savedOrder.setId(100L);
        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(savedOrder);

        CustomerOrder result = orderService.createOrder(tableId, itemsData);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(orderRepository, times(1)).save(any(CustomerOrder.class));
    }

    @Test
    public void testCreateOrderTableNotFound() {
        Long tableId = 1L;
        when(tableRepository.findById(tableId)).thenReturn(Optional.empty());

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("menuItemId", 10L);
        itemData.put("quantity", 2);
        List<Map<String, Object>> itemsData = List.of(itemData);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(tableId, itemsData);
        });
        assertTrue(exception.getMessage().contains("Столик не знайдено"));
    }

    @Test
    public void testCreateOrderMenuItemNotFound() {
        Long tableId = 1L;
        RestaurantTable table = new RestaurantTable();
        table.setId(tableId);
        table.setNumber(5);

        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));

        Map<String, Object> itemData = new HashMap<>();
        itemData.put("menuItemId", 10L);
        itemData.put("quantity", 2);
        List<Map<String, Object>> itemsData = List.of(itemData);

        when(menuItemRepository.findById(10L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(tableId, itemsData);
        });
        assertTrue(exception.getMessage().contains("Не знайдено страву з id: 10"));
    }

    @Test
    public void testGetOrderItemDTOsForTable() {
        Long tableId = 1L;
        RestaurantTable table = new RestaurantTable();
        table.setId(tableId);
        table.setNumber(5);
        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));

        // Створюємо замовлення з одним OrderItem
        CustomerOrder order = new CustomerOrder();
        OrderItem orderItem = new OrderItem();
        MenuItem menuItem = new MenuItem("Burger", "Delicious burger", 5.99, Category.MAIN);
        menuItem.setId(11L);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(3);
        order.setOrderItems(List.of(orderItem));

        when(orderRepository.findByRestaurantTable(table)).thenReturn(List.of(order));

        List<OrderItemDTO> dtos = orderService.getOrderItemDTOsForTable(tableId);
        assertNotNull(dtos);
        assertEquals(1, dtos.size());

        OrderItemDTO dto = dtos.get(0);
        assertEquals(11L, dto.getMenuItemId());
        assertEquals("Burger", dto.getName());
        assertEquals(5.99, dto.getPrice());
        assertEquals(3, dto.getQuantity());
    }

    @Test
    public void testGetOrderItemDTOsForTableNoOrderFound() {
        Long tableId = 1L;
        RestaurantTable table = new RestaurantTable();
        table.setId(tableId);
        table.setNumber(5);
        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));
        when(orderRepository.findByRestaurantTable(table)).thenReturn(Collections.emptyList());

        List<OrderItemDTO> dtos = orderService.getOrderItemDTOsForTable(tableId);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    public void testConfirmPaymentSuccessful() {
        Long tableId = 1L;
        RestaurantTable table = new RestaurantTable();
        table.setId(tableId);
        table.setNumber(5);
        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));

        CustomerOrder order = new CustomerOrder();
        order.setId(200L);
        List<CustomerOrder> orders = List.of(order);
        when(orderRepository.findByRestaurantTable(table)).thenReturn(orders);

        doNothing().when(orderRepository).deleteAll(orders);

        orderService.confirmPayment(tableId);

        verify(orderRepository, times(1)).deleteAll(orders);
    }

    @Test
    public void testConfirmPaymentNoOrders() {
        Long tableId = 1L;
        RestaurantTable table = new RestaurantTable();
        table.setId(tableId);
        table.setNumber(5);
        when(tableRepository.findById(tableId)).thenReturn(Optional.of(table));
        when(orderRepository.findByRestaurantTable(table)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.confirmPayment(tableId);
        });
        assertTrue(exception.getMessage().contains("Замовлень за цим столиком немає"));
    }
}
