package local.kongyu.mybatisFlex.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import local.kongyu.mybatisFlex.entity.MyUser;
import local.kongyu.mybatisFlex.mapper.MyUserMapper;
import local.kongyu.mybatisFlex.service.MyUserService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author 孔余
 * @since 1.0.0
 */
@Service
public class MyUserServiceImpl extends ServiceImpl<MyUserMapper, MyUser> implements MyUserService {

}
