## 使用递归的方式自动生成多级树

-----

### 1.测试代码

##### Menu.java

```java
package com.wiscom;

import java.util.List;

public class Menu {
    private String id;
    private String parentId;
    private String text;
    private String url;
    private String yxbz;
    private List<Menu> children;
    /*省略get\set*/
}

```

##### BaseApplicationTests.java

```java
package com.wiscom;

import io.swagger.util.Json;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BaseApplicationTests {

    @Test
    void contextLoads() {
        List<Menu> menuList = new ArrayList<Menu>();
        /*插入一些数据*/
        menuList.add(new Menu("GN001D000", "0", "系统管理", "/admin", "Y"));
        menuList.add(new Menu("GN001D100", "GN001D000", "权限管理", "/admin", "Y"));
        menuList.add(new Menu("GN001D110", "GN001D100", "密码修改", "/admin", "Y"));
        menuList.add(new Menu("GN001D120", "GN001D100", "新加用户", "/admin", "Y"));
        menuList.add(new Menu("GN001D200", "GN001D000", "系统监控", "/admin", "Y"));
        menuList.add(new Menu("GN001D210", "GN001D200", "在线用户", "/admin", "Y"));
        menuList.add(new Menu("GN002D000", "0", "订阅区", "/admin", "Y"));
        menuList.add(new Menu("GN003D000", "0", "未知领域", "/admin", "Y"));
        menuList.add(new Menu("GN001D300", "GN001D210", "aaaaa", "/admin", "Y"));
        menuList.add(new Menu("GN001D400", "GN001D300", "bbbb", "/admin", "Y"));

        /*让我们创建树*/
        MenuTree menuTree = new MenuTree(menuList);
        menuList = menuTree.builTree();
        /*转为json看看效果*/

        String jsonOutput = Json.pretty(menuList);
        System.out.println(jsonOutput);
    }
}
class MenuTree {
    private List<Menu> menuList = new ArrayList<Menu>();

    public MenuTree(List<Menu> menuList) {
        this.menuList = menuList;
    }

    //建立树形结构
    public List<Menu> builTree() {
        //
        List<Menu> treeMenus = new ArrayList<Menu>();
        // 遍历根节点，getRootNode()：获取根节点的数据
        for (Menu menuNode : getRootNode()) {
            menuNode = buildChilTree(menuNode);
            treeMenus.add(menuNode);
        }
        return treeMenus;
    }

    //递归，建立子树形结构
    // pNode: 父级树的信息
    private Menu buildChilTree(Menu pNode) {
        // 用于存储父级树下子树信息
        List<Menu> chilMenus = new ArrayList<Menu>();

        // 遍历所有传入的数据
        for (Menu menuNode : menuList) {
            // 判断数据的父级节点是否为当前的节点
            if (menuNode.getParentId().equals(pNode.getId())) {
                // 将当前遍历的数据添加到当前pNode的孩子节点
                chilMenus.add(buildChilTree(menuNode));
            }
        }
        pNode.setChildren(chilMenus);
        return pNode;
    }

    //获取根节点
    private List<Menu> getRootNode() {
        List<Menu> rootMenuLists = new ArrayList<Menu>();
        // menuList：初始化MenuTree时的的数据
        for (Menu menuNode : menuList) {
            // 判断ParentId是否为0，判断是否为根节点
            if (menuNode.getParentId().equals("0")) {
                // 如果是将根节点的数据添加进list中
                rootMenuLists.add(menuNode);
            }
        }
        // 返回List<Menu>，list中包含所有根节点的数据
        return rootMenuLists;
    }

}
```





### 2.实战应用

##### TreeCommon.java

```java
package com.wiscom.domain;

import java.util.List;

/**
 * @program: base
 * @description: 用于返回树的信息
 * @author: 王新晖
 * @create: 2020/05/13
 */
public class TreeCommon {
    private String id;
    private String parentId;
    private String name;
    private List<TreeCommon> children;
}

```

##### TreesController.java

