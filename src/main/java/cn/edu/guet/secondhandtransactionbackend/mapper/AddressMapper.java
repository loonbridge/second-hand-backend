package cn.edu.guet.secondhandtransactionbackend.mapper;

import cn.edu.guet.secondhandtransactionbackend.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Sammy
 * @description 针对表【address(用户地址实体表)】的数据库操作Mapper
 * @createDate 2025-08-03 21:05:00
 */

@Mapper
public interface AddressMapper extends BaseMapper<Address> {

}
