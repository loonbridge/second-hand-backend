package cn.edu.guet.secondhandtransactionbackend.dto.address;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddressDTO {

    private String receiverName;

    private String phoneNumber;

    private String address;

    private Boolean isDefault;
}