```java
package com.wiscom.controller;

import com.wiscom.Application;
import com.wiscom.common.ApiResult;
import com.wiscom.service.ITreesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;

/**
 * @program: base
 * @description: 实现系统树
 * @author: 王新晖
 * @create: 2020/5/7
 */
@Component
@Path("trees")
@Api(value = "实现系统树的接口-TreesController", description = "系统树接口")
public class TreesController {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ITreesService iTreesService;

    @GET
    @Path("getTwoDoorTree")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("二道门在岗管理-二级页面的三级树- 待优化")
//    @Authorization
    public ApiResult getTwoDoorTree(
    ) {

        return ApiResult.Success(iTreesService.getTwoDoorTree());
    }


    @GET
    @Path("getEarlyWarningTree")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("首页-应急预警数量分析-危化品二级页面三级树")
//    @Authorization
    public ApiResult getEarlyWarningTree(
    ) {
        return ApiResult.Success(iTreesService.getEarlyWarningTree());
    }

    @GET
    @Path("getMajorHazardSourcesTree")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("重大危险源备案-一级页面四级树")
//    @Authorization
    public ApiResult getMajorHazardSourcesTree(
    ) {

        return ApiResult.Success(iTreesService.getMajorHazardSourcesTree());
    }
}
```

##### ITreesService.java

```java
package com.wiscom.service;

import com.wiscom.domain.TreeCommon;

import java.util.List;


public interface ITreesService {

    List<TreeCommon> getTwoDoorTree();

    List<TreeCommon> getEarlyWarningTree();

    List<TreeCommon> getMajorHazardSourcesTree();
}
```

##### TreesService.java

```java
package com.wiscom.service.impl;

import com.wiscom.domain.TreeCommon;
import com.wiscom.repository.TreesMapper;
import com.wiscom.service.ITreesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TreesService implements ITreesService {
    @Autowired
    TreesMapper treesMapper;

    // 二道门在岗管理，二级页面树，待优化
    @Override
    public List<TreeCommon> getTwoDoorTree() {
        // 查询第三级数据,二道门企业的id,name,parentId
        List<TreeCommon> thirdLevelTree = treesMapper.getTwoDoorTreeThirdLevelTree();
        // 查询第二级数据,区域的id，name，parentId
        List<TreeCommon> secondLevelTree = treesMapper.getTwoDoorTreeSecoundLevelTree();
        //查询第一级数据，市的id，name，parentId
        List<TreeCommon> firstLevelTree = treesMapper.getTwoDoorTreeFirstLevelTree();

        // 将查询到的所有数据整合到一起
        List<TreeCommon> allLevelTree = new ArrayList<>();

        // 将所有的list数据合并到thirdLevelTree中
        allLevelTree.addAll(thirdLevelTree);
        allLevelTree.addAll(secondLevelTree);
        allLevelTree.addAll(firstLevelTree);

        // 用于存储放回的数据
        List<TreeCommon> treeReturnInfo = new ArrayList<>();

        // 建立树形结构
        for (TreeCommon treeCommon : firstLevelTree) {
            // 调用递归方法，根据第一级树的信息，为其创建所有子树
            treeCommon = buildChildTree(treeCommon, allLevelTree);
            // treeReturnInfo：存储以经创建好的树，最后返回给前端
            treeReturnInfo.add(treeCommon);
        }

        return treeReturnInfo;
    }

    // 首页-应急预警数量分析-二级页面危化品树
    @Override
    public List<TreeCommon> getEarlyWarningTree() {
        // 查询第三级数据,危化品企业的id,name,parentId
        List<TreeCommon> thirdLevelTree = treesMapper.getDangerousChemicalsThirdLevelTree();
        // 查询第二级数据,区域的id，name，parentId
        List<TreeCommon> secondLevelTree = treesMapper.getDangerousChemicalSecoundLevelTree();
        //查询第一级数据，市的id，name，parentId
        List<TreeCommon> firstLevelTree = treesMapper.getDangerousChemicalFirstLevelTree();

        // 将查询到的所有数据整合到一起
        List<TreeCommon> allLevelTree = new ArrayList<>();

        // 将所有的list数据合并到thirdLevelTree中
        allLevelTree.addAll(thirdLevelTree);
        allLevelTree.addAll(secondLevelTree);
        allLevelTree.addAll(firstLevelTree);

        // 用于存储放回的数据
        List<TreeCommon> treeReturnInfo = new ArrayList<>();

        // 建立树形结构
        for (TreeCommon treeCommon : firstLevelTree) {
            // 调用递归方法，根据第一级树的信息，为其创建所有子树
            treeCommon = buildChildTree(treeCommon, allLevelTree);
            // treeReturnInfo：存储以经创建好的树，最后返回给前端
            treeReturnInfo.add(treeCommon);
        }

        return treeReturnInfo;
    }

    @Override
    public List<TreeCommon> getMajorHazardSourcesTree() {
        // 查询第四级数据,重大危险源名称的id,name,parentId
        List<TreeCommon> forthLevelTree = treesMapper.getMajorHazardSourcesForthLevelTree();
        // 查询第三级级数据,相关企业的id,name,parentId
        List<TreeCommon> thirdLevelTree = treesMapper.getMajorHazardSourcesThirdLevelTree();
        // 查询第二级级数据,相关区的id,name,parentId
        List<TreeCommon> secondLevelTree = treesMapper.getMajorHazardSourcesSecondLevelTree();
        // 查询第一级级数据,相关区的id,name,parentId
        List<TreeCommon> firstLevelTree = treesMapper.getMajorHazardSourcesFirstLevelTree();

        // 将查询到的所有数据整合到一起
        List<TreeCommon> allLevelTree = new ArrayList<>();

        // 将所有的list数据合并到thirdLevelTree中
        allLevelTree.addAll(forthLevelTree);
        allLevelTree.addAll(thirdLevelTree);
        allLevelTree.addAll(secondLevelTree);
        allLevelTree.addAll(firstLevelTree);


        // 用于存储放回的数据
        List<TreeCommon> treeReturnInfo = new ArrayList<>();

        // 建立树形结构
        for (TreeCommon treeCommon : firstLevelTree) {
            // 调用递归方法，根据第一级树的信息，为其创建所有子树
            treeCommon = buildChildTree(treeCommon, allLevelTree);
            // treeReturnInfo：存储以经创建好的树，最后返回给前端
            treeReturnInfo.add(treeCommon);
        }

        return treeReturnInfo;
    }


    // 根据一级树数据，使用递归，建立子树形结构
    // pNode：父节点的数据
    // allLevelTree：查询到的所有节点的数据
    public TreeCommon buildChildTree(TreeCommon pNode, List<TreeCommon> allLevelTree) {

        // 用于存储父级树下的子树数据
        List<TreeCommon> levelTree = new ArrayList<>();

        // 每一层遍历的allLevelTree都相同
        ArrayList<TreeCommon> alltreesInfo = new ArrayList<>();
        alltreesInfo.addAll(allLevelTree);

        // 遍历所有传入的数据
        for (TreeCommon index : alltreesInfo) {
            // 如果index的parentId与pNode的id相等，则index为pNode的子节点
            if (pNode.getId().equals(index.getParentId())) {
                // index为pNode的子节点，则将子节点的数据存入levelTree
                levelTree.add(buildChildTree(index, alltreesInfo));
            }
        }
        pNode.setChildren(levelTree);
        return pNode;
    }
}
```

