package com.k0s.onlineshop.entity;

import com.k0s.onlineshop.security.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString(exclude = {"user"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_cart",
        uniqueConstraints = @UniqueConstraint(
                name = "cart_userId",
                columnNames = "user_id"
        )
)
@Entity
public class ProductCart {
    @Id
    @Column(name = "cart_id")
    @SequenceGenerator(name = "cart_sequence",
            sequenceName = "cart_sequence",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_sequence")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cart_item",
            joinColumns = @JoinColumn(
                    name = "cart_id",
                    referencedColumnName = "cart_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id"
            )
    )
    private List<Product> products = new ArrayList<>();


    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean deleteProduct(Long productId) {
        return products.removeIf(product -> productId == product.getId());
    }
}
