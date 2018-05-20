package com.imooc.shiro.realm;

import com.imooc.dao.UserDao;
import com.imooc.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangjf
 * @date 2018/4/30 0030.
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    // 授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = (String) principals.getPrimaryPrincipal();

        // 从数据库中获取角色数据
        Set<String> roles = getRolesByUserName(username);
        // 从数据库中获取权限信息
        Set<String> permissions = getPermissionsByRoleName(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);

        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByRoleName(String rolename){
        Set<String> roles = new HashSet<String>();
        roles.add("user:update");
        roles.add("user:delete");
        return roles;
    }

    private Set<String> getRolesByUserName(String username){
        List<String> list = userDao.queryRolesByUserName(username);
        Set<String> sets = new HashSet<String>(list);
        return sets;
    }

    // 认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        // 1. 从主体传过来的认证信息中，获得用户名
        String username = (String) token.getPrincipal();

        // 2. 通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(username);
        if(password == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("wangjf"));
        return authenticationInfo;
    }

    /**
     * 模拟数据库查询凭证
     * */
    private String getPasswordByUserName(String userName){
        User user = userDao.getUserByUserName(userName);
        if(user != null){
            return user.getPassword();
        }
        return null;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("1234567", "wangjf");
        // 98eff15c480cb1bc094d77e24f94ad71
        System.out.println(md5Hash.toString());
    }

}
