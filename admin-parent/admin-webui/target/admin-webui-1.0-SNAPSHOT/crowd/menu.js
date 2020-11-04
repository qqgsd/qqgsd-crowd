/**
 * 生成树形结构
 */
function generateTree(){

    $.ajax({
        url:"menu/get/whole/tree.json",
        type:"post",
        dataType:"json",
        success:function (response){
            if (response.result==="SUCCESS"){
                // zTree的设置
                let setting = {
                    data:{
                        key:{
                            url:"notExist"
                        }
                    },
                    view:{
                        addDiyDom:addDiyDom,  // 更改树的图标
                        addHoverDom:addHoverDom,
                        removeHoverDom:removeHoverDom,
                    }
                };

                let zNodes=response.data;

                // 初始化树
                $.fn.zTree.init($("#tree"),setting,zNodes);

            }
            else{
                layer.msg(response.message);
            }
        }
    })
}

/**
 * 更改树的图标
 * @param treeId 树形结构附着的静态节点<ul id=“tree”>的id
 * @param treeNode 当前树节点
 */
function addDiyDom(treeId,treeNode){
    //console.log(treeNode)
    /*
        zTree的id生成规则
        ”<ul>的id“+“_”+"当前节点序号"+"_"+"功能"
        例：tree_7_ico
     */
    // 获取图标的id
    let spanId=treeNode.tId+"_ico";

    //更改图标
    let $spanId = $("#"+spanId);
    $spanId.removeClass().addClass(treeNode.icon);
}

/**
 * 鼠标移入事件（添加按钮组）
 * @param treeId
 * @param treeNode
 */
function addHoverDom(treeId,treeNode){
    /*
        按钮组的位置：节点超链接（id:tree_5_a）的后面
     */

    // 按钮标签
    let addBtn="<a id='"+treeNode.id+"' class=\"addBtn btn btn-info dropdown-toggle btn-xs\" " +
                "style=\"margin-left:10px;padding-top:0;\" href=\"#\" " +
                "title=\"添加节点\">&nbsp;&nbsp;" +

                    "<i class=\"fa fa-fw fa-plus rbg \"></i>" +
                "</a>";
    let editBtn="<a id='"+treeNode.id+"' class=\"editBtn btn btn-info dropdown-toggle btn-xs\" " +
                "style=\"margin-left:10px;padding-top:0;\" href=\"#\" " +
                "title=\"修改节点\">&nbsp;&nbsp;" +
                    "<i class=\"fa fa-fw fa-edit rbg \"></i>" +
                "</a>";
    let removeBtn="<a id='"+treeNode.id+"' class=\"removeBtn btn btn-info dropdown-toggle btn-xs\" " +
                   "style=\"margin-left:10px;padding-top:0;\" href=\"#\" " +
                   "title=\"删除节点\">&nbsp;&nbsp;" +
                        "<i class=\"fa fa-fw fa-times rbg \"></i>" +
                   "</a>";

    // 按钮组
    let btn="";

    //当前节点级别
    let level = treeNode.level;
    if (level===0){             //根节点
        btn+=addBtn;
    }
    else if(level===1){         //分支节点
        btn=addBtn+" "+editBtn;
        if (treeNode.children.length===0) {   //没有子节点
            btn+=" "+removeBtn;
        }
    }
    else{                       // 叶子节点
        btn=editBtn+" "+removeBtn;
    }

    // 超链接id
    let a = treeNode.tId+"_a";

    // 按钮组数量大于0，直接返回
    if($("#"+(treeNode.tId+"_btnGrp")).length>0)
        return ;

    //添加按钮组
    $("#"+a).after("<span id='"+(treeNode.tId+"_btnGrp")+"'>"+btn+"</span>");
}

/**
 * 鼠标移除事件（删除按钮组）
 * @param treeId
 * @param treeNode
 */
function removeHoverDom(treeId,treeNode){

    let a = treeNode.tId+"_btnGrp";
    $("#"+a).remove();
}