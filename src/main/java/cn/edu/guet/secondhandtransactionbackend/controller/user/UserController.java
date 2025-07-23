package cn.edu.guet.secondhandtransactionbackend.controller.user;

import cn.edu.guet.secondhandtransactionbackend.controller.api.UsersApi;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.dto.UpdateUserDTO;
import cn.edu.guet.secondhandtransactionbackend.dto.UserProfileVO;
import cn.edu.guet.secondhandtransactionbackend.dto.UsersMeFavoritesPostRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
    public ResponseEntity<List<ProductSummaryVO>> usersMeFavoritesGet(Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<Void> usersMeFavoritesPost(UsersMeFavoritesPostRequest usersMeFavoritesPostRequest) {
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
    public ResponseEntity<UserProfileVO> usersMePatch(UpdateUserDTO updateUserDTO) {
        return null;
    }
}
