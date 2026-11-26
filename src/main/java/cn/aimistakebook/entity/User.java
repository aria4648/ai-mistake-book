package cn.aimistakebook.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类 —— 对应数据库 mistake_book 库的 user 表
 *
 * 实体类与数据库表的映射关系（面试必考点）：
 *   1. 表的每一列 → 类的一个属性
 *   2. 数据库下划线命名（create_time）→ Java 驼峰命名（createTime）
 *      （application.yml 中 map-underscore-to-camel-case: true 自动完成转换）
 *   3. 数据库 BIGINT → Java Long（包装类，可 null）
 *   4. 数据库 DATETIME → Java LocalDateTime（现代 Java 推荐，替代老旧的 Date）
 */
@Data
@TableName("user")
public class User {

    /** 主键，对应数据库 id 字段，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名，对应数据库 username，唯一不可重复 */
    private String username;

    /** 密码，对应数据库 password，存 MD5 加密后的值 */
    private String password;

    /** 昵称，对应数据库 nickname，允许为空 */
    private String nickname;

    /** 创建时间，对应数据库 create_time，自动填入当前时间 */
    private LocalDateTime createTime;

    /** 无参构造器 —— MyBatis-Plus 反射创建对象时必须要有 */
    public User() {}

    /** 全参构造器 —— 方便一次性创建并赋值 */
    public User(Long id, String username, String password, String nickname, LocalDateTime createTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.createTime = createTime;
    }
}
