package local.kongyu.mybatisFlexPostGis.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import local.kongyu.mybatisFlexPostGis.entity.MyUserEntity;
import local.kongyu.mybatisFlexPostGis.mapper.MyUserMapper;
import local.kongyu.mybatisFlexPostGis.service.MyUserService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2023-07-27 14:40
 */
@Service
public class MyUserServiceImpl extends ServiceImpl<MyUserMapper, MyUserEntity> implements MyUserService {

    @Override
    public MyUserEntity getUser(Serializable id) {
        return this.getById(id);
    }

    @Override
    public List<MyUserEntity> listUser() {
        return this.list();
    }

    @Override
    public List<MyUserEntity> listPageUser(long pageNumber, long pageSize) {
        Page<MyUserEntity> page = new Page<>();
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        Page<MyUserEntity> userPage = this.page(page, QueryWrapper.create());
        return userPage.getRecords();
    }

}
