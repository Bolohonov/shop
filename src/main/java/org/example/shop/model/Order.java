package org.example.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreationTimestamp
    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    @Column(name = "customer", nullable = false)
    private String customer;

    @Column(name = "session", nullable = false)
    private String session;

    //  NEW, IN_PROGRESS, FINISHED
    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();
}
