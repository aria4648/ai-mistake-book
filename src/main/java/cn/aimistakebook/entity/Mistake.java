package cn.aimistakebook.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 错题实体类 —— 对应数据库的 mistake 表
 *
 * ---------- 字段对应关系 ----------
 * 数据库.id                → Mistake.id
 * 数据库.user_id           → Mistake.userId     (下划线转驼峰)
 * 数据库.title             → Mistake.title
 * 数据库.my_answer         → Mistake.myAnswer
 * 数据库.correct_answer    → Mistake.correctAnswer
 * 数据库.analysis          → Mistake.analysis
 * 数据库.subject           → Mistake.subject
 * 数据库.knowledge_point    → Mistake.knowledgePoint
 * 数据库.status            → Mistake.status
 * 数据库.create_time       → Mistake.createTime
 */
@Data
@TableName("mistake")
public class Mistake {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String title;

    private String myAnswer;

    private String correctAnswer;

    private String analysis;

    private String subject;

    private String knowledgePoint;

    private Integer status;

    private LocalDateTime createTime;

    public Mistake() {}

    public Mistake(Long id, Long userId, String title, String myAnswer, String correctAnswer, String analysis, String subject, String knowledgePoint, Integer status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.myAnswer = myAnswer;
        this.correctAnswer = correctAnswer;
        this.analysis = analysis;
        this.subject = subject;
        this.knowledgePoint = knowledgePoint;
        this.status = status;
        this.createTime = LocalDateTime.now();
    }
}
