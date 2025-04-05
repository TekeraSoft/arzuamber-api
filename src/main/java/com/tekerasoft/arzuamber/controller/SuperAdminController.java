package com.tekerasoft.arzuamber.controller;

import com.tekerasoft.arzuamber.dto.UserAdminDto;
import com.tekerasoft.arzuamber.dto.request.PriceUpdatePercentageRequest;
import com.tekerasoft.arzuamber.dto.response.ApiResponse;
import com.tekerasoft.arzuamber.model.Role;
import com.tekerasoft.arzuamber.service.ProductService;
import com.tekerasoft.arzuamber.service.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/super-admin")
public class SuperAdminController {

    private final UserService userService;
    private final ProductService productService;

    public SuperAdminController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<PagedModel<EntityModel<UserAdminDto>>> getAllUsers(@RequestParam String lang,@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(userService.getUsers(page,size));
    }

    @PatchMapping("/change-role")
    public ResponseEntity<ApiResponse<?>> changeRole(@RequestParam String id, @RequestParam Role role) {
        return ResponseEntity.ok(userService.changeUserRole(id,role));
    }

    @PutMapping("update-price-by-percentage")
    public ResponseEntity<ApiResponse<?>> updatePriceByPercentage(@RequestBody PriceUpdatePercentageRequest percentageRequest) {
        return ResponseEntity.ok(productService.updateAllToursPrice(percentageRequest.getPercentage()));
    }

}
