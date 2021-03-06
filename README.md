# Java秒杀系统方案设计

### 前言

秒杀系统相较于普通系统有很大的不同。在开始秒杀的瞬间，秒杀系统会面临两大挑战：(1)系统的并发量会非常的大；(2)并发量大的同时，网络的流量也会瞬间变大。

如果不针对秒杀功能进行优化，那么服务器的 load 瞬间会超出机器的承载能力，导致应用瘫痪甚至宕机。另外，秒杀系统在处理业务时要解决超卖、恶意刷单等问题。

### 开发环境

| JDK  | MySQL  | Spring Boot   | Redis | Maven | RabbitMQ |
| ---- | ------ | ------------- | ----- | ----- | -------- |
| 1.8.0_152| 8.0.13 | 2.1.4.RELEASE | 5.0.4 | 3.6.0 | 3.7.9    |

### 数据库设计

| 表名          | 功能                     | 关键列                                                       |
| ------------- | ------------------------ | ------------------------------------------------------------ |
| miaosha_user  | 用户信息表，存放用户信息 | id                                                           |
| goods         | 商品表                   | id(使用自增 id，但一般公司会用 snowflask 代替自增 id)        |
| miaosha_goods | 秒杀商品表               | id，goods_id                                                 |
| order_info    | 订单表                   | id，user_id，goods_id                                        |
| miaosha_order | 秒杀订单表               | id，user_id，goods_id，order_id(user_id 和 goods_id 做唯一索引保证一个用户只能秒杀到一个) |

### 安全校验

1. 明文密码前后端两次 MD5 加密

   + 用户端：password = MD5(明文 + 固定 salt)，防止明文密码在网络中传输。
   + 服务端：password = MD5(用户输入 + 随机 salt)，防止数据库被盗破解密码。

2. JSR303 + 全局异常处理器

   使用 JSR303 在入库前检测用户输入是否安全，检验返回的错误码以异常的方式抛出，由全局异常处理器收集并返回。

### 页面缓存技术

为解决前言中挑战(2)的流量问题，可以有以下解决方案：

1. 针对前后端不分离的系统：页面缓存 + URL缓存 + 对象缓存，其中对象缓存粒度最细，比较少用，毕竟 Redis 本身也是一种开销。
2. 页面静态化，前后端分离。
3. 静态资源优化，压缩，组合(减少连接数)，图床……(Tengine 自带优化，或者前端打包工具)。
4. CDN 优化(距离镜像，就近访问)。

## 秒杀系统优化

### 瓶颈

通过结合 JMeter 压测工具以及 Linux 的 top 指令，可以发现高并发下系统的主要瓶颈在于对数据库的访问，所以秒杀系统的主要优化思路在于减少系统对数据库的访问。

### 减少数据库访问思路

1. 系统初始化时，把商品库存数量加载到 Redis。
2. 收到请求后，Redis 预减库存，库存不足直接返回，否则进入下一步。
3. 请求入队，立即返回排队中。
4. 请求出队，生成订单，减少库存。
5. 客户端轮询，是否秒杀成功。

### 接口优化思路

1. Redis 预减库存减少数据库访问，详见减少数据库访问思路。
2. 内存标记减少对 Redis 的访问，即使用一个 HashMap 在内存中记录秒杀商品对应的库存，若为0则直接返回，无需再访问 Redis。
3. Spring Boot 集成 RabbitMQ 实现消息队列。请求先入队缓冲，异步下单，增强用户体验。详见减少数据库访问思路。

### 超卖问题解决思路

1. 数据库加唯一索引，防止用户重复购买，详见数据库设计。

2. SQL 语句添加库存数量判断，防止库存变为负数。

### 接口安全优化

1. 秒杀接口地址隐藏

   思路：秒杀开始之前，先去请求接口获取秒杀地址。(类似 token)

   + 接口改造，动态化接口，使其带上 Path Variable 参数。
   + 添加生成地址的接口。
   + 秒杀收到请求，先验证 Path Variable。

2. 使用数学公式验证码

   思路：点击秒杀之前，先输入验证码，分散用户请求。

   + 添加生成验证码的接口。
   + 获取秒杀路径的时候，验证验证码。
   + 使用 ScriptEngine 辅助。

3. 接口限流防刷

   思路：对接口做限流，使用拦截器控制单个用户的访问速率，减少对业务的入侵。

### 服务器优化

服务器优化详见博文 [秒杀系统服务器优化思路
](https://pomo16.github.io/2019/06/01/%E7%A7%92%E6%9D%80%E7%B3%BB%E7%BB%9F%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%BC%98%E5%8C%96%E6%80%9D%E8%B7%AF/)。

1. nginx 水平扩展，负载均衡。
2. Tomcat 优化。
3. nginx 优化。
4. LVS 四层负载均衡。
5. LVS + keepalived 高可用。