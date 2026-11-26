package cn.aimistakebook.service;

import cn.aimistakebook.entity.Mistake;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * MistakeService —— 错题业务逻辑接口
 */
public interface MistakeService extends IService<Mistake> {

    /** 查询某个用户的所有错题 */
    List<Mistake> listByUserId(Long userId);

    /** 查询某个用户 + 某个学科的错题 */
    List<Mistake> listByUserIdAndSubject(Long userId, String subject);
}
