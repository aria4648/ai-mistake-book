package cn.aimistakebook.service.impl;

import cn.aimistakebook.entity.Mistake;
import cn.aimistakebook.mapper.MistakeMapper;
import cn.aimistakebook.service.MistakeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MistakeServiceImpl —— 错题业务实现
 */
@Service
public class MistakeServiceImpl extends ServiceImpl<MistakeMapper, Mistake> implements MistakeService {

    /**
     * 查询某个用户的所有错题
     * 按录入时间倒序（最新的排最前面）
     */
    @Override
    public List<Mistake> listByUserId(Long userId) {
        QueryWrapper<Mistake> qw = new QueryWrapper<>();
        qw.eq("user_id", userId)         // WHERE user_id = ?
          .orderByDesc("create_time");   // ORDER BY create_time DESC
        return list(qw);
    }

    /**
     * 查询某个用户 + 某个学科的错题
     */
    @Override
    public List<Mistake> listByUserIdAndSubject(Long userId, String subject) {
        QueryWrapper<Mistake> qw = new QueryWrapper<>();
        qw.eq("user_id", userId)
          .eq("subject", subject)
          .orderByDesc("create_time");
        return list(qw);
    }
}
