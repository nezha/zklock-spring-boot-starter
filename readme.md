## 基于zookeeper的分布式锁的starter



# 快速开始

> spring boot项目接入


1.添加lock starter组件依赖，目前还没上传到公共仓库，需要自己下源码build 
```
<dependency>
    <groupId>com.nezha.component</groupId>
    <artifactId>zklock-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>

```

2.application.properties配置zookeeper链接：spring.zklock.hosts=127.0.0.1:2181


3.在需要加分布式锁的方法上，添加注解@ZKlock，如：
```java
@Service
public class TestService {

    @ZKlock(waitTime = 1, name = "nezha", keys = {"#param"}, lockTimeoutStrategy = LockTimeoutStrategy.FAIL_FAST, customLockTimeoutStrategy = "lockFailure")
    public String getValue(String param) throws Exception {
        if ("sleep".equals(param)) {//线程休眠或者断点阻塞，达到一直占用锁的测试效果
            Thread.sleep(1000 * 3);
        }
        return "success";
    }

    private String lockFailure(String param) {
        return "===Failure===";
    }
}

```


# 使用参数说明

> 配置参数说明

```properties
spring.zklock.hosts  : redis链接地址
spring.zklock.retry : 重试次数
spring.zklock.connectTimeout : 连接超时时间30毫秒
spring.zklock.waitTime : 获取锁最长阻塞时间（默认：1，单位：秒）
spring.zklock.leaseTime: 已获取锁后自动释放时间（默认：1，单位：秒）
spring.zklock.sessionTimeout : 会话保持时间，默认300毫秒 
```


> @ZKlock注解参数说明
```
@ZKlock可以标注四个参数，作用分别如下

name：lock的name，对应zookeeper的路径。默认为：/lock/类名+方法名

lockType：锁的类型，目前支持（可重入锁，读写锁）。默认为：可重入锁

waitTime：获取锁最长等待时间。默认为：1s。同时也可通过spring.zklock.waitTime统一配置

leaseTime：获得锁后，自动释放锁的时间。默认为：1s。同时也可通过spring.zklock.leaseTime统一配置

lockTimeoutStrategy: 加锁超时的处理策略，可配置为不做处理、快速失败、阻塞等待的处理策略，默认策略为不做处理

customLockTimeoutStrategy: 自定义加锁超时的处理策略，需指定自定义处理的方法的方法名，并保持入参一致。

releaseTimeoutStrategy: 释放锁时，持有的锁已超时的处理策略，可配置为不做处理、快速失败的处理策略，默认策略为不做处理

customReleaseTimeoutStrategy: 自定义释放锁时，需指定自定义处理的方法的方法名，并保持入参一致。
```

## 设计思路

> 主要还是学习为主，学习的是klock的设计思路

编写Starter非常简单，与编写一个普通的Spring Boot应用没有太大区别，总结如下：

```text
1.新建Maven项目，在项目的POM文件中定义使用的依赖；
2.新建配置类，写好配置项和默认的配置值，指明配置项前缀；
3.新建自动装配类，使用@Configuration和@Bean来进行自动装配；
4.新建spring.factories文件，指定Starter的自动装配类；
```


## 参考文献

从零开始开发一个Spring Boot Starter： https://www.jianshu.com/p/bbf439c8a203


## 感谢：

https://github.com/kekingcn/spring-boot-klock-starter
代码是基于Klock的基础上改为zookeeper分布式锁的