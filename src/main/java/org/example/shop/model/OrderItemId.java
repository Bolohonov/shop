package org.example.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderItemId {

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;
}
