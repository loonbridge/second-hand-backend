package cn.edu.guet.secondhandtransactionbackend.controller.user;

import cn.edu.guet.secondhandtransactionbackend.controller.api.UsersApi;
import cn.edu.guet.secondhandtransactionbackend.dto.*;
import org.springframework.http.ResponseEntity;

public class UserController  implements UsersApi {

    @Override
    public ResponseEntity<Void> usersIdFollowDelete(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> usersIdFollowPost(String id) {
        return null;
    }

    @Override
    public ResponseEntity<ProductListVO> usersMeFavoritesGet(Integer page, Integer size) {
        return null;
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
        return null;
    }

    @Override
    public ResponseEntity<UserProfileVO> usersMePatch(UpdateUserRequest updateUserRequest) {
        return null;
    }
}
