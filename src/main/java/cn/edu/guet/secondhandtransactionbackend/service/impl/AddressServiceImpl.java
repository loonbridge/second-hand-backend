package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.address.AddressBO;
import cn.edu.guet.secondhandtransactionbackend.dto.address.AddressDTO;
import cn.edu.guet.secondhandtransactionbackend.entity.Address;
import cn.edu.guet.secondhandtransactionbackend.mapper.AddressMapper;
import cn.edu.guet.secondhandtransactionbackend.service.AddressService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sammy
 * @description 针对表【address(用户地址实体表)】的数据库操作Service实现
 * @createDate 2025-08-03 21:05:00
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<AddressBO> getUserAddresses(Long userId) {
        List<Address> addresses = this.list(new LambdaQueryWrapper<Address>()
                .eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreatedAt));

        return addresses.stream()
                .map(this::convertToBO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressBO createAddress(AddressDTO addressDTO, Long userId) {
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        address.setUserId(userId);
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());

        // 如果设置为默认地址，需要将其他地址的默认状态取消
        if (Boolean.TRUE.equals(addressDTO.getIsDefault())) {
            this.update(new LambdaUpdateWrapper<Address>()
                    .eq(Address::getUserId, userId)
                    .set(Address::getIsDefault, false));
        }

        this.save(address);
        return convertToBO(address);
    }

    @Override
    @Transactional
    public AddressBO updateAddress(Long addressId, AddressDTO addressDTO, Long userId) {
        Address address = this.getOne(new LambdaQueryWrapper<Address>()
                .eq(Address::getAddressId, addressId)
                .eq(Address::getUserId, userId));

        if (address == null) {
            return null;
        }

        BeanUtils.copyProperties(addressDTO, address);
        address.setUpdatedAt(LocalDateTime.now());

        // 如果设置为默认地址，需要将其他地址的默认状态取消
        if (Boolean.TRUE.equals(addressDTO.getIsDefault())) {
            this.update(new LambdaUpdateWrapper<Address>()
                    .eq(Address::getUserId, userId)
                    .ne(Address::getAddressId, addressId)
                    .set(Address::getIsDefault, false));
        }

        this.updateById(address);
        return convertToBO(address);
    }

    @Override
    public boolean deleteAddress(Long addressId, Long userId) {
        return this.remove(new LambdaQueryWrapper<Address>()
                .eq(Address::getAddressId, addressId)
                .eq(Address::getUserId, userId));
    }

    private AddressBO convertToBO(Address address) {
        AddressBO addressBO = new AddressBO();
        BeanUtils.copyProperties(address, addressBO);
        return addressBO;
    }
}
