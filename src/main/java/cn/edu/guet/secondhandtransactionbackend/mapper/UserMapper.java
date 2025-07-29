package cn.edu.guet.secondhandtransactionbackend.mapper;

import cn.edu.guet.secondhandtransactionbackend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Sammy
* @description 针对表【user(用户实体表)】的数据库操作Mapper
* @createDate 2025-07-25 18:08:42
* @Entity cn.edu.guet.secondhandtransactionbackend.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




