package local.kongyu.validator.controller;

import local.kongyu.validator.entity.Account;
import local.kongyu.validator.utils.Result;
import local.kongyu.validator.validation.AddGroup;
import local.kongyu.validator.validation.UpdateGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account") // 基础路径为/account
public class AccountController {

    /**
     * 创建账户的接口，使用CreateAccountGroup进行校验。
     *
     * @param account 账户信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result createAccount(@Validated(AddGroup.class) @RequestBody Account account) {
        // 在这里处理账户创建逻辑，比如保存账户到数据库
        return Result.success();
    }

    /**
     * 更新账户信息的接口，使用UpdateAccountGroup进行校验。
     *
     * @param account 账户信息
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result updateAccount(@Validated(UpdateGroup.class) @RequestBody Account account) {
        // 在这里处理账户更新逻辑，比如更新账户信息到数据库
        return Result.success();
    }
}