# open-smartcloud
通用微服务平台：采用SpringCloud系列和alibaba产品集成;
                包括基本的服务支撑模块（注册中心，API网关，配置中心，监控中心，日志中心，全文搜索，基础权限模块，登录认证中心, CI/CD等等）;
                主要用于学习用途;


## 1. 项目介绍
项目总体功能划分为
  - 核心组件包jason-kerner：包括常用的数据库封装，redis封装，oauth2客户端封装，swagger2封装，统一异常，输入，输入和web配置等
  - 微服务支撑模块jason-microservice-support：微服务的网关，流控，注册，监控等
  - 配置模块jason-config：提取公共配置单独一个模块，也可以把这些文件维护到nacos配置中心
  - 业务系统模块jason-business：目前有网站后台，统一用户服务和统一登录认证中心
  - 业务公共组件模块jason-biz-support：目前有日志中心(Kafka+Mysql+Elasticsearch)和全文搜索中心(Elasticsearch)
  - 分布式事务模块jason-message:基于消息预处理方式模拟一个订单，付款的解决方案
  
  所有业务系统采用统一的包目录结构，根目录下分为config,core,modular三个主包，所有的AOP和相关的配置都是基于这个结构来现；

具体功能详细如下：
* **统一登录认证功能**
  - 支持oauth2的四种模式登录；支持用户名、密码加图形验证码登录； 支持手机号加密码登录；支持openId登录；支持第三方系统(授权模式)单点登录

* **分布式支撑功能**
  - 基于nacos的统一注册，统一配置的管理；基于sentinel进行流控管理，本项目中使用的组件都是基于源码做了一些Bug的修正处理的，版本为1.6.2；

* **日志功能**
  - 日志功能主要分为两部分：
  - 自定义RequstNo和SpanId进行服务调用间链路的全程跟踪；实时记录链路日志到Kafka中，消费都存入mysql；提供日志记录工具随时可以调用定制的日志；
  - 进行log4j埋点的方式记录系统日志到文件，再通过ElasticSearch+logstash+kibana实时抽取和分析；

* **全文搜索功能**
  - 采用最新的RestHighLevelClient Http方式直接操作Elasticsearch,本项目中所使用elk和java客户端相关版本全为6.8.1的破解版；
  
* **业务基础功能支撑**
  - 高性能方法级幂等性支持；RBAC权限管理，实现细粒度控制(方法、url级别)；数据库访问层自动实现crud操作，支持多库切换；网关聚合所有服务的Swagger接口文档；
   统一跨域处理；统一异常处理；统一的请求RequestData和输出ResponseData处理；

