<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: qqgsd
  Date: 2020/9/29
  Time: 19:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<%@include file="/WEB-INF/include/head.jsp"%>
<link rel="stylesheet" href="css/pagination.css">
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript">
    $(function (){

        // 页码导航条初始化
        initPagination()

    })

    // 页码导航条初始化
    function initPagination(){

        // 总记录数
        let total=${requestScope.pageInfo.total};

        //声明json对象存储Pagination要设置的属性
        let properties={
            num_edge_entries: 3,                                // 边缘页
            num_display_entries: 5,                             // 主体页
            callback: pageSelectCallback,                       // 翻页按钮
            items_per_page: ${requestScope.pageInfo.pageSize},  // 每页显示的数量
            current_page: ${requestScope.pageInfo.pageNum-1},   // 当前页
            prev_text: "上一页",
            next_text: "下一页"
        };

        //生成页码导航条
        $("#Pagination").pagination(total,properties);
    }

    //实现页面跳转
    function pageSelectCallback(pageIndex,jQuery){

        // pageNum
        let pageNum=pageIndex+1;

        //跳转页面
        window.location.href="admin/get/page.html?pageNum="+pageNum+"&keyword=${param.keyword}";

        //由于按钮是超链接，所以取消超链接的默认行为
        return false
    }
</script>
<body>
    <%@include file="/WEB-INF/include/nav.jsp"%>
    <div class="container-fluid">
        <div class="row">
            <%@include file="/WEB-INF/include/sidebar.jsp"%>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                    </div>
                    <div class="panel-body">
                        <form class="form-inline" action="admin/get/page.html" role="form" style="float:left;">
                            <div class="form-group has-feedback">
                                <div class="input-group">
                                    <div class="input-group-addon">查询条件</div>
                                    <input name="keyword" class="form-control has-success" type="text" placeholder="请输入查询条件">
                                </div>
                            </div>
                            <button type="submit" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                        </form>
                        <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                        <a href="admin/to/add/page.html" style="float:right;margin-left:10px;" class="btn btn-primary"><i class="glyphicon glyphicon-plus"></i> 新增</a>
                        <br>
                        <hr style="clear:both;">
                        <div class="table-responsive">
                            <table class="table  table-bordered">
                                <thead>
                                <tr>
                                    <th width="30">#</th>
                                    <th width="30"><label>
                                        <input type="checkbox">
                                    </label></th>
                                    <th>账号</th>
                                    <th>名称</th>
                                    <th>邮箱地址</th>
                                    <th width="100">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:if test="${empty requestScope.pageInfo.list}">
                                    <tr>
                                        <td colspan="6" align="center">抱歉！没有查询到您要的数据!</td>
                                    </tr>
                                </c:if>
                                <c:if test="${!empty requestScope.pageInfo.list}">
                                    <c:forEach items="${requestScope.pageInfo.list}" var="admin" varStatus="myStaus">
                                        <tr>
                                            <td>${myStaus.count}</td>
                                            <td><label>
                                                <input type="checkbox">
                                            </label></td>
                                            <td>${admin.loginAcct}</td>
                                            <td>${admin.userName}</td>
                                            <td>${admin.email}</td>
                                            <td>
                                                <a href="assign/to/assign/role/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-success btn-xs glyphicon glyphicon-check"></a>
                                                <a href="admin/to/edit/page.html?adminId=${admin.id}&pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}" class="btn btn-primary btn-xs glyphicon glyphicon-pencil"></a>
                                                <a href="admin/remove/${admin.id}/${requestScope.pageInfo.pageNum}/${param.keyword}.html" class="btn btn-danger btn-xs glyphicon glyphicon-remove"></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>

                                </tbody>
                                <tfoot>
                                <tr>
                                    <td colspan="6" align="center">
                                        <div id="Pagination" class="pagination"></div>
                                    </td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
