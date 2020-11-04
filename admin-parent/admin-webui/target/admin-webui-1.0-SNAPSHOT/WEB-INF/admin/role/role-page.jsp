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
<link rel="stylesheet" href="css/pagination.css">
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="crowd/role.js" ></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css">
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
    $(function (){
        // 1.为分页准备初始化数据
        window.pageNum=1;
        window.pageSize=5;
        window.keyword="";

        // 2.调用分页函数
        generatePage();

        // 给查询按钮绑定响应函数
        $("#searchBtn").click(function (){
            // 获取查询关键字
            window.keyword=$("#keywordInput").val()
            // 显示分页
            generatePage();
        })

        // 打开新增模态窗口
        $("#showAddModelBtn").click(function (){
            $("#addModel").modal("show")
        })

        // 保存按钮单击事件
        $("#saveRoleBtn").click(function (){
            // 获取输入的角色名称
            let roleName=$.trim($("#roleName").val());

            $.ajax({
                url:"role/save.json",
                type:"post",
                data:{
                    "name":roleName
                },
                dataType:"json",
                success:function (response){
                    let result = response.result;
                    if (result==="SUCCESS"){
                        layer.msg("操作成功！")
                    }
                    else if(result==="FAILED"){
                        layer.msg("操作失败！"+response.message)
                    }

                    // 重新加载分页
                    window.pageNum=999999;
                    generatePage();
                },
                error:function (response){
                    layer.msg(response.status+":"+response.statusText)
                }
            });

            // 关闭模态窗
            $("#addModel").modal("hide")

        });

        /*修改按钮单击事件 jQuery动态生成的按钮无法绑定
        $(".glyphicon-pencil").click(function (){
            alert("hhhhhh")
        })*/

        let $rolePageBody = $("#rolePageBody");
        // 打开修改模态窗口
        $rolePageBody.on("click",".glyphicon-pencil",function (){
            $("#editModel").modal("show")

            let roleName = $(this).parent().parent().prev().text();
            window.roleId = this.id;

            // 回显表单
            $("#editModel [name=roleName]").val(roleName)
        });

        // 更新按钮单击事件
        $("#updateRoleBtn").click(function (){
            // 从文本框中获取新的角色名称
            let roleName = $("#editModel [name=roleName]").val();
            $.ajax({
                url:"role/update.json",
                type:"post",
                data:{
                    "id":window.roleId,
                    "name":roleName
                },
                dataType:"json",
                success:function (response){
                    let result = response.result;
                    if (result==="SUCCESS"){
                        layer.msg("操作成功！")
                    }
                    else if(result==="FAILED"){
                        layer.msg("操作失败！"+response.message)
                    }

                    // 重新加载分页
                    generatePage();
                },
                error:function (response){
                    layer.msg(response.status+":"+response.statusText)
                }
            });

            // 关闭模态窗
            $("#editModel").modal("hide")
        })

        // 批量删除按钮单击事件
        $("#removeBtn").click(function (){
            let roleArray=[];
            $(".itemBox:checked").each(function (){
                let roleId=this.id;
                let roleName=$(this).parent().next().text();
                roleArray.push({roleId:roleId,roleName:roleName})
            })

            // 检查roleArray长度是否为0
            if (roleArray.length===0){
                layer.msg("请至少选择一个删除")

            }
            else
                showConfirmModal(roleArray)
        })

        // 确认按钮单击事件
        $("#removeRoleBtn").click(function (){
            $.ajax({
                url:"role/remove/by/role/id/array.json",
                data:JSON.stringify(window.roleIdArray),
                type:"post",
                contentType:"application/json;charset=UTF-8",
                dataType:"json",
                success:function (response){
                    let result = response.result;
                    if (result==="SUCCESS"){
                        layer.msg("操作成功！")
                    }
                    else if(result==="FAILED"){
                        layer.msg("操作失败！"+response.message)
                    }

                    // 重新加载分页
                    generatePage();
                },
                error:function (response){
                    layer.msg(response.status+":"+response.statusText)
                }
            });
            // 关闭模态窗
            $("#confirmModel").modal("hide")
        })

        // 单条删除
        $rolePageBody.on("click",".glyphicon-remove",function (){
            let roleName = $(this).parent().parent().prev().text();
            let roleArray=[{roleId:this.id,roleName:roleName}]
            showConfirmModal(roleArray)
        })

        // 全选按钮
        $("#sumBox").click(function (){

            // 全选按钮当前自身的状态
            let current=this.checked;
            $(".itemBox").prop("checked",current)
        })

        // 全选、全不选反向
        $rolePageBody.on("click",".itemBox",function (){
            // 已选中的.itemBox的数量
            let checkedCount=$(".itemBox:checked").length;
            // 全部.itemBox的数量
            let totalCount=$(".itemBox").length;
            $("#sumBox").prop("checked",checkedCount===totalCount)
        });

        // 分配权限按钮
        $rolePageBody.on("click",".checkBtn",function (){
            window.roleId=this.id
            $("#assignModel").modal("show")
            // 生成权限树
            fillAuthTree();
        });
        // 分配权限的保存按钮
        $("#assignSaveBtn").click(function (){
            // 收集已勾选的节点
            let zTreeObj = $.fn.zTree.getZTreeObj("treeAuth");
            let checkedNodes=zTreeObj.getCheckedNodes(true);
            // 获取已勾选节点的id
            let authIdArray=[];
            for (let i=0;i<checkedNodes.length;i++){
                authIdArray.push(checkedNodes[i].id)
            }
            let requestBody={
                authIdArray:authIdArray,
                roleId:[window.roleId],
            }
            requestBody = JSON.stringify(requestBody);
            $.ajax({
                url:"assign/do/roleAssign/auth.json",
                data:requestBody,
                type:"post",
                dataType:"json",
                contentType: "application/json;charset=utf-8",
                success:function (response){
                    let result = response.result;
                    if (result==="SUCCESS"){
                        layer.msg("操作成功！")
                    }
                    else if(result==="FAILED"){
                        layer.msg("操作失败！"+response.message)
                    }
                },
                error:function (response){
                    layer.msg(response.status+":"+response.statusText)
                }
            });
            $("#assignModel").modal("hide")
        })
    })
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
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                 <input id="keywordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" id="removeBtn" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" id="showAddModelBtn" class="btn btn-primary" style="float:right;">
                        <i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="sumBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><%--显示分页--%></div>
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

<%--添加模态窗口--%>
<%@include file="/WEB-INF/admin/model/role/model-role-add.jsp"%>
<%--修改模态窗口--%>
<%@include file="/WEB-INF/admin/model/role/model-role-edit.jsp"%>
<%--删除模态窗口--%>
<%@include file="/WEB-INF/admin/model/role/model-role-confirm.jsp"%>
<%@include file="/WEB-INF/admin/model/assign/role-assign-auth.jsp"%>
</body>
</html>