* **持续集成**
  -  自定义支持window,linux多平台，多言语（nodejs,java,dotnet）的通过脚本，结合GitLab源码管理工具,Jenkins构建工具和Harbor私有镜像库;提交代码自动部署集成；
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
│  │  ├─remark              -- 综合相关说明
│  │  ├─sql                 -- 初始化SQL脚本
│  │  ├─syspic              -- 系统截图
│  ├─jason-biz-support           -- 业务公共模块
│  │  ├─modular-log             -- 日志模块
│  │  │  ├─biz-support-log     -- 日志业务系统（kafka+mysql）【端口：7021】
│  │  │  ├─biz-support-log-api -- 日志API和实体定义
│  │  ├─modular-search          -- 全文搜索引擎
│  │  │  ├─search-api          -- 公用ElasticSearch搜索API
│  │  │  ├─search-client       -- 搜索客户端（feign消息者)（需要使用的第三方系统直接引入这个模块）
│  │  │  ├─search-server       -- ElasticSearch搜索服务(feign生产提供者）【端口：7031】
│  ├─jason-business -- 业务系统模块一级工程
│  │  ├─auth-center -- 登录认证中心【端口：7000】 (spring security+OAuth2, 支持oauth2的四种认证模式，已重写authorization_code登录页和授权页)
│  │  ├─user-center -- 用户中心【端口：7011】
│  │  ├─back-web    -- 后台管理网站【端口：8888】
│  ├─jason-config -- 配置中心（通用配置，可以拷贝到nacos配置中心使用）
│  │─jason-demo   -- 测试使用
│  ├─jason-job -- 分布式任务调度
│  │  ├─job-admin   -- 任务管理器
│  │  ├─job-core    -- 任务调度核心代码
│  │  ├─job-demo    -- 任务执行者executor样例
│  ├─jason-kernel    --  内核封装
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
│  │  ├─jason-message-api     -- 消息操作API接口
│  │  ├─jason-message-check   -- 消息监听(feign消费者)
│  │  ├─jason-message-service -- 消息操作服务(feign生产提供者)
│  ├─jason-microservice-eureka -- 微服务相关支持，这里是采用Eureka实现（已用zuul取代）
│  │  ├─api-gateway   -- 网关
│  │  ├─eureka-client -- eureka客户端API
│  │  ├─eureka-server -- eureka注册中心
│  ├─jason-microservice-support        -- 微服务相关支持，这里是采用nacos+zull实现
│  │  ├─jason-register                -- nacos注册中心(把jar上传linux中部署)
│  │  ├─modular-monitor               -- 监控
│  │  │  ├─sm-admin-server           -- spring cloud admin监控中心【端口：9011】
│  │  ├─sentinel-parent               -- Sentinel源码：基于开源源码重写后的Sentinel-Dashboard，可以与nacos自动同步拉取和推送配置数据
│  │  ├─spring-cloud-alibaba-sentinel -- Sentinel源码：1.6.2版本，基于开源源码修复feign继承接口Api的Bug
│  │  ├─zuul-gateway -- zuul网关【端口：9000】

```

&nbsp;

## 3. 截图（点击可大图预览）

<table>
    <tr>
        <td colspan="2">登录中心和工作台</td>
    </tr>
    <tr>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/login.png"/ alt="登录"></td>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/homepage.png"/ alt="工作台"></td>
    </tr>
    <tr>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/oauth2_login.png"/ alt="第三方系统纺一登录"></td>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/oauth2_approve.png"/ alt="第三方系统纺一授权"></td>
    </tr>
    <tr>
        <td colspan="2">系统服务</td>
    </tr>
    <tr>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/api-gateway1.png"/ alt="网关服务"></td>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/auth-server1.png"/ alt="登录认证服务"></td>
    </tr>
    <tr>
        <td colspan="2">日志管理(链路日志kafka和系统日志log4j+elasticsearch+logstash+kibana)</td>
    </tr>
    <tr>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/kafkatool.png"/ alt="链路日志"></td>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/kibana.png"/ alt="kibana"></td>
    </tr>
    <tr>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/es-head1.png"/ alt="es管理1"></td>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/es-head2.png"/ alt="es管理2"></td>
    </tr>
    <tr>
        <td colspan="2">监控管理</td>
    </tr>
    <tr>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/mo-admin1.png"/ alt="admin服务监控中心"></td>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/nacos.png"/ alt="注册中心和配置中心"></td>
    </tr>
    <tr>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/sentinel-dashboard1.png"/ alt="流控中心1"></td>
        <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/sentinel-dashboard2.png"/ alt="流控中心2"></td>
    </tr>
        <tr>
            <td colspan="2">持续集成和镜像库</td>
        </tr>
        <tr>
            <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/jenkins1.png"/ alt="jenkins构建中心"></td>
        </tr>
        <tr>
            <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/harbor1.png"/ alt="镜像库1"></td>
            <td><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/harbor2.png"/ alt="镜像库2"></td>
        </tr>
    <tr>
        <td colspan="2">任务中心</td>
    </tr>
    <tr>
        <td colspan="2"><img src="https://github.com/jiangjie888/open-smartcloud/blob/master/doc/syspic/job.png"/ alt="xxxjob任务中心"></td>
    </tr>
   
</table>