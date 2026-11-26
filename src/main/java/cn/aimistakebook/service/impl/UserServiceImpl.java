package cn.aimistakebook.service.impl;

import cn.aimistakebook.entity.User;
import cn.aimistakebook.mapper.UserMapper;
import cn.aimistakebook.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * UserServiceImpl —— UserService 的实现类
 *
 * 继承 ServiceImpl<UserMapper, User>
 *   - 第一个参数：对应的 Mapper
 *   - 第二个参数：对应的实体类
 *
 * 这样就自动拥有了 Mapper 层的所有增删改查能力
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 根据用户名查用户
     * QueryWrapper 是 MyBatis-Plus 的查询条件构造器
     *   .eq("username", username) → 相当于 SQL: WHERE username = 'xxx'
     */
    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        return getOne(qw);  // getOne 是 IService 自带的方法
    }

    /**
     * 登录校验
     * 密码用 MD5 加密后和数据库存的对比（MD5是单向的，只能加密不能解密）
     */
    @Override
    public User login(String username, String password) {
        User user = getByUsername(username);
        if (user == null) {
            return null;              // 用户不存在
        }
        // DigestUtils.md5DigestAsHex 是 Spring 自带的 MD5 工具
        String inputMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!inputMd5.equals(user.getPassword())) {
            return null;              // 密码不对
        }
        return user;                   // 登录成功
    }

    /**
     * 注册
     */
    @Override
    public boolean register(User user) {
        // 1. 检查用户名是否已存在
        if (getByUsername(user.getUsername()) != null) {
            return false;
        }
        // 2. 密码 MD5 加密
        String md5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5);
        // 3. 保存（save 是 ServiceImpl 自带的）
        return save(user);
    }
}