##### TreesMapper.java

```java
package com.wiscom.repository;

import com.wiscom.domain.TreeCommon;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreesMapper {
    // 危化品第三级树信息-企业
    List<TreeCommon> getDangerousChemicalsThirdLevelTree();
    // 危化品第二级树信息-相关区
    List<TreeCommon> getDangerousChemicalSecoundLevelTree();
    // 危化品第二级树信息-相关市
    List<TreeCommon> getDangerousChemicalFirstLevelTree();

    // 重大危险源备案四级树信息-相关危险源
    List<TreeCommon> getMajorHazardSourcesForthLevelTree();
    // 重大危险源备案三级树信息-相关企业
    List<TreeCommon> getMajorHazardSourcesThirdLevelTree();
    // 重大危险源备案二级树信息-相关区
    List<TreeCommon> getMajorHazardSourcesSecondLevelTree();
    // 重大危险源备案一级树信息-相关市
    List<TreeCommon> getMajorHazardSourcesFirstLevelTree();

    // 二道门三级树信息-相关企业
    List<TreeCommon> getTwoDoorTreeThirdLevelTree();
    // 二道门二级树信息-相关区
    List<TreeCommon> getTwoDoorTreeSecoundLevelTree();
    // 二道门一级树信息-相关市
    List<TreeCommon> getTwoDoorTreeFirstLevelTree();
}
```

##### TreesMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.wiscom.repository.TreesMapper">
    <!--危化品第三级树信息-->
    <select id="getDangerousChemicalsThirdLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT  cee.`AREA_CODE` id,LEFT(cee.AREA_CODE, 6) parentId, cms.`ENTERPRISE_NAME` `name`
        FROM c_mw_sensoralert cms
        INNER JOIN c_ei_enterpriseinfo cee ON cms.`ENTERPRISE_ID`=cee.`ENTERPRISE_ID`
        INNER JOIN cfg_precinct cp ON LEFT(cee.AREA_CODE, 6)=cp.`precinct_id`
        GROUP BY cms.`ENTERPRISE_ID`
    </select>
    <!--危化品第二级树信息-相关区-->
    <select id="getDangerousChemicalSecoundLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT LEFT(cee.AREA_CODE, 6) id,cp.`up_precinct_id` parentId,cp.`precinct_name` `name`
        FROM c_mw_sensoralert cms
        INNER JOIN c_ei_enterpriseinfo cee ON cms.`ENTERPRISE_ID`=cee.`ENTERPRISE_ID`
        INNER JOIN cfg_precinct cp ON LEFT(cee.AREA_CODE, 6)=cp.`precinct_id`
        GROUP BY cms.`ENTERPRISE_ID`
    </select>
    <!--危化品第一级树信息-相关市-->
    <select id="getDangerousChemicalFirstLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT cp1.`precinct_id` id,cp1.`up_precinct_id` parentId ,cp1.`precinct_name` `name`
        FROM c_mw_sensoralert cms
        INNER JOIN c_ei_enterpriseinfo cee ON cms.`ENTERPRISE_ID`=cee.`ENTERPRISE_ID`
        INNER JOIN cfg_precinct cp ON LEFT(cee.AREA_CODE, 6)=cp.`precinct_id`
        INNER JOIN cfg_precinct cp1 ON cp.`up_precinct_id`=cp1.`precinct_id`
	    GROUP BY cp1.`precinct_id`
    </select>


    <!--重大危险源备案四级树信息-相关危险源-->
    <select id="getMajorHazardSourcesForthLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT ced.`DANGSRC_NAME` `name`,ced.`DANGSRC_ID` id,cee.`ENTERPRISE_ID` parentId
        FROM c_ei_dangsrc ced
        INNER JOIN c_ei_enterpriseinfo cee ON ced.`ENTERPRISE_ID`=cee.`ENTERPRISE_ID`
    </select>

    <!--重大危险源备案三级树信息-相关企业-->
    <select id="getMajorHazardSourcesThirdLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT cee.`ENTERPRISE_ID` id,cee.`ENTERPRISE_NAME` `name`,cp.`precinct_id` parentId
        FROM c_ei_dangsrc ced
        INNER JOIN c_ei_enterpriseinfo cee ON ced.`ENTERPRISE_ID`=cee.`ENTERPRISE_ID`
        INNER JOIN cfg_precinct cp ON LEFT(cee.AREA_CODE, 6)=cp.`precinct_id`
    </select>

    <!--重大危险源备案二级树信息-相关区-->
    <select id="getMajorHazardSourcesSecondLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT cp.`precinct_id` id ,cp.`precinct_name` `name`,cp1.`precinct_id` parentId
        FROM c_ei_dangsrc ced
        INNER JOIN c_ei_enterpriseinfo cee ON ced.`ENTERPRISE_ID`=cee.`ENTERPRISE_ID`
        INNER JOIN cfg_precinct cp ON LEFT(cee.AREA_CODE, 6)=cp.`precinct_id`
        INNER JOIN cfg_precinct cp1 ON cp.up_precinct_id=cp1.precinct_id
    </select>

    <!--重大危险源备案一级树信息-相关市-->
    <select id="getMajorHazardSourcesFirstLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT cp1.`precinct_id` id,cp1.`precinct_name` `name`, cp2.`precinct_id` parentId
        FROM c_ei_dangsrc ced
        INNER JOIN c_ei_enterpriseinfo cee ON ced.`ENTERPRISE_ID`=cee.`ENTERPRISE_ID`
        INNER JOIN cfg_precinct cp ON LEFT(cee.AREA_CODE, 6)=cp.`precinct_id`
        INNER JOIN cfg_precinct cp1 ON cp.up_precinct_id=cp1.precinct_id
        INNER JOIN cfg_precinct cp2 ON cp1.up_precinct_id=cp2.precinct_id
        GROUP BY cp1.`precinct_id`
    </select>

    <!--二道门三级树信息-相关企业-->
    <select id="getTwoDoorTreeThirdLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT cp.`precinct_id` id, cp.`precinct_name` `name`,cp.`up_precinct_id` parentId
        FROM cfg_precinct_twodoor cpt
        INNER JOIN cfg_precinct cp ON cpt.`up_precinct_id` = cp.`precinct_id`
        WHERE exist_twodoor=1
    </select>

    <!--二道门二级树信息-相关区-->
    <select id="getTwoDoorTreeSecoundLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT cp1.`precinct_id` id,cp1.`precinct_name` `name`,cp1.`up_precinct_id` parentId
        FROM cfg_precinct_twodoor cpt
        INNER JOIN cfg_precinct cp ON cpt.`up_precinct_id` = cp.`precinct_id`
        INNER JOIN cfg_precinct cp1 ON cp.`up_precinct_id` = cp1.`precinct_id`
        WHERE exist_twodoor=1
    </select>

    <!--二道门一级树信息-相关市-->
    <select id="getTwoDoorTreeFirstLevelTree" resultType="com.wiscom.domain.TreeCommon">
        SELECT cp2.`precinct_id` id,cp2.`precinct_name` `name`,cp2.`up_precinct_id` parentId
        FROM cfg_precinct_twodoor cpt
        INNER JOIN cfg_precinct cp ON cpt.`up_precinct_id` = cp.`precinct_id`
        INNER JOIN cfg_precinct cp1 ON cp.`up_precinct_id` = cp1.`precinct_id`
        INNER JOIN cfg_precinct cp2 ON cp1.`up_precinct_id` = cp2.`precinct_id`
        WHERE exist_twodoor=1
        GROUP BY cp2.precinct_id
    </select>
