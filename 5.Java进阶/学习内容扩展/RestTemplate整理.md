## RestTemplate

> 主要参考：`https://blog.csdn.net/u012702547/article/details/77917939/`

------

### GET请求

#### getForEntity()

> `getForEntity(String url, Class<T> responseType, Object... uriVariables)`	-- 有其他重载
>
> > 第一个参数为我要调用的服务的地址
> >
> > 第二个参数String.class表示我希望返回的body类型是String
> >
> > 第三和参数是需要传递的参数

> 返回值是一个`ResponseEntity<T>`
>
> > `ResponseEntity<T>`是Spring对HTTP请求响应的封装
> >
> > > 包括如**响应码、contentType、contentLength、响应消息体**等

##### 案例：

###### 无参调用接口

```java
@RequestMapping("/gethello")
public String getHello() {
    ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class);
    String body = responseEntity.getBody();
    HttpStatus statusCode = responseEntity.getStatusCode();
    int statusCodeValue = responseEntity.getStatusCodeValue();
    HttpHeaders headers = responseEntity.getHeaders();
    StringBuffer result = new StringBuffer();
    result.append("responseEntity.getBody()：").append(body).append("<hr>")
            .append("responseEntity.getStatusCode()：").append(statusCode).append("<hr>")
            .append("responseEntity.getStatusCodeValue()：").append(statusCodeValue).append("<hr>")
            .append("responseEntity.getHeaders()：").append(headers).append("<hr>");
    return result.toString();
```

###### 有参调用接口

```java
@RequestMapping("/sayhello")
public String sayHello() {
    // 方式一
    // >> 可以用一个数字做占位符，最后是一个可变长度的参数，来一一替换前面的占位符
    ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/sayhello?name={1}", String.class, "张三");
    return responseEntity.getBody();
}

@RequestMapping("/sayhello2")
public String sayHello2() {
    // 方式二：
    // >> 前面使用name={name}这种形式，最后一个参数是一个map，map的key即为前边占位符的名字，map的value为参数
    Map<String, String> map = new HashMap<>();
    map.put("name", "李四");
    ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/sayhello?name={name}", String.class, map);
    return responseEntity.getBody();
}
```

###### 使用自定义的对象接收返回值

```java
@RequestMapping("/book1")
public Book book1() {
    ResponseEntity<Book> responseEntity = restTemplate.getForEntity("http://HELLO-SERVICE/getbook1", Book.class);
    return responseEntity.getBody();
}
```



#### getForObject()

> `getForObject(String url, Class<T> responseType, Object... uriVariables)`	-- 有其他重载
>
> > 函数实际上是对getForEntity函数的进一步封装
> >
> > > 如果你**只关注返回的消息体的内容**，对其他信息都不关注，此时**可以使用getForObject**

##### 案例：

###### 简单案例：

```java
@RequestMapping("/book2")
public Book book2() {
    Book book = restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
    return book;
}
```







### Post请求

#### postForEntity()

> `postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables)`	-- 有其他重载
>
> > 该方法和get请求中的getForEntity方法类似

> 方法的第一参数表示要调用的服务的地址
>
> 方法的第二个参数表示上传的参数
>
> 方法的第三个参数表示返回的消息体的数据类型

##### 案例

````java
@RequestMapping("/book3")
public Book book3() {
    Book book = new Book();
    book.setName("红楼梦");
    ResponseEntity<Book> responseEntity = restTemplate.postForEntity("http://HELLO-SERVICE/getbook2", book, Book.class);
    return responseEntity.getBody();
}
````

#### postForObject()

> 该方法和get请求中的getForEntity方法类似

#### postForLocation()

> postForLocation的参数和前面两种的**参数基本一致**，只不过该方法的返回值为Uri
>
> > 这个只需要服务提供者返回一个Uri即可，该Uri表示新资源的位置



### Put请求

> `put(String url, Object request, Object... uriVariables)	`	-- 有其他重载
>
> > put方法的参数和前面介绍的postForEntity方法的参数基本一致，只是put方法没有返回值而已

#### 案例

```java
@RequestMapping("/put")
public void put() {
    Book book = new Book();
    book.setName("红楼梦");
    restTemplate.put("http://HELLO-SERVICE/getbook3/{1}", book, 99);
}
```



### DELETE请求

> delete方法也有几个重载的方法，不过重载的参数和前面基本一致

#### 案例

```java
@RequestMapping("/delete")
public void delete() {
    restTemplate.delete("http://HELLO-SERVICE/getbook4/{1}", 100);
}
```