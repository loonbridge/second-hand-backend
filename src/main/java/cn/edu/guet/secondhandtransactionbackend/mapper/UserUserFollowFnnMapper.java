package cn.edu.guet.secondhandtransactionbackend.mapper;

import cn.edu.guet.secondhandtransactionbackend.entity.UserUserFollowFnn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Sammy
 * @description 针对表【user_user_follow_fnn([行为]用户关注用户(n-n)关系表)】的数据库操作Mapper
 * @createDate 2025-07-25 18:08:42
 * @Entity cn.edu.guet.secondhandtransactionbackend.entity.UserUserFollowFnn
 */

@Mapper
public interface UserUserFollowFnnMapper extends BaseMapper<UserUserFollowFnn> {

}




