package com.work.restaurant2.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "restauranttable")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_number")
    private int number;


    public RestaurantTable() {
    }

    public RestaurantTable(int number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
