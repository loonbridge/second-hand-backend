package cn.edu.guet.secondhandtransactionbackend.controller.user;

import cn.edu.guet.secondhandtransactionbackend.assembler.ProductAssembler;
import cn.edu.guet.secondhandtransactionbackend.assembler.UserAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.UsersApi;
import cn.edu.guet.secondhandtransactionbackend.dto.*;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductListBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileDTO;
import cn.edu.guet.secondhandtransactionbackend.service.UserService;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController  implements UsersApi {

    private  final  AuthenticationHelper authenticationHelper;

    private final UserService userService;

    private  final  ProductAssembler productAssembler;

    private final UserAssembler userAssembler;


    @Autowired
    public UserController(UserAssembler userAssembler,ProductAssembler productAssembler,AuthenticationHelper authenticationHelper,
                          UserService userService) {
        // 通过构造函数注入 AuthenticationHelper 和 UserService 实例
        this.authenticationHelper = authenticationHelper;
    this.productAssembler = productAssembler; // 这里可以注入 ProductAssembler 实例
        this.userService = userService; // 这里可以注入 UserService 实例
        this.userAssembler = userAssembler; // 这里可以注入 UserAssembler 实例
    }

    @Override
    public ResponseEntity<Void> usersIdFollowDelete(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> usersIdFollowPost(String id) {
        return null;
    }

    /*
     * 获取我的收藏列表
     * */
    @Override
    public ResponseEntity<ProductListVO> usersMeFavoritesGet(Integer page, Integer size) {

        //获取当前ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();

        if (currentUserId.isEmpty()) {
            //如果没有认证用户，返回未授权
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //如果有认证用户，获取用户ID
        Long userId = currentUserId.get();
        //调用用户服务获取收藏列表
        List<ProductSummaryBO> productSummaryBOs = userService.getUserFavorites(userId, page, size);


        ProductListVO productListVO = productAssembler.toProductListVO(productSummaryBOs, size);

        return Optional.ofNullable(productListVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<Void> usersMeFavoritesPost(AddFavoriteRequest addFavoriteRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Void> usersMeFavoritesProductIdDelete(String productId) {
        return null;
    }

    @Override
    public ResponseEntity<UserProfileVO> usersMeGet() {

        //获取认证上下文拿到用户信息
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();


        if (currentUserId.isEmpty()) {
            //如果没有认证用户，返回未授权
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //如果有认证用户，获取用户ID

        Long userId = currentUserId.get();


        UserProfileBO userProfileById = userService.getUserProfileById(userId);

        UserProfileVO userProfileVO = userAssembler.toUserProfileVO(userProfileById);

        return Optional.ofNullable(userProfileVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    //更新用户信息，增量更新
    @Override
    public ResponseEntity<UserProfileVO> usersMePatch(UpdateUserRequest updateUserRequest) {

        //获取当前用户id

        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();


        UserProfileDTO userProfileDTO=  userAssembler.toUserProfileDTO(updateUserRequest);
        UserProfileBO userProfileBO= userService.pathchUser(userProfileDTO,currentUserId.get());

        UserProfileVO userProfileVO = userAssembler.toUserProfileVO(userProfileBO);
        return Optional.ofNullable(userProfileVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
