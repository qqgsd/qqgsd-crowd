<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: qqgsd
  Date: 2020/10/9
  Time: 9:28
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: qqgsd
  Date: 2020/9/29
  Time: 19:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<%@include file="/WEB-INF/include/head.jsp"%>
<script type="text/javascript">
    $(function (){
        // 把未分配的角色放到已分配的角色列表
        $("#toRightBtn").click(function (){
            $("#unassigned>option:selected").appendTo($("#assigned"))
        });

        // 把已分配的角色放到未分配的角色列表
        $("#toLeftBtn").click(function (){
            $("#assigned>option:selected").appendTo($("#unassigned"))
        });

        // 保存更改
        $("#saveBtn").click(function (){
            $("#assigned>option").prop("selected","selected")
        })
    })
</script>
<body>
<%@include file="/WEB-INF/include/nav.jsp"%>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include/sidebar.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div style="text-align: center" class="panel-body">
                    <form action="assign/do/role/assign.html" method="post" role="form" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <label>未分配角色列表</label><br>
                            <label>
                                <select id="unassigned" class="form-control" multiple="multiple" size="10" style="width:200px;overflow-y:auto;">
                                    <c:forEach items="${requestScope.unassignedRoleList}" var="role">
                                        <option value="${role.id}">${role.name}</option>
                                    </c:forEach>
                                </select>
                            </label>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label>已分配角色列表</label><br>
                            <label>
                                <select id="assigned" name="roleIdList" class="form-control" multiple="multiple" size="10" style="width:200px;overflow-y:auto;">
                                    <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                        <option value="${role.id}">${role.name}</option>
                                    </c:forEach>
                                </select>
                            </label>
                        </div>
                        <button id="saveBtn" type="submit" style="width: 70px;margin: 50px auto 0 auto" class="btn btn-lg btn-success btn-block">保存</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
