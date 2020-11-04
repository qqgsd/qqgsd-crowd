/*
*  角色的js
* */

// 显示删除角色时的确认模态窗
function showConfirmModal(roleArray){
    $("#confirmModel").modal("show")

    let $roleNameDiv=$("#roleNameDiv")
    // 清空以前的数据
    $roleNameDiv.empty()

    // 全局数组存放角色id
    window.roleIdArray=[]

    // 显示要删除的角色名称
    for (let i=0;i<roleArray.length;i++){
        let role=roleArray[i];
        let roleName = role.roleName;
        $roleNameDiv.append(roleName+"<br/>")

        window.roleIdArray.push(role.roleId)
    }
}

/*生成分页页面*/
function generatePage() {

    // 1.获取分页数据
    let pageInfo=getPageInfoRemote();

    // 2.填充表格
    fillTableBody(pageInfo)

    // 每次分页时全选按钮为false
    $("#sumBox").prop("checked",false);
}

/*获取pageInfo数据*/
function getPageInfoRemote(){
    let ajaxResult=$.ajax({
        url:"role/get/page/info.json",
        type:"post",
        data:{
            pageNum:window.pageNum,
            pageSize:window.pageSize,
            keyword:window.keyword
        },
        async:false,
        dataType:"json"
    })
    //console.log(ajaxResult)
    // 响应状态码
    let statusCode=ajaxResult.status

    // 响应失败
    if (statusCode!==200){
        layer.msg("failed! statusCode="+statusCode+" message="+ajaxResult.statusText)
        return null;
    }

    // 获取resultEntity
    let resultEntity = ajaxResult.responseJSON;
    let result = resultEntity.result;
    if (result==="FAILED"){
        layer.msg(resultEntity.message)
    }

    // 返回pageInfo
    return resultEntity.data;
}

/*填充表格*/
function fillTableBody(pageInfo){

    // 清除tbody中的旧数据
    let $rolePageBody = $("#rolePageBody");
    $rolePageBody.empty();
    $("#Pagination").empty();

    // 判断pageInfo是否有效
    if (pageInfo===null||pageInfo===undefined||pageInfo.list===null||pageInfo.list.length===0){
        $rolePageBody.append("<tr><td colspan='4' align='center'>抱歉！没有查询到您要的数据!</td></tr>")
        return ;
    }
    // 填充tbody
    for (let i=0;i<pageInfo.list.length;i++){
        let role=pageInfo.list[i];
        let roleId=role.id;
        let roleName=role.name;

        let numberTd="<td>"+(i+1)+"</td>";
        let checkboxTd="<td><input id='"+roleId+"' class='itemBox' type='checkbox'></td>";
        let roleNameTd="<td>"+roleName+"</td>"
        let checkBtn="<button id='"+roleId+"' type='button' class='checkBtn btn btn-success btn-xs'><i class=' glyphicon glyphicon-check'></i></button>";
        let pencilBtn="<button type=\"button\" class=\"btn btn-primary btn-xs\"><i id='"+roleId+"' class=\" glyphicon glyphicon-pencil\"></i></button>";
        let removeBtn="<button type=\"button\" class=\"btn btn-danger btn-xs\"><i id='"+roleId+"' class=\" glyphicon glyphicon-remove\"></i></button>";
        let buttonTd="<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>"
        let tr="<tr>"+numberTd+checkboxTd+roleNameTd+buttonTd+"</tr>"

        $rolePageBody.append(tr);

        // 生成页码导航条
        generateNavigator(pageInfo)
    }
}

/*生成页码导航条*/
function generateNavigator(pageInfo) {
    // 总记录数
    let total=pageInfo.total;

    //声明json对象存储Pagination要设置的属性
    let properties={
        num_edge_entries: 3,                                // 边缘页
        num_display_entries: 5,                             // 主体页
        callback: paginationCallBack,                       // 翻页按钮
        items_per_page: pageInfo.pageSize,                  // 每页显示的数量
        current_page: pageInfo.pageNum-1,                   // 当前页
        prev_text: "上一页",
        next_text: "下一页"
    };

    //生成页码导航条
    $("#Pagination").pagination(total,properties);
}

/*翻页时的回调函数*/
function paginationCallBack(pageIndex,jQuery){

    // pageNum
    window.pageNum=pageIndex+1;

    generatePage();

    //取消超链接默认行为
    return false;
}

// 生成权限树
function fillAuthTree(){
    $.ajax({
        url:"assign/get/all/auth.json",
        type:"post",
        dataType: "json",
        success:function (response){
            let result = response.result;
            if (result==="SUCCESS"){
                // 树的节点
                let authList = response.data;
                let setting={
                    data:{
                        simpleData: {
                            enable:true,
                            pIdKey:"categoryId",
                        },
                        key:{
                            name:"title"
                        },
                    },
                    check:{
                        enable: true,
                    }
                };

                // 生成树形结构
                $.fn.zTree.init($("#treeAuth"),setting,authList);
                // 展开节点
                let zTreeObj = $.fn.zTree.getZTreeObj("treeAuth");
                zTreeObj.expandAll(true);
                // 已选择的权限回显
                getAssignedAuthIdByRoleId(zTreeObj)
            }
            else if(result==="FAILED"){
                layer.msg("操作失败！"+response.message)
            }
        },
        error:function (response){
            layer.msg(response.status+" "+response.statusText)
        },
    });
}

// 获取角色已分配的权限
function getAssignedAuthIdByRoleId(zTreeObj){
    $.ajax({
        url:"assign/getAssigned/authId/byRoleId.json",
        data:{
            roleId:window.roleId
        },
        type:"post",
        dataType:"json",
        //async:false,
        success:function (response){
            let result = response.result;
            if (result==="SUCCESS"){
                let authIdArray = response.data;
                for (let i=0;i<authIdArray.length;i++){
                    let authId=authIdArray[i];
                    // 根据id查对应的node
                    let treeNod = zTreeObj.getNodeByParam("id",authId);
                    // 将treeNode设置勾选
                    let checked=true;  // 勾选
                    let checkTypeFlag=false;  // 不联动
                    zTreeObj.checkNode(treeNod,checked,checkTypeFlag);
                }
            }
            else if(result==="FAILED"){
                layer.msg("操作失败！"+response.message)
            }
        },
        error:function (response){
            layer.msg(response.status+" "+response.statusText)
        },
    })
}

