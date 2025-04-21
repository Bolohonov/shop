package org.example.shop.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @Column("id")
    private Integer id;

    @Column("order_time")
    private LocalDateTime orderTime;

    @Column("customer")
    private String customer;

    @Column("session")
    private String session;

    //  NEW, IN_PROGRESS, FINISHED
    @Column("status")
    private String status;
}
