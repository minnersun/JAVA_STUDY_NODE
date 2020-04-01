## 第十四天笔记(jstl)

---

### jstl标签库

##### jsp引入jstl

> <%@taglib uri="" prefix=""%>

##### jstl标签的子库

> 核心标签库 core - c
>
> >  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
> >
> > URI = URL+URN(命名空间)

##### jstl标签库中常用标签

###### `<c:set>`

```jsp
<h1>c:set 设置/修改域属性 </h1>
<c:set scope="request" var="name" value="zs"></c:set>
${name}
<c:set scope="request" var="name" value="ls"></c:set>
${name}
<hr>

<h1>c:set 设置/修改域中Map属性 </h1>
 <%   
		Map<String,String> map = new HashMap<String,String>();             			pageContext.setAttribute("map", map);
  %>
     
<c:set target="${map }" property="addr" value="bj"></c:set${map.addr }
<c:set target="${map }" property="addr" value="sh"></c:set>${map.addr }
<hr>
     
<h1>c:set 设置/修改域中javabean属性 </h1>
<%
	Person p = new Person("ww",19,"gz");
	pageContext.setAttribute("p", p);
%>
<c:set target="${p }" property="age" value="20"></c:set>
```

###### `<c:remove>`

```jsp
<h1>c:remove 删除所有域或指定域中的属性 </h1>
<c:set var="namex" value="zl1" scope="page"></c:set>
<c:set var="namex" value="zl2" scope="request"></c:set>
<c:set var="namex" value="zl2" scope="session"></c:set>
<c:set var="namex" value="zl2" scope="application"></c:set>
<c:set var="agex" value="18" scope="page"></c:set>
${pageScope.namex }
${requestScope.namex }
${sessionScope.namex }
${applicationScope.namex }
${agex }
<c:remove var="namex" scope="page"/>
${pageScope.namex }
${requestScope.namex }
${sessionScope.namex }
${applicationScope.namex }
${agex }
<c:remove var="namex"/>
${pageScope.namex }
${requestScope.namex }
${sessionScope.namex }
${applicationScope.namex }
${agex }
```

###### `<c:catch>`

````jsp
<h1>c:catch 捕获异常</h1>
<c:catch var="e">
    <%
       String str = null;
       str.toUpperCase();
    %>
</c:catch></c:catch>${e}<c:if>
````

###### `<c:if>`

```jsp
<h1>c:if 实现判断</h1>
<%
       pageContext.setAttribute("num", 18);
%>
<c:if test="${num > 20}" scope="page" var="flag">yes~</c:if>
<c:if test="${num <= 20}">no~</c:if>
${flag}
```

`<c:choose><c:when><c:otherwise`

```jsp
<h1>c:choose 实现多重判断</h1>
<%
   pageContext.setAttribute("num", 18);
%>
<c:choose>
    <c:when test="${num<10 }">小于10</c:when>
    <c:when test="${num<100 }">大于10小于100</c:when>
    <c:when test="${num<1000 }">大于100小于1000</c:when>
    <c:otherwise>大于1000</c:otherwise>
</c:choose>
```

`<c:foreach>`

````jsp
<h1>c:foreach 实现循环遍历</h1>
<%
	List<String>list=newArrayList<String();
	list.add("aa");
	list.add("bb");
	list.add("cc");
	list.add("dd");
	list.add("ee");
	pageContext.setAttribute("list", list);
%>
<c:forEach items="${list }" var="x">
    ${x } 
</c:forEach>
<c:forEach begin="0" end="100" step="2" var="i">
    ${i }
</c:forEach>
<hr>

<h1>c:foreach案例：遍历10到100的偶数，如果数字所在的位置是3的倍数，显示成红色</h1><c:forEach begin="10" end="100" step="2" var="i" varStatus="stat">
<c:if test="${stat.count % 3 == 0 }">
    <font color="red">${i }</font></c:if>
    <c:if test="${stat.count % 3 != 0 }">
        <font color="blue">${i }</font>
    </c:if>
</c:forEach>
````

`<c:fortoken>`

> 根据给定分隔符切割指定字符串，将得到的字符串数组进行遍历

```jsp
<h1>c:fortoken </h1>
<c:forTokens items="www.tedu.cn" delims="." var="str">
    ${str} <br>
</c:forTokens>
```

