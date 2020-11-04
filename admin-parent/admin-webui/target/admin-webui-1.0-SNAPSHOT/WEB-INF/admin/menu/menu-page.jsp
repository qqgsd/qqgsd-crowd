<%--
  Created by IntelliJ IDEA.
  User: qqgsd
  Date: 2020/10/7
  Time: 20:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<%@include file="/WEB-INF/include/head.jsp" %>
<link rel="stylesheet" href="ztree/zTreeStyle.css">
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/menu.js"></script>
<script type="text/javascript">
    $(function (){

        // 生成树形结构
        generateTree()

        let $tree = $("#tree");
        // 添加节点
        $tree.on("click",".addBtn",function (){
            // 当前节点的id作为新节点的pid（父节点id）
            window.pid=this.id;

            // 打开模态框
            $("#menuAddModal").modal("show");
            return false;
        });
        // 添加节点的模态窗中的保存按钮
        $("#menuSaveBtn").click(function (){
            // 收集输入的数据
            let name = $.trim($("#menuBody [name=name]").val());            // 节点名称
            let url = $.trim($("#menuBody [name=url]").val());              // 节点的url
            let icon = $.trim($("#menuBody [name=icon]:checked").val());    // 节点的图标
            $.ajax({
                url:"menu/save/node.json",
                data:{
                    pid:window.pid,
                    name:name,
                    url:url,
                    icon:icon,
                },
                type:"post",
                dataType:"json",
                success:function (response){
                    let result = response.result;
                    if (result==="SUCCESS"){
                        layer.msg("操作成功!")
                        // 刷新树形结构
                        generateTree();
                    }
                    else{
                        layer.msg("操作失败!"+response.message);
                    }
                },
                error:function (response){
                    layer.msg(response.status+" "+response.statusText)
                }
            });

            // 关闭模态窗
            $("#menuAddModal").modal("hide");

            // 清空表单
            $("#menuResetBtn").click();
        });

        // 修改节点
        $tree.on("click",".editBtn",function (){
            // 当前节点的id
            window.id=this.id;

            // 获取zTreeObj
            let zTreeObj = $.fn.zTree.getZTreeObj("tree");

            // 根据id获取节点
            let currentNode = zTreeObj.getNodeByParam("id",window.id);

            // 打开模态框
            $("#menuEditModal").modal("show");

            // 表单回显
            $("#menuEditModal [name=name]").val(currentNode.name)
            $("#menuEditModal [name=url]").val(currentNode.url)
            $("#menuEditModal [name=icon]").val([currentNode.icon])

            return false;
        })
        // 修改节点的模态窗的更新按钮
        $("#menuEditBtn").click(function (){
            // 收集数据
            let name = $.trim($("#menuEditModal [name=name]").val());
            let url = $.trim($("#menuEditModal [name=url]").val());
            let icon = $.trim($("#menuEditModal [name=icon]:checked").val());
            $.ajax({
                url:"menu/edit/node.json",
                data:{
                    id:window.id,
                    name:name,
                    url:url,
                    icon:icon,
                },
                type:"post",
                dataType:"json",
                success:function (response){
                    let result = response.result;
                    if (result==="SUCCESS"){
                        layer.msg("操作成功!")
                        // 刷新树形结构
                        generateTree();
                    }
                    else{
                        layer.msg("操作失败!"+response.message);
                    }
                },
                error:function (response){
                    layer.msg(response.status+" "+response.statusText)
                }
            });
            // 关闭模态窗
            $("#menuEditModal").modal("hide");
        });

        // 删除节点
        $tree.on("click",".removeBtn",function (){
            // 当前节点的id作为新节点的pid（父节点id）
            window.id=this.id;

            // 获取zTreeObj
            let zTreeObj = $.fn.zTree.getZTreeObj("tree");

            // 根据id获取节点
            let currentNode = zTreeObj.getNodeByParam("id",window.id);

            $("#removeNodeSpan").html("【<i class='"+currentNode.icon+"'></i>"+" "+currentNode.name+"】");

            // 打开模态框
            $("#menuConfirmModal").modal("show");
            return false;
        })
        // 删除节点的模态窗的确认按钮
        $("#confirmBtn").click(function (){
            $.ajax({
                url:"menu/remove/node.json",
                data:{
                    id:window.id,
                },
                type:"post",
                dataType:"json",
                success:function (response){
                    let result = response.result;
                    if (result==="SUCCESS"){
                        layer.msg("操作成功!")
                        // 刷新树形结构
                        generateTree();
                    }
                    else{
                        layer.msg("操作失败!"+response.message);
                    }
                },
                error:function (response){
                    layer.msg(response.status+" "+response.statusText)
                }
            });
            $("#menuConfirmModal").modal("hide");
        });
    });
</script>
<body>
<%@include file="/WEB-INF/include/nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include/sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="panel panel-default">
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <ul id="tree" class="ztree">

                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%--添加模态窗--%>
<%@include file="/WEB-INF/admin/model/menu/add.jsp"%>
<%--修改模态窗--%>
<%@include file="/WEB-INF/admin/model/menu/edit.jsp"%>
<%--删除模态窗--%>
<%@include file="/WEB-INF/admin/model/menu/confirm.jsp"%>
</body>
</html>
