# 智学通

基于 Java SpringBoot 的项目

[toc]

## 模板特点

### 主流框架 & 特性

- Spring Boot
- Spring Security
- Spring MVC
- MyBatis
- Spring Scheduler 定时任务

### 数据存储

- MySQL 数据库
- Redis 内存数据库
- Cache Spring 缓存
- Minio 对象存储

### 工具类

- Hutool 工具库
- Apache Commons Lang3 工具类
- Lombok 注解
- Validation 验证
- 科大讯飞 等等。。

### 业务特性

- JWT身份验证和授权
- 全局请求响应拦截器
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- 全局跨域处理
- 多环境配置


## 业务功能

- 管理员志愿管理
- 添加待办
- 参加志愿
- 个人中心
- 讯飞星火
- 图片理解，文本合规，图片合规等等

### 单元测试

- 基于apifox进行接口测试
- apifox地址：https://apifox.com/apidoc/shared-25a0116c-28cc-4210-867a-420ad2d8e1ff

### 架构设计

- 合理分层


### MySQL 数据库

1）修改 `application.yml` 的数据库配置为你自己的：

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/my_db
    username: root
    password: 123456
```

2）执行 `sql/orientation.sql` 中的数据库语句，自动创建库表


### Redis 缓存

1）修改 `application.yml` 的 Redis 配置为你自己的：

```yml
spring:
  redis:
    host: localhost
    port: 6379
    password: 123456
```

### minio 对象存储

1）修改 `application.yml` 的 minion 配置为你自己的：
```yml
endpoint: http://localhost:9000
bucketName: orientation
accessKey: 
secretKey: 
```

### 科大讯飞密钥
```yml
    # id
    secret-id: 
    # 密钥
    secret-key:
```

###  uni短信服务
```yml
    #短信公钥
    access-key-id: 
    #短信模板 ID
    template_id:
    #短信签名
    signature: 
    #有效时间 1000*60*3
    ttl: 
```

运行SmartStudyApplication即可访问，端口为3078
服务器地址: 114.132.67.226:3078