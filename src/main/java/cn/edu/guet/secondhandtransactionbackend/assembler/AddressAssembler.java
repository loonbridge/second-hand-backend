package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.AddressVO;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateAddressRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.UpdateAddressRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.address.AddressBO;
import cn.edu.guet.secondhandtransactionbackend.dto.address.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressAssembler {

    /**
     * 将AddressBO转换为AddressVO
     */
    @Mapping(source = "addressId", target = "addressId")
    AddressVO toAddressVO(AddressBO addressBO);

    /**
     * 将CreateAddressRequest转换为AddressDTO
     */
    AddressDTO toAddressDTO(CreateAddressRequest createAddressRequest);

    /**
     * 将UpdateAddressRequest转换为AddressDTO
     */
    AddressDTO toAddressDTO(UpdateAddressRequest updateAddressRequest);
}
