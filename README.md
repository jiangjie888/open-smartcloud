# open-smartcloud
通用微服务平台：采用SpringCloud系列和alibaba产品集成;
                包括基本的服务支撑模块（注册中心，API网关，配置中心，监控中心，日志中心，基础权限模块，登录认证中心, CI/CD等等）;
                主要用于学习用途;


## 1. 项目介绍
> 重构于开源项目OCP&cp：https://gitee.com/owenwangwen/open-capacity-platform
               Microservices-platform：https://gitee.com/zlt2000/microservices-platform
               Roses https://www.stylefeng.cn/

&nbsp;

## 2. 模块说明

```lua
open-smartcloud -- 父项目，公共依赖
│  ├─doc -- 项目文档:系统说明文件夹
│  │  ├─CI&CD -- 日志模块
│  │  │  ├─Dockerfile     -- Dockerfile镜像文件（包括java, nodejs, dotnet core）
│  │  │  ├─Images         -- 自己制作基础镜像，比如java程序运行的基本依赖环境
│  │  │  ├─Tool           -- 持续集成相关的工具
│  │  │  ├─自动发布脚本    -- 持续集成自动发布相关的公共脚本（包括window bat,linux shell）
│  │  │  ├─自动部署脚本    -- 持续集成自动部署相关的公共脚本（包括window bat,linux shell）
│  │  ├─remark -- 综合相关说明
│  │  ├─sql    -- 初始化SQL脚本
│  │  ├─syspic -- 系统截图
│  ├─jason-biz-support -- 业务公共模块
│  │  ├─modular-log -- 日志模块
│  │  │  ├─biz-support-log     -- 日志业务系统（kafka+mysql）
│  │  │  ├─biz-support-log-api -- 日志API和实体定义
│  ├─jason-business -- 业务系统模块一级工程
│  │  ├─auth-center -- 登录认证中心[7000] (spring security+OAuth2, 支持oauth2的四种认证模式，已重写authorization_code登录页和授权页)
│  │  ├─user-center -- 用户中心[7011]
│  │  ├─back-web    -- 后台管理网站[8888]
│  ├─jason-config -- 配置中心（通用配置，可以拷贝到nacos配置中心使用）
│  │─jason-demo   -- 测试使用
│  ├─jason-job -- 分布式任务调度
│  │  ├─job-admin -- 任务管理器
│  │  ├─job-core -- 任务调度核心代码
│  │  ├─job-demo -- 任务执行者executor样例
│  ├─jason-kernel --  内核封装
│  │  ├─kernel-actuator -- 封装spring security client端不需要认证的actuator
│  │  ├─kernel-auth     -- 封装spring security client端的通用操作逻辑
│  │  ├─kernel-core     -- 封装系统支持所需要的通用内核
│  │  ├─kernel-db       -- 封装数据库通用操作逻辑，包括支持多个数据源操作和对mybatisplus mapper配置yml读取注入
│  │  ├─kernel-logger   -- 封装log通用操作逻辑,在需要的系统中直接引入这个包就可以自动拦截各种日志传播给kafka
│  │  ├─kernel-redis    -- 封装Redis的通用操作逻辑
│  │  ├─kernel-ribbon   -- 封装Ribbon和Feign的通用操作逻辑,对微服务间所有请求header进行传递
│  │  ├─kernel-sentinel -- 封装Sentinel的通用操作逻辑
│  │  ├─kernel-swagger  -- 封装Swagger通用操作逻辑
│  ├─jason-message --  分布式事务（通过一个消息中间进行消息预处理的方式）
│  │  ├─jason-demo-account    -- 封装帐目
│  │  ├─jason-demo-api        -- 模块相关API
│  │  ├─jason-demo-order      -- 订单系统
│  │  ├─jason-message-api     -- 封装数据库通用操作逻辑，包括支持多个数据源操作和对mybatisplus mapper配置yml读取注入
│  │  ├─jason-message-check   -- 消息消费
│  │  ├─jason-message-service -- 消息服务
│  ├─jason-microservice-eureka -- 微服务相关支持，这里是采用Eureka实现
│  │  ├─api-gateway   -- 网关
│  │  ├─eureka-client -- eureka客户端API
│  │  ├─eureka-server -- eureka注册中心
│  ├─jason-microservice-support -- 微服务相关支持，这里是采用nacos+zull实现
│  │  ├─jason-register                       -- nacos注册中心(把jar上传linux中部署)
│  │  ├─Sentinel-Dashboard-Nacos-1.6.2-NACOS -- 重写后的Sentinel-Dashboard，可以与nacos自动同步拉去和推送配置
│  │  ├─zuul-gateway -- zuul网关[9000]
│  ├─jason-monitor -- 监控
│  │  ├─admin-server -- 应用监控
```

&nbsp;

## 3. 截图（点击可大图预览）