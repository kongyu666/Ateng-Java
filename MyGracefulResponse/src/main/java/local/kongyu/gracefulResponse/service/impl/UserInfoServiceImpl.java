package local.kongyu.gracefulResponse.service.impl;

import com.feiniaojin.gracefulresponse.GracefulResponse;
import local.kongyu.gracefulResponse.config.AppStatusCodeEnum;
import local.kongyu.gracefulResponse.entity.UserInfo;
import local.kongyu.gracefulResponse.exception.NotFoundException;
import local.kongyu.gracefulResponse.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 10:33
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public UserInfo getUser(Long id) {
        // 使用断言来验证数据的准确性
        GracefulResponse.wrapAssert(()->Assert.isTrue(id!=-1,"id等于-1"));
        GracefulResponse.wrapAssert("90101",()->Assert.isTrue(id!=-2,"id等于-2"));

        // 抛出GracefulResponseException异常
        if (id == 1) {
            //GracefulResponse.raiseException("自定义的错误码", "自定义的错误信息");
            GracefulResponse.raiseException(AppStatusCodeEnum.AUTH_ACCESS_TOKEN_EXPIRED.getCode(), AppStatusCodeEnum.AUTH_ACCESS_TOKEN_EXPIRED.getDescription());
        }

        // 抛出自定义异常
        if (id == 0) {
            throw new NotFoundException();
        }
        return UserInfo.builder().id(id).name("阿腾").age(24).build();
    }
}
