package cn.edu.guet.secondhandtransactionbackend.controller.user;

import cn.edu.guet.secondhandtransactionbackend.assembler.ProductAssembler;
import cn.edu.guet.secondhandtransactionbackend.assembler.UserAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.UsersApi;
import cn.edu.guet.secondhandtransactionbackend.dto.AddFavoriteRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.UpdateUserRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.UserProfileVO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileDTO;
import cn.edu.guet.secondhandtransactionbackend.entity.UserProductFavoriteFnn;
import cn.edu.guet.secondhandtransactionbackend.entity.UserUserFollowFnn;
import cn.edu.guet.secondhandtransactionbackend.service.UserProductFavoriteFnnService;
import cn.edu.guet.secondhandtransactionbackend.service.UserService;
import cn.edu.guet.secondhandtransactionbackend.service.UserUserFollowFnnService;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    private final UserProductFavoriteFnnService userProductFavoriteFnnService;

    private  final UserUserFollowFnnService userUserFollowFnnService;


    @Autowired
    public UserController(UserUserFollowFnnService userUserFollowFnnService, UserProductFavoriteFnnService userProductFavoriteFnnService, UserAssembler userAssembler, ProductAssembler productAssembler, AuthenticationHelper authenticationHelper,
                          UserService userService) {
        // 通过构造函数注入 AuthenticationHelper 和 UserService 实例
        this.authenticationHelper = authenticationHelper;
    this.productAssembler = productAssembler; // 这里可以注入 ProductAssembler 实例
        this.userService = userService; // 这里可以注入 UserService 实例
        this.userAssembler = userAssembler; // 这里可以注入 UserAssembler 实例
        this.userProductFavoriteFnnService = userProductFavoriteFnnService; // 这里可以注入 UserProductFavoriteFnnService 实例
        this.userUserFollowFnnService = userUserFollowFnnService; // 这里可以注入 UserUserFollowFnnService 实例
    }



    /*
     *
     * 当前用户取消关注特定用户*/
    @Override
    public ResponseEntity<Void> usersIdFollowDelete(String id) {

        //获取当前ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            //如果没有认证用户，返回未授权
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }
        //如果有认证用户，获取用户ID
        Long currentId = currentUserId.get();

        //调用用户服务取消关注用户

        boolean remove = userUserFollowFnnService.remove(new LambdaQueryWrapper<UserUserFollowFnn>()
                .eq(UserUserFollowFnn::getFollowerUserId, currentId)
                .eq(UserUserFollowFnn::getFollowingUserId, Long.valueOf(id))
        );
        return ResponseEntity.status(remove ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).build();
    }


    /*当前用户，
     * 关注特定用户*/
    @Override
    public ResponseEntity<Void> usersIdFollowPost(String id) {

        //获取当前ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            //如果没有认证用户，返回未授权
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //如果有认证用户，获取用户ID
        Long currentId = currentUserId.get();

        //自己不能关注自己
        if (currentId.equals(Long.valueOf(id))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //调用用户服务关注用户
        boolean save = userUserFollowFnnService.save(new UserUserFollowFnn()
                .setFollowerUserId(currentId)
                .setFollowingUserId(Long.valueOf(id))
        );

        return ResponseEntity.status(save ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).build();
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

        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();

        if (currentUserId.isEmpty()) {
            //如果没有认证用户，返回未授权
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //如果有认证用户，获取用户ID

       boolean result= userProductFavoriteFnnService.addUserProductFavorite(
                currentUserId.get(),
                Long.valueOf(addFavoriteRequest.getProductId())
        );


        return ResponseEntity.status(result ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).build();
    }



    /*删除收藏的商品*/
    @Override
    public ResponseEntity<Void> usersMeFavoritesProductIdDelete(String productId) {

        LambdaQueryWrapper<UserProductFavoriteFnn> userProductFavoriteFnnLambdaQueryWrapper = new LambdaQueryWrapper<>();

        userProductFavoriteFnnLambdaQueryWrapper.eq(UserProductFavoriteFnn::getProductId,
                Long.valueOf(productId));

        boolean remove = userProductFavoriteFnnService.remove(userProductFavoriteFnnLambdaQueryWrapper);

        return ResponseEntity.status(remove ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).build();
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
