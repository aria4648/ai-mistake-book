package cn.aimistakebook.service;

/**
 * AI 服务接口 —— 调用大模型生成解析
 *
 * 为什么不继承 IService？
 *   因为 AiService 不是操作数据库的服务，
 *   它是调百炼大模型 API 的，和 MyBatis-Plus 没关系
 */
public interface AiService {

    /**
     * 根据题目和正确答案，让 AI 生成解析
     *
     * @param title         题目
     * @param correctAnswer 正确答案
     * @return AI 生成的解析文字
     */
    String generateAnalysis(String title, String correctAnswer);
}
