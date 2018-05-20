package com.imooc.dao;

import com.imooc.vo.User;

import java.util.List;

/**
 * @author wangjf
 * @date 2018/5/20 0020.
 */
public interface UserDao {

    public User getUserByUserName(String userName);

    public List<String> queryRolesByUserName(String userName);

}
