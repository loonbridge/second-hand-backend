package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.UpdateUserRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileDTO;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.User;
import cn.edu.guet.secondhandtransactionbackend.service.UserService;
import cn.edu.guet.secondhandtransactionbackend.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * @author Sammy
 * @description 针对表【user(用户实体表)】的数据库操作Service实现
 * @createDate 2025-07-25 18:08:42
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private final UserMapper userMapper;

    private ProductService productService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        // 移除构造函数中的 ProductService 注入
        this.userMapper = userMapper;
    }

    @Autowired
    @Lazy // 继续使用 @Lazy 以确保安全
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }


    @Override
    public UserProfileBO getUserProfileById(Long userId) {

        User user = userMapper.selectById(userId);

        UserProfileBO userProfileBO = new UserProfileBO();

        BeanUtils.copyProperties(user, userProfileBO);

        return userProfileBO;
    }

    /**
     * 更新用户信息
     *
     * @param userProfileDTO 用户信息数据传输对象
     * @param currentUserId 当前用户ID
     * @return 更新后的用户信息
     */

    @Override
    public UserProfileBO pathchUser(UserProfileDTO userProfileDTO,Long currentUserId) {

        // 1. 【安全校验】首先检查是否有任何需要更新的字段
        if (userProfileDTO.getNickname() == null && userProfileDTO.getAvatarUrl() == null) {
            // 如果DTO是空的，什么都不做，直接查询并返回当前用户信息
            User currentUser = userMapper.selectById(currentUserId);
            if (currentUser == null) {
                throw new RuntimeException("用户不存在"); // 或者抛出更具体的业务异常
            }
            UserProfileBO userProfileBO = new UserProfileBO();
            BeanUtils.copyProperties(currentUser, userProfileBO);
            return userProfileBO;
        }


        // 2. 【核心】构造 UpdateWrapper 来实现部分更新
        // 使用 LambdaUpdateWrapper 更现代，且能防止字段名写错
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();

        // 指定 WHERE 条件
        updateWrapper.eq(User::getUserId, currentUserId);

        // 根据 DTO 中的值，动态地设置要更新的字段 (SET a=b, c=d)
        if (userProfileDTO.getNickname() != null) {
            updateWrapper.set(User::getNickname, userProfileDTO.getNickname());
        }
        if (userProfileDTO.getAvatarUrl() != null) {
            updateWrapper.set(User::getAvatarUrl, userProfileDTO.getAvatarUrl());
        }

        // 3. 执行更新
        int updatedRows = userMapper.update( updateWrapper);

        // (可选，但推荐) 检查是否真的更新成功了
        if (updatedRows == 0) {
            throw new RuntimeException("更新用户信息失败，可能用户不存在");
        }

        // 4. 【核心】重新从数据库查询最新的、完整的用户信息
        User updatedUser = userMapper.selectById(currentUserId);

        // 5. 将更新后的完整信息封装到BO中并返回
        UserProfileBO userProfileBO = new UserProfileBO();
        BeanUtils.copyProperties(updatedUser, userProfileBO);

        return userProfileBO;
    }


    /*
     * 返回 productSummaryBO 列表
     * */


    //TODO：实现获取用户收藏列表
    @Override
    public List<ProductSummaryBO> getUserFavorites(Long userId, Integer page, Integer size) {


      List<ProductSummaryBO> productsSummaryBo =    productService.getFavoriteProductsByUserId(userId, page, size);


        return productsSummaryBo;
    }


}




