package com.work.restaurant2.service;

import com.work.restaurant2.entity.MenuItem;
import com.work.restaurant2.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class MenuService {

    private final MenuItemRepository menuItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems() {
        logger.info("Отримання всіх елементів меню");
        return menuItemRepository.findAll();
    }
}
