package com.work.restaurant2.service;

import com.work.restaurant2.dto.TableDTO;
import com.work.restaurant2.entity.CustomerOrder;
import com.work.restaurant2.entity.RestaurantTable;
import com.work.restaurant2.repository.CustomerOrderRepository;
import com.work.restaurant2.repository.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private RestaurantTableRepository tableRepository;

    @Mock
    private CustomerOrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTablesWithOrderStatus() {
        RestaurantTable table1 = new RestaurantTable();
        table1.setId(1L);
        table1.setNumber(1);

        RestaurantTable table2 = new RestaurantTable();
        table2.setId(2L);
        table2.setNumber(2);

        when(tableRepository.findAll()).thenReturn(List.of(table1, table2));

        CustomerOrder order = new CustomerOrder();
        when(orderRepository.findByRestaurantTable(table1)).thenReturn(List.of(order));
        when(orderRepository.findByRestaurantTable(table2)).thenReturn(List.of());

        List<TableDTO> tableDTOs = tableService.getAllTablesWithOrderStatus();
        assertNotNull(tableDTOs);
        assertEquals(2, tableDTOs.size());

        TableDTO dto1 = tableDTOs.stream().filter(t -> t.getTableNumber() == 1).findFirst().orElse(null);
        TableDTO dto2 = tableDTOs.stream().filter(t -> t.getTableNumber() == 2).findFirst().orElse(null);

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertTrue(dto1.isHasOrder());
        assertFalse(dto2.isHasOrder());
    }
}
