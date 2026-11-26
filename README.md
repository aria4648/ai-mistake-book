# 🤖 AI 错题本后端

基于 Spring Boot + MyBatis-Plus + MySQL 的 AI 错题管理系统后端，集成**阿里云百炼大模型**实现错题智能解析。

## 🛠 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 1.8 | 开发语言 |
| Spring Boot | 2.7.6 | 基础框架 |
| MyBatis-Plus | 3.5.5 | ORM 增强工具 |
| MySQL | 8.x | 数据库 |
| Lombok | - | 简化代码 |
| RestTemplate | Spring 内置 | 调大模型 API |
| 阿里云百炼 | qwen-plus | AI 大模型 |

## 🏗 项目结构（经典四层架构）

```
cn.aimistakebook
├── AiMistakeBookApplication.java   # 启动类
├── common/
│   └── Result.java                 # 统一返回格式 Result<T>
├── config/
│   └── RestTemplateConfig.java     # HTTP 客户端配置
├── controller/                     # 🎯 控制层：处理 HTTP 请求
│   ├── UserController.java         #   用户登录注册
│   └── MistakeController.java      #   错题增删改查 + AI 解析
├── entity/                         # 📦 实体层：数据库表映射
│   ├── User.java                   #   用户表
│   └── Mistake.java                #   错题表
├── mapper/                         # 🔌 数据层：继承 BaseMapper
│   ├── UserMapper.java
│   └── MistakeMapper.java
└── service/                        # ⚙️ 业务层
    ├── AiService.java              #   AI 服务接口
    ├── UserService.java
    ├── MistakeService.java
    └── impl/
        ├── AiServiceImpl.java      #   调百炼大模型
        ├── UserServiceImpl.java
        └── MistakeServiceImpl.java
```

**四层架构流转：** `Controller → Service → Mapper → MySQL`

## 🚀 快速启动

### 1. 环境准备

- JDK 1.8+
- Maven 3.6+
- MySQL 8.x
- （可选）阿里云百炼 API Key

### 2. 初始化数据库

```bash
mysql -u root -p < sql/init.sql
```

自动创建 `mistake_book` 库、`user` 表、`mistake` 表，插入 3 条示例错题和 test 用户（密码：`123456`）。

### 3. 配置（可选）

`src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    password: ${DB_PASSWORD}            # 从环境变量读取数据库密码

bailian:
  api-key: ${BAILIAN_API_KEY}           # 从环境变量读取百炼 API Key
```

**必须配置环境变量后才能启动**，Windows 设置方式：

```powershell
# 用户级环境变量（永久生效）
[Environment]::SetEnvironmentVariable("DB_PASSWORD", "你的MySQL密码", "User")
[Environment]::SetEnvironmentVariable("BAILIAN_API_KEY", "你的百炼APIKey", "User")
# 设置完重启 IDEA
```

也可以创建 `application-local.yml`（已被 .gitignore 排除，本地开发用，**不会提交到 Git**）：

```yaml
spring:
  datasource:
    password: 你的MySQL密码
bailian:
  api-key: 你的百炼APIKey
```

### 4. 启动

```bash
mvn spring-boot:run
```

访问 http://localhost:8080/ 即可进入前端页面。

## 📋 接口列表

### 用户模块

| 方法 | URL | 说明 |
|------|-----|------|
| POST | `/api/user/register` | 注册（MD5 加密存库） |
| POST | `/api/user/login` | 登录（Session 存状态） |
| GET | `/api/user/info` | 获取当前登录用户 |
| POST | `/api/user/logout` | 登出 |

### 错题模块

| 方法 | URL | 说明 |
|------|-----|------|
| POST | `/api/mistake` | 新增错题 |
| GET | `/api/mistake` | 查我的错题（支持 `?subject=数学` 筛选） |
| GET | `/api/mistake/{id}` | 查错题详情（越权防护） |
| PUT | `/api/mistake/{id}` | 更新错题 |
| DELETE | `/api/mistake/{id}` | 删除错题（越权防护） |
| POST | `/api/mistake/{id}/ai-analysis` | 🤖 AI 生成解析 |

## ✨ 项目亮点

1. **四层架构** — Entity / Mapper / Service / Controller 职责清晰，每层解耦
2. **MyBatis-Plus 泛型链路** — `BaseMapper<Entity>` → `IService<Entity>` → `ServiceImpl<Mapper, Entity>`，白嫖 20+ CRUD 方法
3. **MD5 密码加密** — `DigestUtils.md5DigestAsHex()` 单向加密，数据库不存明文
4. **Session 登录鉴权** — 登录成功存 `session.setAttribute("currentUser")`，Controller 统一拦截检查
5. **越权访问防护** — 每个错题接口比对 `userId`，防止 A 用户查看/删除 B 用户的错题
6. **阿里云百炼集成** — RestTemplate 调 DashScope API，Prompt 工程让 AI 返回结构化解析
7. **配置安全** — 敏感配置用环境变量 `${XXX}` + `.gitignore` 排除本地配置，防止密钥泄露
8. **统一返回格式** — `Result<T>` 泛型封装，`code/msg/data` 三字段，前后端对接无歧义
9. **全链路类型安全** — 主键用 `Long`（对应 BIGINT），时间用 `LocalDateTime`（线程安全），实体类字段全用包装类

## 📌 关于 AI 返回格式

百炼 DashScope API 的实际返回格式为：

```json
{
  "output": {
    "finish_reason": "stop",
    "text": "AI 生成的解析内容"
  }
}
```

解析时取 `output.text` 字段即可。
