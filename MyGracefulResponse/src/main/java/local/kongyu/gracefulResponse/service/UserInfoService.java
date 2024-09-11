package local.kongyu.gracefulResponse.service;

import local.kongyu.gracefulResponse.entity.UserInfo;

public interface UserInfoService {
    UserInfo getUser(Long id);
}
