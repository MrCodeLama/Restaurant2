package com.work.restaurant2.service;

import com.work.restaurant2.entity.MenuItem;
import com.work.restaurant2.entity.Category;
import com.work.restaurant2.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMenuItems() {
        MenuItem item1 = new MenuItem("Pizza", "Cheese Pizza", 9.99, Category.MAIN);
        item1.setId(1L);
        MenuItem item2 = new MenuItem("Coke", "Soft drink", 1.99, Category.DRINK);
        item2.setId(2L);

        when(menuItemRepository.findAll()).thenReturn(List.of(item1, item2));

        List<MenuItem> menuItems = menuService.getAllMenuItems();
        assertNotNull(menuItems);
        assertEquals(2, menuItems.size());
        assertEquals("Pizza", menuItems.get(0).getName());
        assertEquals("Coke", menuItems.get(1).getName());
    }
}
