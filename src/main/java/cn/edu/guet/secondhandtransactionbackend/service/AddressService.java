package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.dto.address.AddressBO;
import cn.edu.guet.secondhandtransactionbackend.dto.address.AddressDTO;
import cn.edu.guet.secondhandtransactionbackend.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Sammy
 * @description 针对表【address(用户地址实体表)】的数据库操作Service
 * @createDate 2025-08-03 21:05:00
 */
public interface AddressService extends IService<Address> {

    /**
     * 获取用户的所有地址
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    List<AddressBO> getUserAddresses(Long userId);

    /**
     * 创建新地址
     *
     * @param addressDTO 地址DTO
     * @param userId     用户ID
     * @return 创建的地址BO
     */
    AddressBO createAddress(AddressDTO addressDTO, Long userId);

    /**
     * 更新地址
     *
     * @param addressId  地址ID
     * @param addressDTO 地址DTO
     * @param userId     用户ID
     * @return 更新后的地址BO
     */
    AddressBO updateAddress(Long addressId, AddressDTO addressDTO, Long userId);

    /**
     * 删除地址
     *
     * @param addressId 地址ID
     * @param userId    用户ID
     * @return 是否删除成功
     */
    boolean deleteAddress(Long addressId, Long userId);
}
