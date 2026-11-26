package cn.aimistakebook.mapper;

import cn.aimistakebook.entity.Mistake;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * MistakeMapper —— 错题表的数据库操作接口
 *
 * 同样继承 BaseMapper<Mistake>，自动拥有增删改查
 */
@Mapper
public interface MistakeMapper extends BaseMapper<Mistake> {
}
