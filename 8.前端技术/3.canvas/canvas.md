## 主要参考

> `https://www.liaoxuefeng.com/wiki/1022910821149312/1023022423592576`	-- 阮一峰Canvas笔记
>
> `https://www.w3school.com.cn/tags/html_ref_canvas.asp`		-- Canvas的Api
>
> `https://developer.mozilla.org/zh-CN/docs/Web/API/Canvas_API/Tutorial`		-- Canvas教程



## canvas简介

> 画布是一个矩形区域，您**可以控制其每一像素**。  
>
> canvas拥有多种绘制路径、矩形、圆形、字符以及添加图像的方法



## 画布操作

### 准备画布

> 画布是**白色**的 而且**默认300*150**
>
> > `<canvas>` 标签**只有两个属性** —— `width`和`height`

> 如果CSS的尺寸与初始画布的比例不一致，它会出现扭曲
>
> > **建议在标签中添加属性，不建议在Css中**

##### 案例：

```html
<canvas id="test-canvas" width="300" height="200"></canvas>
```

### 验证浏览器是否支持Canvas

##### 案例：

```js
var canvas = document.getElementById('test-canvas');
if (canvas.getContext) {
    console.log('你的浏览器支持Canvas!');
} else {
    console.log('你的浏览器不支持Canvas!');
}
```

### 实战案例

> `var ctx = canvas.getContext('2d');`		// 2D绘制
>
> `var gl = canvas.getContext("webgl");`			// 3D绘制,暂不支持

##### 2D案例：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas{
            border: 1px solid #ccc;
            /*不建议在 样式设置尺寸*/
            /*width: 600px;
            height: 400px;*/
        }
    </style>
</head>
<body>
<!--1.准备画布-->
<!--1.1 画布是白色的 而且默认300*150-->
<!--1.2 设置画布的大小  width="600" height="400" -->
<canvas width="600" height="400" ></canvas>
<!--2.准备绘制工具-->
<!--3.利用工具绘图-->
<script>
    /*1.获取元素*/
    var myCanvas = document.querySelector('canvas');
    /*2.获取上下文 绘制2D */
    var ctx = myCanvas.getContext('2d'); /*web gl 绘制3d效果的网页技术*/
</script>
</body>
</html>
```

##### 绘制多条样式不同的直线案例：

````html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas{
            border: 1px solid #ccc;
            /*不建议在 样式设置尺寸*/
            /*width: 600px;
            height: 400px;*/
        }
    </style>
</head>
<body>
<canvas width="600" height="400" ></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d'); /*web gl 绘制3d效果的网页技术*/
    /*3.移动画笔*/
    ctx.moveTo(100,100);
    /*4.绘制直线 (轨迹，绘制路径)*/
    // 默认的宽度 1px
    ctx.lineTo(200,100);
    /*5.描边*/
    ctx.stroke();

    // 表示开启新路径，修改样式不会影响上面的代码
    ctx.beginPath();
    /*红色 20px*/
    ctx.moveTo(100,200);
    ctx.lineTo(300,200);
    ctx.strokeStyle = 'red';
    ctx.lineWidth = 20;
    /*描边*/
    ctx.stroke();

    ctx.beginPath();/*Kai*/
    /*绿色 30px*/
    ctx.moveTo(100,300);
    ctx.lineTo(300,300);
    ctx.strokeStyle = 'green';
    ctx.lineWidth = 30;
    /*描边*/
    ctx.stroke();

</script>
</body>
</html>
````

##### 绘制一个填充的三角形案例

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas {
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<canvas width="600" height="400"></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d');

    /*1.绘制一个三角形*/
	// 移动画笔
    ctx.moveTo(100,100);
    ctx.lineTo(200,100);
    ctx.lineTo(200,200);
    
    // 问题:  起始点和lineTo的结束点无法完全闭合缺角
    // ctx.lineTo(100,100);

    // 解决：关闭路径
    ctx.closePath();
    
    ctx.lineWidth = 10;
    /*2.描边*/
    //ctx.stroke();
    /*3.填充*/
    ctx.fill();

</script>
</body>
</html>
```

##### 使用填充绘制一个镂空正方形

