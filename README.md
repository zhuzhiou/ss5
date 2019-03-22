# ss5

`spring security 5` 项目已支持`oauth2`部份特性，（不是指`spring security oauth2`），特此创建 `git` 学习。

- spring security 5 开发参考手册

    [https://docs.spring.io/spring-security/site/docs/5.1.4.RELEASE/reference/htmlsingle/](https://docs.spring.io/spring-security/site/docs/5.1.4.RELEASE/reference/htmlsingle/)

- spring security 5 `oauth2` 特性矩阵图

    [https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix)

## 环境配置

Cookie有两个很重要的属性:Domain和Path，用来指示此Cookie的作用域。如果工程使用了Cookie并且使用同一个主机名，即使端口不一样，也会产生不可预计的错误。

在本机测试的时候，可以使用 `nginx` 代理本机应用，避免上面所述的潜在问题。

**Nginx配置**

```
server {
    listen 80;
    server_name oauth2provider;

    location / {
        proxy_pass http://127.0.0.1:8081/;
        proxy_set_header Host $host;
    }
}

server {
    listen 80;
    server_name oauth2resource;

    location / {
        proxy_pass http://127.0.0.1:8082/;
        proxy_set_header Host $host;
    }

}

server {
    listen 80;
    server_name oauth2client;

    location / {
        proxy_pass http://127.0.0.1:8083/;
        proxy_set_header Host $host;
    }
}

server {
    listen 80;
    server_name oauth2login;

    location / {
        proxy_pass http://127.0.0.1:8084/;
        proxy_set_header Host $host;
    }

}
```

**DNS配置**

```
127.0.0.1	oauth2provider
127.0.0.1	oauth2resource
127.0.0.1	oauth2client
127.0.0.1	oauth2login
```

## oauth2-provider

> ss5的 `Authorization Server` 特性还在开发中， 这里使用`Spring Security OAuth2`实现

**最小化配置**

1.  打开pom.xml文件，加入`spring-security-oauth2-autoconfigure`

```xml
<dependency>
    <groupId>org.springframework.security.oauth.boot</groupId>
    <artifactId>spring-security-oauth2-autoconfigure</artifactId>
    <version>2.1.3.RELEASE</version>
</dependency>
```

2.  打开`main`函数所在文件，加上`@EnableAuthorizationServer`

```java
@EnableAuthorizationServer
@SpringBootApplication
public class SimpleAuthorizationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleAuthorizationServerApplication, args);
    }
}
```

3.  编辑`application.yaml`文件，添加`ClientDetails`详细

```yaml
security:
  oauth2:
    client:
      client-id: helloworld
      client-secret: helloworld
```

更多细节请查阅：[https://github.com/spring-projects/spring-security-oauth2-boot](https://github.com/spring-projects/spring-security-oauth2-boot)

## oauth2-resource

**最小化配置**

1. 打开`pom.xml`文件，添加`spring-boot-starter-oauth2-resource-server`

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

2.  重载 `Boot Auto Configuration`的默认配置，添加`oauth2ResourceServer` 方法
```
@EnableWebSecurity
public class MyCustomSecurityConfiguration extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) {
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .oauth2ResourceServer()
                .jwt();
    }
}
```

3. 打开`application.yaml`文件，设置`jwk-set-uri`

```
security:
  oauth2:
    resourceserver:
      jwt:
        jwk-set-uri: https://idp.example.com/.well-known/jwks.json
```

## oauth2-client

## oauth2-login

**最小化配置**

1. 打开`pom.xml`文件，添加`spring-boot-starter-oauth2-client`

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

2. 重载 `Boot Auto Configuration`的默认配置，添加`oauth2Login` 方法

```

```

3. 打开application.yaml文件，配置`ClientRegistration`

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: d74141bd4e9396138ccc
            client-secret: 3907354b4e35be34165bc9880441ab1724e589a6
```