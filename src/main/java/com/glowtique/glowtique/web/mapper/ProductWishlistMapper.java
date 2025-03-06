package com.glowtique.glowtique.web.mapper;

import com.glowtique.glowtique.product.model.Product;
import com.glowtique.glowtique.wishlistitem.model.WishlistItem;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWishlistMapper {
    private Product product;
    private WishlistItem wishlistItem;
}
