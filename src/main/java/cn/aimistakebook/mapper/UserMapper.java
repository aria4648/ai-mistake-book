package cn.aimistakebook.mapper;

import cn.aimistakebook.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper —— 用户表的数据库操作接口
 *
 * 继承 BaseMapper<User> 之后，MyBatis-Plus 自动帮你拥有：
 *   insert(User)              → 新增用户
 *   deleteById(id)            → 根据id删除
 *   updateById(User)          → 根据id更新
 *   selectById(id)            → 根据id查询
 *   selectOne(查询条件)        → 条件查一条（比如根据用户名查）
 *   selectList(查询条件)       → 条件查多条
 *   selectCount(查询条件)      → 查询总数
 *   ...等等
 *
 * 你不需要写任何 SQL，框架自动搞定！
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
