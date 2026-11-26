package cn.aimistakebook.controller;

import cn.aimistakebook.common.Result;
import cn.aimistakebook.entity.User;
import cn.aimistakebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * UserController —— 用户相关接口
 *
 * 为什么用 @RestController？
 *   @RestController = @Controller + @ResponseBody
 *   自动把返回值转成 JSON 格式
 *
 * 接口路径统一以 /api/user 开头，比如：
 *   POST /api/user/register  注册
 *   POST /api/user/login     登录
 *   GET  /api/user/info      获取当前登录用户信息
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;  // 注入 Service

    /**
     * 注册接口
     * 请求方式: POST
     * 请求地址: /api/user/register
     * 请求体:   {"username":"xxx", "password":"xxx", "nickname":"xxx"}
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        boolean ok = userService.register(user);
        if (ok) {
            return Result.success();
        }
        return Result.error("用户名已存在");
    }

    /**
     * 登录接口
     * 登录成功后把用户信息存到 Session 里，后面就不用每次都传密码了
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user, HttpSession session) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser == null) {
            return Result.error("用户名或密码错误");
        }
        // 登录成功，把用户信息存在 session 中（key 叫 "currentUser"）
        session.setAttribute("currentUser", loginUser);
        // 返回给前端时，密码不要返回（安全）
        loginUser.setPassword(null);
        return Result.success(loginUser);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<User> info(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return Result.error("未登录");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.removeAttribute("currentUser");  // 从 session 中移除
        return Result.success();
    }
}
