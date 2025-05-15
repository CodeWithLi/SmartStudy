# 智学通

## 简述
随着人工智能技术的发展及其在教育领域的深入应用，教育现代化迎来了新的机遇。我们设计并开发了一个集成GPT功能、待办管理与志愿服务模块的Web网站，旨在为学生提供智能、便捷、安全的服务体验。借助如科大讯飞提供的语音识别、自然语言处理等AI能力，平台不仅助力学生高效管理学习与生活事务，也为其参与社会公益活动提供便捷通道，推动教育信息化与智能化发展。

## 项目亮点
1. 安全性保障
- 登陆框架选择了Spring security,结合JWT技术，实现了用户认证与授权。
- 数据库权限控制采用了ABAC权限管理模型，细分权限，确保敏感数据仅限授权人员访问。
- 数据库id号采用雪花算法随机生成，防止id重复引起数据插入异常,并有效防止恶意爬虫视频。
2. 优化聊天对话速度
- 采取缓存技术:使用Redis缓存对话列表,减少数据库访问次数。
3. 图片存储问题
- 采取MinIO: 支持MinIO, MinMinIO是开源的分布式对象存储，可以降低运营成本，将图片文件到MinIO,降低网络延迟,提升用户体验。
4. 部署
- 支持传统Spring Boot部署
  a. 兼谷性好:许多企业服务器已经安装了Mysql，Redis等数据库，因此可以快速部署。不用重复安装
  b. 稳定性和可靠性高:传统的应用服务器经过多年的打磨和优化，具备高度的稳定性和可靠性，适合运行关键任务的企业级应用。

这些措施不仅提升了系统的安全性和稳定性，还优化了用户体验，提高了系统的性能和响应速度，为项目的成功运行
和用户满意度提供了坚实保障。

[toc]

## 设计
### 应用框架
![image](https://github.com/user-attachments/assets/e5619cb0-1d85-4453-af8f-471c68564705)![image](https://github.com/user-attachments/assets/e1845fdf-e327-4a3d-a3e8-3b621e50e578)
技术和开发工具的选型考虑以下方面:良好的生态，性能，流行性与易用性。
基于Java后端技术栈的应用，采用了Spring security、spring Boot、Spring MVc以及MyBatis-Plus等框架和工具。这些技术的结合提供了高效的开发环境和可靠的业务逻辑实现，同时使用MSQL和Redis等教据库技术，保障了数据的存
储和快速访问。
开发过程中，使用了IDEA和Navicat作为主要的开发工具，确保了开发过程的高效性和可视化管理数据库的便捷性。另外，通过使用Another Redis Desktop Manager管理Redis数据库，进一步提升了开发效率和数据管理的可靠性。
在应用中整合了MinIO相关API和SDK，为图片存储提供了可靠的解决方案。

总的来说，该应用采用了一系列专业的技术和工具，从后端到前端，从数据库到服务器，为用户提供了高质量、稳定性和安全性的应用体验。

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
