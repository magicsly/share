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
    <p>��ӷ���</p>
    <form action="/addproject" method="get">
        <p>����: <input type="text" name="name" value="��������"/></p>
        <p>��Ϣ: <input type="text" name="info" value="������Ϣ"/></p>
        <p>״̬: <input type="text" name="type" value="1"/></p>
        <p>�û�id: <input type="text" name="uid" value="4"/></p>
        <input type="submit" value="�ύ" />
    </form>
</div>

<div>
    <p>��ӹ�Ʊ</p>
    <form action="/addproinfo" method="get">
        <p>����id: <input type="text" name="pid" value="6"/></p>
        <p>��Ʊ����: <input type="text" name="sid" value="sh600773"/></p>
        <p>�ٷֱ�: <input type="text" name="size" value="0.2"/></p>
        <input type="submit" value="�ύ" />
    </form>
</div>
</body>
</html>
