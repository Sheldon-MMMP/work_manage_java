## 返回格式

### 错误返回格式：
```
{
    code: xxxxx,
    message: "xxxxxx",
    data: null
}
```
- code 错误码,说明如下：
    - 五位数字 12345，第1位是错误等级，第 2 位是错误来源，345 是编号，人的大脑不会主动地分辨每位数字的不同含义。
        - 第1位：错误等级
            - 1：业务逻辑错误
            - 2：参数错误
            - 3：系统错误
        - 第2位：错误来源
            - 1：用户操作
            - 2：系统操作
            - 3：第三方服务
        - 第3位到第5位：错误编号
            - 001-999：业务逻辑错误
            - 100-199：参数错误
            - 200-299：系统错误
- message 错误信息，对错误的详细描述：错误将错误信息用一个文件单独维护，不同的错误码对应一个错误信息
- data 错误数据，可选字段，用于返回与错误相关的数据

### 成功返回格式：
```
{
    code: 0,
    message: "success",
    data: {} or [] or null
}
```
- code 成功码,固定为 0
- message 成功信息,固定为 "success"
- data 成功数据,根据业务逻辑返回不同的数据格式

## 接口风格
1. 接口都采用RESTful API的形式
    - 接口类似于：/{version}/{resources}/{resource_id}

## 代码风格规范
1. 基础规范
```
    统一使用 UTF-8 编码，缩进为 4 个空格。
    每个类都应有清晰的职责（单一职责原则）。
```

2. 命名规范（参考阿里巴巴规范）
类型 	命名规则示例 	说明
类名 	UserServiceImpl 	使用大驼峰，常用后缀如 DTO、VO、BO、DO、Mapper
方法名 	getUserById() 	小驼峰，动宾结构
常量 	MAX_RETRY_TIMES 	全大写+下划线分隔
变量名 	userId、userList 	小驼峰命名
包名 	com.example.project.module 	全小写，不使用下划线

## 包结构
```
com.example.project
├── controller       // 接口层
├── service          // 业务逻辑层
│   └── impl
├── mapper           // MyBatis 映射接口
├── domain           // 实体类（DO/PO）
├── dto              // 数据传输对象（DTO）
├── vo               // 前端返回对象（VO）
├── config           // 配置类
├── exception        // 自定义异常类
├── common           // 通用类（常量、工具类、枚举）
└── Application.java // 启动类
```

## 日志规范
统一使用 SLF4J + Logback（通过 Lombok 的 @Slf4j 注解简化调用）
    所有 catch 块必须打印日志：
```
catch (Exception e) {
    log.error("操作失败，userId: {}", userId, e);
}
```
    - 不允许打印敏感数据（如密码、token）；
    - 禁止使用 System.out.println。
