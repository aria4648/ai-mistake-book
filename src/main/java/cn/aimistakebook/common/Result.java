package cn.aimistakebook.common;

import lombok.Data;

/**
 * 统一返回结果 —— 后端所有接口都返回这个格式
 *
 * 为什么要统一？
 *   如果不统一，有的接口返回 {"name":"zhangsan"}，有的返回 "ok"
 *   前端处理起来就很乱。统一成一个格式，前端一套代码搞定。
 *
 * 格式:
 * {
 *   "code": 200,           // 200成功，其他失败
 *   "msg": "操作成功",      // 提示信息
 *   "data": {...}          // 真正的数据（可能是对象、列表、null）
 * }
 */
@Data
public class Result<T> {       // <T> 泛型，表示 data 可以是任意类型

    private Integer code;       // 状态码
    private String msg;         // 提示信息
    private T data;             // 返回数据

    // 成功（有数据）
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }

    // 成功（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败
    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }
}
