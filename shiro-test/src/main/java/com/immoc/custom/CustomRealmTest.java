package com.immoc.custom;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author wangjf
 * @date 2018/4/30 0030.
 */
public class CustomRealmTest {

    @Test
    public void testAuthentication(){

        CustomRealm customRealm = new CustomRealm();

        // 1. 构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        // shiro进行加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);

        // 2. 主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("wangjf", "1234567");

        // 登录操作
        subject.login(token);
        System.out.println("isAuthenticated >>> " + subject.isAuthenticated());

        // 检查角色
        subject.checkRoles("admin");

        // 检查权限
        subject.checkPermissions("user:delete");
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("1234567", "wangjf");
        System.out.println(md5Hash.toString());
    }

}
