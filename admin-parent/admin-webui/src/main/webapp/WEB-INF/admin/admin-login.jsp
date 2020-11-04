<%--
  Created by IntelliJ IDEA.
  User: qqgsd
  Date: 2020/9/23
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/login.css">
    <script src="jquery/jquery-2.1.1.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <style>

    </style>
    <title></title>
</head>
<body>
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <div><a class="navbar-brand" href="index.html" style="font-size:32px;">众筹网-创意产品众筹平台</a></div>
            </div>
        </div>
    </nav>

    <div class="container">

        <form action="admin/do/login.html" method="post" class="form-signin" role="form">
            <h2 style="text-align: center" class="form-signin-heading">
                管理员登录
            </h2>
            <p>${requestScope.exception.message}</p>
            <p>${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
            <div class="form-group has-success has-feedback">
                <label for="inputSuccess3"></label>
                <input type="text" name="loginAcct" class="form-control"
                       id="inputSuccess3" placeholder="请输入登录账号" autofocus>
                <span class="glyphicon glyphicon-user form-control-feedback"></span>
            </div>
            <div class="form-group has-success has-feedback">
                <label for="inputSuccess4"></label>
                <input type="text" name="userPwd" class="form-control"
                       id="inputSuccess4" placeholder="请输入登录密码" style="margin-top:10px;">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <button type="submit" class="btn btn-lg btn-success btn-block">登录</button>
        </form>
    </div>

</body>
</html>