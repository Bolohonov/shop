package org.example.shop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "order_items")
@NoArgsConstructor
public class OrderItem {

    @Id
    private Integer id;
    @Column("order_id")
    private Integer orderId;
    @Column("item_id")
    private Integer itemId;
    @Column("quantity")
    private Integer quantity;

    public OrderItem(Item item, Order order, int quantity) {
        this.itemId = item.getId();
        this.orderId = order.getId();
        this.quantity = 0;
    }

    public OrderItem(Item item, Order order) {
        this.itemId = item.getId();
        this.orderId = order.getId();
        this.quantity = 0;
    }
}
