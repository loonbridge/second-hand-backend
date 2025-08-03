package cn.edu.guet.secondhandtransactionbackend.controller.address;

import cn.edu.guet.secondhandtransactionbackend.assembler.AddressAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.AddressesApi;
import cn.edu.guet.secondhandtransactionbackend.dto.AddressVO;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateAddressRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.UpdateAddressRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.address.AddressBO;
import cn.edu.guet.secondhandtransactionbackend.dto.address.AddressDTO;
import cn.edu.guet.secondhandtransactionbackend.service.AddressService;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AddressController implements AddressesApi {

    private final AuthenticationHelper authenticationHelper;
    private final AddressService addressService;
    private final AddressAssembler addressAssembler;

    @Autowired
    public AddressController(AuthenticationHelper authenticationHelper,
                             AddressService addressService,
                             AddressAssembler addressAssembler) {
        this.authenticationHelper = authenticationHelper;
        this.addressService = addressService;
        this.addressAssembler = addressAssembler;
    }

    @Override
    public ResponseEntity<Void> usersMeAddressesAddressIdDelete(String addressId) {
        // 获取当前用户ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = currentUserId.get();
        boolean deleted = addressService.deleteAddress(Long.valueOf(addressId), userId);

        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<AddressVO> usersMeAddressesAddressIdPut(String addressId, UpdateAddressRequest updateAddressRequest) {
        // 获取当前用户ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = currentUserId.get();
        AddressDTO addressDTO = addressAssembler.toAddressDTO(updateAddressRequest);
        AddressBO updatedAddress = addressService.updateAddress(Long.valueOf(addressId), addressDTO, userId);

        if (updatedAddress == null) {
            return ResponseEntity.notFound().build();
        }

        AddressVO addressVO = addressAssembler.toAddressVO(updatedAddress);
        return ResponseEntity.ok(addressVO);
    }

    @Override
    public ResponseEntity<List<AddressVO>> usersMeAddressesGet() {
        // 获取当前用户ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = currentUserId.get();
        List<AddressBO> addresses = addressService.getUserAddresses(userId);

        List<AddressVO> addressVOs = addresses.stream()
                .map(addressAssembler::toAddressVO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(addressVOs);
    }

    @Override
    public ResponseEntity<AddressVO> usersMeAddressesPost(CreateAddressRequest createAddressRequest) {
        // 获取当前用户ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = currentUserId.get();
        AddressDTO addressDTO = addressAssembler.toAddressDTO(createAddressRequest);
        AddressBO createdAddress = addressService.createAddress(addressDTO, userId);

        AddressVO addressVO = addressAssembler.toAddressVO(createdAddress);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressVO);
    }
}
