package com.glowtique.glowtique.web.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateQuantityRequest {
    private UUID userId;
    private UUID productId;
    private int quantity;
}
