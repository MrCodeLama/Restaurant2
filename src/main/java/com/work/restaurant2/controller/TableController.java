package com.work.restaurant2.controller;

import com.work.restaurant2.dto.TableDTO;
import com.work.restaurant2.entity.RestaurantTable;
import com.work.restaurant2.service.TableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public List<TableDTO> getTables() {
        return tableService.getAllTablesWithOrderStatus();
    }
}
