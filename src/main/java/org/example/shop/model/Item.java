package org.example.shop.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "items")
public class Item {

    @Id
    @Column("id")
    private Integer id;

    @Column("title")
    private String title;

    @Column("price")
    private BigDecimal price;

    @Column("description")
    private String description;

    @Column("img_path")
    private String imgPath;
}
