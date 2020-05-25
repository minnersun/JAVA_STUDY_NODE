## PageHelper笔记

-----

### PageHelper的中文API网址

> `https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md`

##### PageInfo用法

```java
//获取第1页，10条内容，默认查询总数count
PageHelper.startPage(1, 10);
List<User> list = userMapper.selectAll();
//用PageInfo对结果进行包装
PageInfo page = new PageInfo(list);
//测试PageInfo全部属性
//PageInfo包含了非常全面的分页属性
assertEquals(1, page.getPageNum());
assertEquals(10, page.getPageSize());
assertEquals(1, page.getStartRow());
assertEquals(10, page.getEndRow());
assertEquals(183, page.getTotal());
assertEquals(19, page.getPages());
assertEquals(1, page.getFirstPage());
assertEquals(8, page.getLastPage());
assertEquals(true, page.isFirstPage());
assertEquals(false, page.isLastPage());
assertEquals(false, page.isHasPreviousPage());
assertEquals(true, page.isHasNextPage());
```

###### 公司案例

> list：为查询的所有数据
>
> PageResult：封装需要向前端返回的数据

```java
    @Override
    public PageResult<TwoDoorAlarmVO> getTwoDoorDetail(int page, int rows,String precinctId,String startTime, String endTime) {
        PageHelper.startPage(page, rows);
        List<TwoDoorAlarmVO> list = alarmCountAnalysisMapper.getTwoDoorDetail(precinctId, startTime, endTime);
        PageInfo<TwoDoorAlarmVO> pageInfo = new PageInfo<>(list);
        return new PageResult<>((int) pageInfo.getTotal(), pageInfo.getList());
    }
```