> 填充时需要遵循非零环绕规则

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas {
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<canvas width="600" height="400"></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d');

    /*1.绘制两个正方形 一大200一小100 套在一起*/
    ctx.moveTo(100,100);
    ctx.lineTo(300,100);
    ctx.lineTo(300,300);
    ctx.lineTo(100,300);
    ctx.closePath();

    ctx.moveTo(150,150);
    ctx.lineTo(150,250);
    ctx.lineTo(250,250);
    ctx.lineTo(250,150);
    ctx.closePath();

    /*2.去填充*/
    //ctx.stroke();
    ctx.fillStyle = 'red';
    ctx.fill();

    /*在填充的时候回遵循非零环绕规则*/
</script>
</body>
</html>
```

##### 拐点样式和线两端样式

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas {
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<canvas width="600" height="400"></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d');

    /*画平行线*/
    ctx.beginPath();
    ctx.moveTo(100,100);
    ctx.lineTo(200,20);
    ctx.lineTo(300,100);
    ctx.strokeStyle = 'blue';
    ctx.lineWidth = 10;
    // 线两端的样式 
    ctx.lineCap = 'butt';
    // ctx.lineCap = 'square';
    // ctx.lineCap = 'round';
    
   	// 线段拐点的样式
    ctx.lineJoin = 'miter';
    // ctx.lineJoin = 'bevel';
    // ctx.lineJoin = 'round';
    
    ctx.stroke();
</script>
</body>
</html>
```

##### 绘制虚线

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas {
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<canvas width="600" height="400"></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d');

    /*画线*/
    ctx.moveTo(100,100.5);
    ctx.lineTo(300,100.5);
    /*[5,10] 数组是用来描述你的排列方式的*/
    ctx.setLineDash([20]);
    /*获取虚线的排列方式 获取的是不重复的那一段的排列方式*/
    console.log(ctx.getLineDash());

    /*如果是正的值 往后偏移*/
    /*如果是负的值 往前偏移*/
    ctx.lineDashOffset = -20;

    ctx.stroke();

</script>
</body>
</html>
```

##### 绘制矩形

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas {
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<canvas width="600" height="400"></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d');

    /*绘制矩形路径 不是独立路径*/
    /*ctx.rect(100,100,200,100);
    ctx.fillStyle = 'green';
    ctx.stroke();
    ctx.fill();*/

    /*绘制矩形  有自己的独立路径*/
    //ctx.strokeRect(100,100,200,100);
    ctx.fillRect(100,100,200,100);

    /*清除矩形的内容*/
    //ctx.clearRect(0,0,ctx.canvas.width,ctx.canvas.height);

    //ctx.fillRect(100,100,200,100);

</script>
</body>
</html>
```

##### 绘制渐变的矩形

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas {
            border: 1px solid #ccc;
        }
/*        .linearGradient{
            width: 400px;
            height: 100px;
            background-image: linear-gradient(to right,pink,blue);
        }*/
    </style>
</head>
<body>
<div class="linearGradient"></div>
<canvas width="600" height="400"></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d');

    /*fillStyle 'pink' '#000' 'rgb()' 'rgba()' */
    /*也可以使用一个渐变的方案了填充矩形*/
    /*创建一个渐变的方案*/
    /*渐变是由长度的*/
    /*x0y0 起始点 x1y1 结束点  确定长度和方向*/
    var linearGradient = ctx.createLinearGradient(100,100,500,400);
    linearGradient.addColorStop(0,'pink');
    //linearGradient.addColorStop(0.5,'red');
    linearGradient.addColorStop(1,'blue');

    ctx.fillStyle = linearGradient;

    ctx.fillRect(100,100,400,100);

    /*pink---->blue*/
    /*回想线性渐变---->要素 方向  起始颜色 结束颜色 */
    /*通过两个点的坐标可以控制 渐变方向*/
</script>
</body>
</html>
```

##### 绘制圆弧

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas {
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<canvas width="600" height="400"></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d');

    /*绘制圆弧*/
    /*确定圆心  坐标 x y*/
    /*确定圆半径  r */
    /*确定起始绘制的位置和结束绘制的位置  确定弧的长度和位置  startAngle endAngle   弧度*/
    /*取得绘制的方向 direction 默认是顺时针 false 逆时针 true */

    /*在中心位置画一个半径150px的圆弧左下角*/
    var w = ctx.canvas.width;
    var h = ctx.canvas.height;
    ctx.arc(w/2,h/2,150,Math.PI/2,Math.PI,true);
    ctx.stroke();


</script>
</body>
</html>
```

##### 绘制在画布中心的一段文字

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas {
            border: 1px solid #ccc;
            display: block;
            margin: 100px auto;
        }
    </style>
</head>
<body>
<canvas width="600" height="400"></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d');

    /*1.在画布的中心绘制一段文字*/
    /*2.申明一段文字*/
    var str = '您吃-,了吗';
    /*3.确定画布的中心*/
    var w = ctx.canvas.width;
    var h = ctx.canvas.height;
    /*4.画一个十字架在画布的中心*/
    ctx.beginPath();
    ctx.moveTo(0, h / 2 - 0.5);
    ctx.lineTo(w, h / 2 - 0.5);
    ctx.moveTo(w / 2 - 0.5, 0);
    ctx.lineTo(w / 2 - 0.5, h);
    ctx.strokeStyle = '#eee';
    ctx.stroke();
    /*5.绘制文本*/
    ctx.beginPath();
    ctx.strokeStyle = '#000';
    var x0 = w/2;
    var y0 = h/2;
    /*注意：起点位置在文字的左下角*/
    /*有文本的属性  尺寸 字体  左右对齐方式  垂直对齐的方式*/
    ctx.font = '40px Microsoft YaHei';
    /*左右对齐方式 (center left right start end) 基准起始坐标*/
    ctx.textAlign = 'center';
    /*垂直对齐的方式 基线 baseline(top,bottom,middle) 基准起始坐标*/
    ctx.textBaseline = 'middle';
    //ctx.direction = 'rtl';
    //ctx.strokeText(str,x0,y0);
    ctx.fillText(str,x0,y0);
    /*6.画一个下划线和文字一样长*/
    ctx.beginPath();
    /*获取文本的宽度*/
    console.log(ctx.measureText(str));
    var width = ctx.measureText(str).width;
    ctx.moveTo(x0-width/2,y0 + 20);
    ctx.lineTo(x0+width/2,y0 + 20);
    ctx.stroke();

</script>
</body>
</html>
```

##### 绘制图片

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        canvas {
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<!--<img src="image/01.jpg" alt="">-->
<canvas width="600" height="400"></canvas>
<script>
    var myCanvas = document.querySelector('canvas');
    var ctx = myCanvas.getContext('2d');

    var image = new Image();
    /*设置图片路径*/
    image.src = 'image/02.jpg';
    
    // 需要图片加载完成后调用
    image.onload = function () {
        /*实现图片绘制*/
        console.log(image);
        /*绘制图片的三种方式*/

        /*3参数*/
        /*图片对象*/
        /*绘制在画布上的坐标 x y*/
        //ctx.drawImage(image,100,100);


        /*5个参数*/
        /*图片对象*/
        /*绘制在画布上的坐标 x y*/
        /*是图片的大小  不是裁剪  是缩放*/
        ctx.drawImage(image,100,100,100,100);


        /*9个参数*/
        /*图片对象*/
        /*图片上定位的坐标  x y */
        /*在图片上截取多大的区域  w h*/
        /*绘制在画布上的坐标 x y*/
        /*是图片的大小  不是裁剪  是缩放*/
        // ctx.drawImage(image,400,400,400,400,200,200,100,100);

    };




    
</script>
</body>
</html>
```



## 画布API

### 路径

##### moveTo()

> 把路径**移动到**画布中的**指定点**，**不创建线条**
>
> > `context.moveTo(x,y);`
> >
> > > | 参数 | 描述                    |
> > > | :--- | :---------------------- |
> > > | *x*  | 路径的目标位置的 x 坐标 |
> > > | *y*  | 路径的目标位置的 y 坐标 |

##### lineTo()

> 添加一个新点，然后在画布中**创建从该点到最后指定点的线条**
>
> > `context.lineTo(x,y);`
> >
> > > | 参数 | 描述                    |
> > > | :--- | :---------------------- |
> > > | *x*  | 路径的目标位置的 x 坐标 |
> > > | *y*  | 路径的目标位置的 y 坐标 |

##### stroke()

> **描边**。实际地绘制出通过 moveTo() 和 lineTo() 方法定义的路径。**默认颜色是黑色**。

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.beginPath();
ctx.moveTo(20,20);
ctx.lineTo(20,100);
ctx.lineTo(70,100);
ctx.strokeStyle="green";
ctx.stroke();
```

##### arc()

> 创建弧/曲线（用于**创建圆或部分圆**）
>
> > `context.arc(x,y,r,sAngle,eAngle,counterclockwise);`
> >
> > > | 参数               | 描述                                                         |
> > > | :----------------- | :----------------------------------------------------------- |
> > > | *x*                | 圆的中心的 x 坐标。                                          |
> > > | *y*                | 圆的中心的 y 坐标。                                          |
> > > | *r*                | 圆的半径。                                                   |
> > > | *sAngle*           | 起始角，**以弧度计**。（弧的圆形的三点钟位置是 0 度）。      |
> > > | *eAngle*           | 结束角，**以弧度计**。                                       |
> > > | *counterclockwise* | **可选**。规定应该逆时针还是顺时针绘图。False = 顺时针，true = 逆时针。 |

```js
// 弧度与角度的关系
// >> 1角度 =  π/180
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.beginPath();
ctx.arc(100,75,50,0,2*Math.PI);
ctx.stroke();
```



### 线条样式

##### lineCap

> 设置或返回线条的**结束端点样式**
>
> > `context.lineCap="butt|round|square";`
> >
> > > | 值     | 描述                                       |
> > > | :----- | :----------------------------------------- |
> > > | butt   | **默认**。向线条的每个末端添加平直的边缘。 |
> > > | round  | 向线条的每个末端添加圆形线帽。             |
> > > | square | 向线条的每个末端添加正方形线帽。           |

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.beginPath();
ctx.lineCap="round";
ctx.moveTo(20,20);
ctx.lineTo(20,200);
ctx.stroke();
```

##### lineJoin

> 设置或返回**两条线相交**时，所创建的**拐角类型**
>
> > `context.lineJoin="bevel|round|miter";`
> >
> > > | 值    | 描述                 |
> > >| :---- | :------------------- |
> > > | bevel | 创建斜角。           |
> > >| round | 创建圆角。           |
> > > | miter | **默认**。创建尖角。 |

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.beginPath();
ctx.lineJoin="round";
ctx.moveTo(20,20);
ctx.lineTo(100,50);
ctx.lineTo(20,100);
ctx.stroke();
```

##### lineWidth

> 设置或返回当前的**线条宽度**
>
> > `context.lineWidth=*number*;`
> >
> > > | 值       | 描述                       |
> > > | :------- | :------------------------- |
> > > | *number* | 当前线条的宽度，以像素计。 |

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.lineWidth=10;
ctx.strokeRect(20,20,80,100);
```

##### setLineDash(segments);

> 设置实线与间隙长度
>
> > 参数
> >
> > > 一个参数
> > >
> > > > 表示线条长度和间隔长度相同
> > >
> > > 多个参数
> > >
> > > > 表示一组线条和间隔的大小

###### 案例：

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.moveTo(100,100.5);
ctx.lineTo(300,100.5);
ctx.setLineDash([5,10]);
ctx.stroke();

```

##### lineDashOffset

> 设置虚线偏移量（负值向右偏移，正值向左偏移）

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.moveTo(100,100.5);
ctx.lineTo(300,100.5);
ctx.setLineDash([5,10]);
stx.lineDashOffset = 10;			// 向左偏移10px
ctx.stroke();
```



### 矩形

##### rect(x,y,width,height)

> `context.rect(x,y,width,height);`
>
> > | 参数     | 描述                 |
> > | :------- | :------------------- |
> > | *x*      | 矩形左上角的 x 坐标  |
> > | *y*      | 矩形左上角的 y 坐标  |
> > | *width*  | 矩形的宽度，以像素计 |
> > | *height* | 矩形的高度，以像素计 |

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
/*该方法绘制矩形路径 不是独立路径
  不加ctx.beginPath(); 会对其他元素产生影响
*/
ctx.rect(20,20,150,100);	// 不是独立路径
ctx.stroke();
```



### 文本

##### font

> `context.font="italic small-caps bold 12px arial";`
>
> > | 值                          | 描述                                                         |
> > | :-------------------------- | :----------------------------------------------------------- |
> > | *font-style*                | 规定字体样式。可能的值：normal 、 italic 、 oblique          |
> > | *font-variant*              | 规定字体变体。可能的值：normal 、 small-caps                 |
> > | *font-weight*               | 规定字体的粗细。可能的值：normal、bold、bolder、lighter、100、200、300、400、500、600、700、800、900 |
> > | *font-size* / *line-height* | 规定字号和行高，以像素计。                                   |
> > | *font-family*               | 规定字体系列。                                               |
> > | caption                     | 使用标题控件的字体（比如按钮、下拉列表等）。                 |
> > | icon                        | 使用用于标记图标的字体。                                     |
> > | menu                        | 使用用于菜单中的字体（下拉列表和菜单列表）。                 |
> > | message-box                 | 使用用于对话框中的字体。                                     |
> > | small-caption               | 使用用于标记小型控件的字体。                                 |
> > | status-bar                  | 使用用于窗口状态栏中的字体。                                 |

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.font="40px Arial";
ctx.fillText("Hello World",10,50);
```

##### textAlign

> 文本内容的当前对齐方式。
>
> > `context.textAlign="center|end|left|right|start";`
> >
> > > | 值     | 描述                           |
> > > | :----- | :----------------------------- |
> > > | start  | 默认。文本在指定的位置开始。   |
> > > | end    | 文本在指定的位置结束。         |
> > > | center | 文本的中心被放置在指定的位置。 |
> > > | left   | 文本左对齐。                   |
> > > | right  | 文本右对齐。                   |

##### textBaseline

> `fillText()` 或 `strokeText()` 方法在画布上定位文本时，将使用指定的 textBaseline 值。
>
> > `context.textBaseline="alphabetic|top|hanging|middle|ideographic|bottom";`
> >
> > > | 值          | 描述                             |
> > > | :---------- | :------------------------------- |
> > > | alphabetic  | 默认。文本基线是普通的字母基线。 |
> > > | top         | 文本基线是 em 方框的顶端。。     |
> > > | hanging     | 文本基线是悬挂基线。             |
> > > | middle      | 文本基线是 em 方框的正中。       |
> > > | ideographic | 文本基线是表意基线。             |
> > > | bottom      | 文本基线是 em 方框的底端。       |

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");

//在位置 y=100 绘制蓝色线条
ctx.strokeStyle="blue";
ctx.moveTo(5,100);
ctx.lineTo(395,100);
ctx.stroke();

ctx.font="20px Arial"

//在 y=200 以不同的 textBaseline 值放置每个单词
ctx.textBaseline="top";
ctx.fillText("Top",5,100);
ctx.textBaseline="bottom";
ctx.fillText("Bottom",50,100);
ctx.textBaseline="middle";
ctx.fillText("Middle",120,100);
ctx.textBaseline="alphabetic";
ctx.fillText("Alphabetic",190,100);
ctx.textBaseline="hanging";
ctx.fillText("Hanging",290,100);
```

##### fillText()

> 请使用`font` 属性来定义字体和字号，并使用`fillStyle`属性以另一种颜色/渐变来渲染文本。

> 绘制**填色的文本**。文本的**默认**颜色是**黑色**。
>
> > `context.fillText(text,x,y,maxWidth);`
> >
> > > | 参数       | 描述                                      |
> > > | :--------- | :---------------------------------------- |
> > > | *text*     | 规定在画布上输出的文本。                  |
> > > | *x*        | 开始绘制文本的 x 坐标位置（相对于画布）。 |
> > > | *y*        | 开始绘制文本的 y 坐标位置（相对于画布）。 |
> > > | *maxWidth* | 可选。允许的最大文本宽度，以像素计。      |

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");

ctx.font="20px Georgia";
ctx.fillText("Hello World!",10,50);

ctx.font="30px Verdana";
// 创建渐变
var gradient=ctx.createLinearGradient(0,0,c.width,0);
gradient.addColorStop("0","magenta");
gradient.addColorStop("0.5","blue");
gradient.addColorStop("1.0","red");
// 用渐变填色
ctx.fillStyle=gradient;
ctx.fillText("w3school.com.cn",10,90);
```

##### strokeText()

> 请使用`font` 属性来定义字体和字号，并使用`fillStyle`属性以另一种颜色/渐变来渲染文本。

> 绘制文本（镂空，没有填色）。文本的默认颜色是黑色。
>
> > `context.strokeText(text,x,y,maxWidth);`
> >
> > > | 参数       | 描述                                      |
> > > | :--------- | :---------------------------------------- |
> > > | *text*     | 规定在画布上输出的文本。                  |
> > > | *x*        | 开始绘制文本的 x 坐标位置（相对于画布）。 |
> > > | *y*        | 开始绘制文本的 y 坐标位置（相对于画布）。 |
> > > | *maxWidth* | 可选。允许的最大文本宽度，以像素计。      |

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");

ctx.font="20px Georgia";
ctx.strokeText("Hello World!",10,50);

ctx.font="30px Verdana";
// 创建渐变
var gradient=ctx.createLinearGradient(0,0,c.width,0);
gradient.addColorStop("0","magenta");
gradient.addColorStop("0.5","blue");
gradient.addColorStop("1.0","red");
// 用渐变填色
ctx.strokeStyle=gradient;
ctx.strokeText("w3school.com.cn",10,90);
```

##### measureText() 

>  方法返回包含一个对象，该对象包含以像素计的指定字体宽度。
>
> > `context.measureText(text).width;`
> >
> > > | 参数   | 描述           |
> > > | :----- | :------------- |
> > > | *text* | 要测量的文本。 |

###### 案例

```js
var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.font="30px Arial";
var txt="Hello World"
ctx.fillText("width:" + ctx.measureText(txt).width,10,50)
ctx.fillText(txt,10,100);
```



### 图像的绘制

##### drawImage()

> 三种语法
>
> > 在画布上**定位图像**：
> >
> > > `context.drawImage(img,x,y);`
> >
> > 在画布上**定位图像**，并**规定**图像的**宽**度和**高**度：
> >
> > > `context.drawImage(img,x,y,width,height);`
> >
> > **剪切图像**，并在画布上定位被剪切的部分：
> >
> > > `context.drawImage(img,sx,sy,swidth,sheight,x,y,width,height);`
>
> > | 参数      | 描述                                         |
> > | :-------- | :------------------------------------------- |
> > | *img*     | 规定要使用的图像、画布或视频。               |
> > | *sx*      | 可选。开始剪切的 x 坐标位置。                |
> > | *sy*      | 可选。开始剪切的 y 坐标位置。                |
> > | *swidth*  | 可选。被剪切图像的宽度。                     |
> > | *sheight* | 可选。被剪切图像的高度。                     |
> > | *x*       | 在画布上放置图像的 x 坐标位置。              |
> > | *y*       | 在画布上放置图像的 y 坐标位置。              |
> > | *width*   | 可选。要使用的图像的宽度。（伸展或缩小图像） |
> > | *height*  | 可选。要使用的图像的高度。（伸展或缩小图像） |

###### 案例

```html
<!DOCTYPE html>
<html>
<body>

<p>要使用的图像：</p>
<img id="tulip" src="/i/eg_tulip.jpg" alt="The Tulip" />

<p>画布：</p>
<canvas id="myCanvas" width="500" height="300" style="border:1px solid #d3d3d3;background:#ffffff;">
Your browser does not support the HTML5 canvas tag.
</canvas>

<script>

var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
var img=document.getElementById("tulip");
img.onload = function(){
	ctx.drawImage(img,10,10);
}

</script>

</body>
</html>
```