</mapper>
```





### 3.将递归树改为循环树

> 由于应急预警树出现栈溢出错误，故将应急预警递归树改为循环树

```java
package com.wiscom.service.impl;

import com.wiscom.domain.TreeCommon;
import com.wiscom.repository.TreesMapper;
import com.wiscom.service.ITreesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TreesService implements ITreesService {
    @Autowired
    TreesMapper treesMapper;


    // 首页-应急预警数量分析-二级页面危化品树
    @Override
    public List<TreeCommon> getEarlyWarningTree() {
        // 查询第三级数据,危化品企业的id,name,parentId
        List<TreeCommon> thirdLevelTree = treesMapper.getDangerousChemicalsThirdLevelTree();
        // 查询第二级数据,区域的id，name，parentId
        List<TreeCommon> secondLevelTree = treesMapper.getDangerousChemicalSecoundLevelTree();
        //查询第一级数据，市的id，name，parentId
        List<TreeCommon> firstLevelTree = treesMapper.getDangerousChemicalFirstLevelTree();

        // 用于存储放回的数据

        return buildChildTree(firstLevelTree, secondLevelTree, thirdLevelTree);
    }

    // 首页-应急预警数量分析-二级页面危化品树common
    // first：第一级树
    // second：第二级树信息
    // third：第三级树信息
    public List<TreeCommon> buildChildTree(List<TreeCommon> first, List<TreeCommon> second,List<TreeCommon> third) {

        // 存一级节点的数据
        List<TreeCommon> commonFirst = new ArrayList<>();


        // 存第二级遍历的数据
        // 遍历所有传入的数据
        for (TreeCommon firstTree : first) {

            // 存第二级遍历的数据
            List<TreeCommon> commonsSecond = new ArrayList<>();

            for (TreeCommon secondTree : second){

                // 存第三级遍历的数据
                List<TreeCommon> commonsThird = new ArrayList<>();

                // 将所有匹配当前第二级的数据存入commonsThird中
                for (TreeCommon thirdTree : third){
                    if (secondTree.getId().equals(thirdTree.getParentId())) {
                        commonsThird.add(thirdTree);
                        System.out.println();
                    }
                }
                // 如果commonsThird不为空，则表示，该二级节点下有数据,将数据存入二级节点
                    secondTree.setChildren(commonsThird);

                if (secondTree.getParentId().equals(firstTree.getId())){
                    // 将当前节点存入commonsSecond中
                    commonsSecond.add(secondTree);
                }

                // 将二级节点的list存入一级节点的子节点中
                firstTree.setChildren(commonsSecond);
            }
            commonFirst.add(firstTree);
            System.out.println();

        }

        return commonFirst;
    }
}


```