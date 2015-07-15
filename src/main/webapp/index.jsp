<%@page pageEncoding="gbk"%>
<!DOCTYPE HTML>
<html lang="zh-cn">
<head>
    <meta content="text/html; charset=gbk" http-equiv="Content-Type" />
    <script src="js/jquery-1.6.4.min.js" type="text/javascript"></script>
    <script src="js/index.js" type="text/javascript"></script>
    <title>index</title>
</head>
<body>
<div>
    <div id="pro"></div>
    <br>
    <div id="proinfo"></div>
    <br>
    <br><br>
    <p>添加方案</p>
    <form action="/addproject" method="get">
        <p>名称: <input type="text" name="name" value="方案名称"/></p>
        <p>信息: <input type="text" name="info" value="方案信息"/></p>
        <p>状态: <input type="text" name="type" value="1"/></p>
        <p>用户id: <input type="text" name="uid" value="4"/></p>
        <input type="submit" value="提交" />
    </form>
</div>

<div>
    <p>添加股票</p>
    <form action="/addproinfo" method="get">
        <p>方案id: <input type="text" name="pid" value="6"/></p>
        <p>股票代码: <input type="text" name="sid" value="sh600773"/></p>
        <p>百分比: <input type="text" name="size" value="0.2"/></p>
        <input type="submit" value="提交" />
    </form>
</div>
</body>
</html>
