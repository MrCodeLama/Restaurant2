package com.work.restaurant2.dto;

public class TableDTO {
    private Long id;
    private int tableNumber;
    private boolean hasOrder;

    public TableDTO() {
    }

    public TableDTO(Long id, int tableNumber, boolean hasOrder) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.hasOrder = hasOrder;
    }

    public Long getId() {
        return id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public boolean isHasOrder() {
        return hasOrder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }
}
