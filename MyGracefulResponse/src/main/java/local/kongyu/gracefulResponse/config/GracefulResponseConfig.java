package local.kongyu.gracefulResponse.config;

import com.feiniaojin.gracefulresponse.AbstractExceptionAliasRegisterConfig;
import com.feiniaojin.gracefulresponse.EnableGracefulResponse;
import com.feiniaojin.gracefulresponse.ExceptionAliasRegister;
import local.kongyu.gracefulResponse.exception.*;
import org.springframework.context.annotation.Configuration;

/**
 * 引入@EnableGracefulResponse注解
 *
 * @author 孔余
 * @since 2024-01-17 10:20
 */
@EnableGracefulResponse
@Configuration
public class GracefulResponseConfig extends AbstractExceptionAliasRegisterConfig {
    @Override
    protected void registerAlias(ExceptionAliasRegister aliasRegister) {
        //注册异常别名
        aliasRegister.doRegisterExceptionAlias(MyNoHandlerFoundException.class);
        aliasRegister.doRegisterExceptionAlias(MyArithmeticException.class);
        aliasRegister.doRegisterExceptionAlias(MyIndexOutOfBoundsException.class);
        aliasRegister.doRegisterExceptionAlias(MyClassCastException.class);
        aliasRegister.doRegisterExceptionAlias(MyIllegalArgumentException.class);
        aliasRegister.doRegisterExceptionAlias(MyIOException.class);
        aliasRegister.doRegisterExceptionAlias(MyNullPointerException.class);
        aliasRegister.doRegisterExceptionAlias(MyUnsupportedOperationException.class);
        aliasRegister.doRegisterExceptionAlias(MyTypeMismatchException.class);
    }
}
