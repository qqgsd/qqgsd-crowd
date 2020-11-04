<%--
  Created by IntelliJ IDEA.
  User: qqgsd
  Date: 2020/10/6
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>

<%--修改角色的模态窗口--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<div id="confirmModel" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">众筹网系统弹窗</h4>
            </div>
            <div class="modal-body">
                <h4>请确认是否要删除下列角色</h4>
                <div id="roleNameDiv" style="text-align: center"></div>
            </div>
            <div class="modal-footer">
                <button id="removeRoleBtn" type="button" class="btn btn-success">确认删除</button>
            </div>
        </div>
    </div>
</div>
