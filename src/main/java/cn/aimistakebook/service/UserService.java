package cn.aimistakebook.service;

import cn.aimistakebook.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * UserService —— 用户业务逻辑接口
 *
 * 继承 IService<User> 之后，自动拥有：
 *   save(User)           → 新增用户
 *   updateById(User)     → 更新
 *   removeById(id)       → 删除
 *   getById(id)          → 根据id查询
 *   list()               → 查全部
 *   count()              → 总数
 *   ...等等
 *
 * 这个接口里我们只写"自己特有的方法"
 */
public interface UserService extends IService<User> {

    /** 根据用户名查用户（登录时要用） */
    User getByUsername(String username);

    /** 登录校验，成功返回用户，失败返回null */
    User login(String username, String password);

    /** 注册用户，true=成功 false=用户名已存在 */
    boolean register(User user);
}
