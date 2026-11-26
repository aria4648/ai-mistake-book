package cn.aimistakebook.service.impl;

import cn.aimistakebook.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 服务实现 —— 调用阿里云百炼大模型
 *
 * 百炼 API 文档参考：https://help.aliyun.com/zh/model-studio/
 */
@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private RestTemplate restTemplate;  // 注入我们刚才配置的 HTTP 客户端

    // 从 application.yml 读取配置
    @Value("${bailian.api-key}")
    private String apiKey;

    @Value("${bailian.endpoint}")
    private String endpoint;

    @Value("${bailian.text-model}")
    private String model;

    @Override
    public String generateAnalysis(String title, String correctAnswer) {
        // 如果没配置 API Key，返回一个假的解析（方便测试）
        if (apiKey == null || apiKey.isEmpty()) {
            return "【示例解析】\n\n题目：" + title + "\n\n正确答案：" + correctAnswer +
                   "\n\n解析：请先在 application.yml 里配置 bailian.api-key";
        }

        try {
            // ========== 1. 构造请求体 ==========
            // 百炼的 DashScope API 格式要求
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);  // 模型名：qwen-plus

            // input 里放 messages
            Map<String, Object> input = new HashMap<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");  // 角色：用户
            // 给 AI 的提示词（prompt）
            message.put("content",
                "请帮我详细解析这道错题：\n\n" +
                "题目：" + title + "\n\n" +
                "正确答案：" + correctAnswer + "\n\n" +
                "请告诉我：1. 解题思路 2. 涉及的知识点 3. 容易出错的地方");

            input.put("messages", new Object[]{message});
            requestBody.put("input", input);

            // ========== 2. 构造请求头 ==========
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);  // 百炼要求 Bearer 前缀

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // ========== 3. 发送请求 ==========
            ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, entity, Map.class);

            // ========== 4. 解析返回 ==========
            System.out.println("===== [DEBUG] response.getStatusCode() = " + response.getStatusCode());
            if (response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                System.out.println("===== [DEBUG] body keys = " + body.keySet());
                Object outputObj = body.get("output");
                System.out.println("===== [DEBUG] outputObj type = " + (outputObj == null ? "null" : outputObj.getClass().getName()));
                System.out.println("===== [DEBUG] outputObj = " + outputObj);

                if (outputObj instanceof Map) {
                    Map<String, Object> output = (Map<String, Object>) outputObj;
                    System.out.println("===== [DEBUG] output keys = " + output.keySet());
                    Object text = output.get("text");
                    System.out.println("===== [DEBUG] text = " + text);
                    if (text != null) {
                        return text.toString();
                    }
                }
            }

            return "AI 解析失败：返回格式异常";

        } catch (Exception e) {
            e.printStackTrace();
            return "AI 解析出错：" + e.getMessage();
        }
    }
}
