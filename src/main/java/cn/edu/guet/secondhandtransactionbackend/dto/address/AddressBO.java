package cn.edu.guet.secondhandtransactionbackend.dto.address;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AddressBO {

    private Long addressId;

    private Long userId;

    private String receiverName;

    private String phoneNumber;

    private String address;

    private Boolean isDefault;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
