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
![image](https://github.com/user-attachments/assets/e5619cb0-1d85-4453-af8f-471c68564705)
技术和开发工具的选型考虑以下方面:良好的生态，性能，流行性与易用性。

基于Java后端技术栈的应用，采用了Spring security、spring Boot、Spring MVc以及MyBatis-Plus等框架和工具。这些技术的结合提供了高效的开发环境和可靠的业务逻辑实现，同时使用MSQL和Redis等教据库技术，保障了数据的存储和快速访问。

开发过程中，使用了IDEA和Navicat作为主要的开发工具，确保了开发过程的高效性和可视化管理数据库的便捷性。另外，通过使用Another Redis Desktop Manager管理Redis数据库，进一步提升了开发效率和数据管理的可靠性。

在应用中整合了MinIO相关API和SDK，为图片存储提供了可靠的解决方案。
在应用中也整合了科大讯飞相关API和SDK，包括星火大模型、文本合规、图片合规、身份证识别等功能，为应用的GPT功能和内容审核提供了可靠的解决方案。

总的来说，该应用采用了一系列专业的技术和工具，从后端到前端，从数据库到服务器，为用户提供了高质量、稳定性和安全性的应用体验。

### 业务设计
![image](https://github.com/user-attachments/assets/47fbeaa9-cc2c-40dd-b66e-10276bd06c72)
上述为本程序的用例图,根据上述用例图,可以看出该应用具有以下功能:
1. 用户注册
   - 未注册用户可通过输入身份证、用户名和密码完成账户注册。
2. 用户登录
   - 注册后用户可使用身份证和密码进行登录，以便使用全部功能。
3. 添加代办
   - 用户可以添加需要待完成的任务，以便于提醒自己。
4. 志愿活动
   - 用户可以根据自己的需求来判断是否参加某个志愿活动。
5. GPT
   - 用户打开网页即可使用GPT，并进行对话。

### sql设计
![image](https://github.com/user-attachments/assets/1a305f08-c6c4-4d94-b4c2-4caa958c0898)
这是本程序的sql设计图，描述了系统中的主要实体及其之间的关系。

### API设计
在线API文档：https://apifox.com/apidoc/shared-25a0116c-28cc-4210-867a-420ad2d8e1ff

## Getting Started
确保本机已经安装了JDK17,MySQL 8以上版本, Redis 7.0.8,Minio 8.4.0
  1. 初始化数据库: 进入根据sql脚本文件导入数据库
  
  2. 配置application.yml文件
   
  1）修改 `application.yml` 的数据库配置为你自己的：
    ```yml
    spring:
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3306/DB
        username: 
        password: 
    ```
    
   2）修改 `application.yml` 的 Redis 配置为你自己的：
    ```yml
      spring:
        redis:
        host: localhost
        port: 6379
        password: 
    ```
    
   3）修改 `application.yml` 的 minion 配置为你自己的：
    ```yml
      endpoint: http://localhost:9000
      bucketName: 
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
  3. 启动程序：启动程序,在项目根目录下执行 mvn spring-boot:run 命令,程序会自动启动

