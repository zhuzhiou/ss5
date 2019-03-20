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
    server_name authorizationserver;

    location / {
        proxy_pass http://127.0.0.1:8081/;
        proxy_set_header Host $host;
    }
}

server {
    listen 80;
    server_name resourceserver;

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
127.0.0.1	authorizationserver
127.0.0.1	resourceserver
127.0.0.1	oauth2client
127.0.0.1	oauth2login
```

## oauth2-authorization-server

ss5的 `Authorization Server` 特性还在plan中， 这里使用`Spring Security OAuth2`实现。

更多细节请查阅：[https://github.com/spring-projects/spring-security-oauth2-boot](https://github.com/spring-projects/spring-security-oauth2-boot)

## oauth2-resource-server

## oauth2-client