package cn.aimistakebook.controller;

import cn.aimistakebook.common.Result;
import cn.aimistakebook.entity.Mistake;
import cn.aimistakebook.entity.User;
import cn.aimistakebook.service.AiService;
import cn.aimistakebook.service.MistakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * MistakeController —— 错题相关接口
 *
 * 路径统一以 /api/mistake 开头
 */
@RestController
@RequestMapping("/api/mistake")
public class MistakeController {

    @Autowired
    private MistakeService mistakeService;

    @Autowired
    private AiService aiService;  // 注入 AI 服务

    /**
     * 新增错题
     * POST /api/mistake
     */
    @PostMapping
    public Result<?> add(@RequestBody Mistake mistake, HttpSession session) {
        User loginUser = (User) session.getAttribute("currentUser");
        if (loginUser == null) {
            return Result.error("请先登录");
        }
        mistake.setUserId(loginUser.getId());
        mistakeService.save(mistake);
        return Result.success();
    }

    /**
     * 查询我的错题列表
     * GET /api/mistake?subject=数学
     */
    @GetMapping
    public Result<List<Mistake>> list(@RequestParam(required = false) String subject,
                                      HttpSession session) {
        User loginUser = (User) session.getAttribute("currentUser");
        if (loginUser == null) {
            return Result.error("请先登录");
        }
        List<Mistake> list;
        if (subject != null && !subject.isEmpty()) {
            list = mistakeService.listByUserIdAndSubject(loginUser.getId(), subject);
        } else {
            list = mistakeService.listByUserId(loginUser.getId());
        }
        return Result.success(list);
    }

    /**
     * 查询错题详情
     * GET /api/mistake/1
     */
    @GetMapping("/{id}")
    public Result<Mistake> getById(@PathVariable Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("currentUser");
        if (loginUser == null) {
            return Result.error("请先登录");
        }
        Mistake mistake = mistakeService.getById(id);
        if (mistake == null || !mistake.getUserId().equals(loginUser.getId())) {
            return Result.error("错题不存在");
        }
        return Result.success(mistake);
    }

    /**
     * 更新错题
     * PUT /api/mistake/1
     */
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Mistake mistake,
                            HttpSession session) {
        User loginUser = (User) session.getAttribute("currentUser");
        if (loginUser == null) {
            return Result.error("请先登录");
        }
        Mistake exist = mistakeService.getById(id);
        if (exist == null || !exist.getUserId().equals(loginUser.getId())) {
            return Result.error("错题不存在");
        }
        mistake.setId(id);
        mistake.setUserId(null);
        mistakeService.updateById(mistake);
        return Result.success();
    }

    /**
     * 删除错题
     * DELETE /api/mistake/1
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("currentUser");
        if (loginUser == null) {
            return Result.error("请先登录");
        }
        Mistake exist = mistakeService.getById(id);
        if (exist == null || !exist.getUserId().equals(loginUser.getId())) {
            return Result.error("错题不存在");
        }
        mistakeService.removeById(id);
        return Result.success();
    }

    /**
     * AI 生成错题解析 ⭐ 新功能！
     * POST /api/mistake/1/ai-analysis
     *
     * 流程：
     *   1. 检查登录
     *   2. 拿到错题
     *   3. 调 AI 生成解析
     *   4. 存进数据库
     */
    @PostMapping("/{id}/ai-analysis")
    public Result<?> aiAnalysis(@PathVariable Long id, HttpSession session) {
        // 1. 检查登录
        User loginUser = (User) session.getAttribute("currentUser");
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 2. 拿到错题 + 权限校验
        Mistake mistake = mistakeService.getById(id);
        if (mistake == null || !mistake.getUserId().equals(loginUser.getId())) {
            return Result.error("错题不存在");
        }

        // 3. 调 AI 生成解析（核心！）
        String analysis = aiService.generateAnalysis(
            mistake.getTitle(),
            mistake.getCorrectAnswer()
        );

        // 4. 存进数据库
        mistake.setAnalysis(analysis);
        mistakeService.updateById(mistake);

        // 5. 返回
        return Result.success(mistake);
    }
}
