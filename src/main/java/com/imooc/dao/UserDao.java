package com.imooc.dao;

import com.imooc.bean.User;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/8 10:57
 */
public interface UserDao {
    /**
     * 搜索用户集合
     * @param user 待搜索条件
     * @return 用户集合
     */
    List<User> selectUser(User user);

    /**
     * 根据手机号选择用户
     * @param phone 手机号
     * @return 用户
     */
    User selectUserByPhone(Long phone);
}
