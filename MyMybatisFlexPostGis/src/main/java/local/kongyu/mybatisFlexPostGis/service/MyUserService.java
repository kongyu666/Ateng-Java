package local.kongyu.mybatisFlexPostGis.service;

import com.mybatisflex.core.service.IService;
import local.kongyu.mybatisFlexPostGis.entity.MyUserEntity;

import java.io.Serializable;
import java.util.List;

public interface MyUserService extends IService<MyUserEntity> {
    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    MyUserEntity getUser(Serializable id);

    /**
     * 查询所有用户列表
     * @return
     */
    List<MyUserEntity> listUser();

    /**
     * 分页查询
     * @param currentPage 当前页
     * @param pageSize 每页显示的记录数
     * @return
     */
    List<MyUserEntity> listPageUser(long currentPage, long pageSize);

}
