package local.kongyu.SaToken.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录认证: https://sa-token.cc/doc.html#/use/login-auth
 *
 * @author 孔余
 * @since 2024-01-18 22:43
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @GetMapping("doLogin")
    public SaResult  doLogin(String username, String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
//            StpUtil.login(10001);
            StpUtil.login(10001, new SaLoginModel()
                    .setDevice("PC")                // 此次登录的客户端设备类型, 用于[同端互斥登录]时指定此次登录的设备类型
                    .setIsLastingCookie(true)        // 是否为持久Cookie（临时Cookie在浏览器关闭时会自动删除，持久Cookie在重新打开后依然存在）
                    .setTimeout(-1)    // 指定此次登录token的有效期, 单位:秒 （如未指定，自动取全局配置的 timeout 值）
                    .setToken("123456789") // 预定此次登录的生成的Token
                    .setIsWriteHeader(false)         // 是否在登录后将 Token 写入到响应头
            );
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return SaResult.data(tokenInfo);
        }
        return SaResult.error();
    }

    // 查询登录状态，浏览器访问
    @GetMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    // 查询 Token 信息
    // 此接口加上了 @SaIgnore 可以游客访问
    @SaIgnore
    @GetMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 测试注销
    @GetMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    // 获取：当前账号所拥有的权限集合
    @GetMapping("getPermissionList")
    public SaResult getPermissionList() {
        return SaResult.data(StpUtil.getPermissionList());
    }

}

