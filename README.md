# open-smartcloud
通用微服务平台：采用SpringCloud系列和alibaba产品集成;
                包括基本的服务支撑模块（注册中心，API网关，配置中心，监控中心，日志中心，基础权限模块，登录认证中心, CI/CD等等）;
                主要用于学习用途;


## 1. 项目介绍
项目采用统一的包目录结构，主目录下分为config,core,modular
* **统一登录认证功能**
  * 支持oauth2的四种模式登录
  * 支持用户名、密码加图形验证码登录
  * 支持手机号加密码登录
  * 支持openId登录
  * 支持第三方系统(授权模式)单点登录
* **分布式系统基础支撑**
  - 服务注册发现、路由与负载均衡
  - 服务降级与熔断
  - 服务限流(url/方法级别)
  - 统一配置中心
  - 统一日志中心
  - 统一搜索中心
  - 统一分布式缓存操作类、cacheManager配置扩展
  - 分布式锁
  - 分布式任务调度器
  - 支持CI/CD持续集成(包括前端和后端)
  - 分布式Id生成器
  - 分布式事务(强一致性/最终一致性)
* **系统监控功能**
  - 服务调用链监控
  - 应用拓扑图
  - 慢查询SQL监控
  - 应用吞吐量监控(qps、rt)
  - 服务降级、熔断监控
  - 服务限流监控
  - 微服务服务监控
  - 服务器监控
  - redis监控
  - mysql监控
  - elasticSearch监控
  - nacos监控
  - prometheus监控
* **业务基础功能支撑**
  * 多租户(应用隔离)
  * 高性能方法级幂等性支持
  * RBAC权限管理，实现细粒度控制(方法、url级别)
  * 快速实现导入、导出功能
  * 数据库访问层自动实现crud操作
  * 代码生成器
  * 基于Hutool的各种便利开发工具
  * 网关聚合所有服务的Swagger接口文档
  * 统一跨域处理
  * 统一异常处理

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
│  │  ├─jason-demo-account    -- 帐目系统
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
│  │  ├─Sentinel-Dashboard-Nacos-1.6.2-NACOS -- 重写后的Sentinel-Dashboard，可以与nacos自动同步拉取和推送配置
│  │  ├─zuul-gateway -- zuul网关[9000]
│  ├─jason-monitor -- 监控
│  │  ├─admin-server -- 应用监控
```

&nbsp;

## 3. 截图（点击可大图预览）

<table>
    <tr>
        <td colspan="2">登录中心和工作台</td>
    </tr>
    <tr>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/doc/syspic/login.png"/></td>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/doc/syspic/homepage.png"/></td>
    </tr>
</table>