<%--
  Created by IntelliJ IDEA.
  User: qqgsd
  Date: 2020/10/6
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>

<%--修改角色的模态窗口--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<div id="editModel" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">众筹网系统弹窗</h4>
            </div>
            <div class="modal-body">
                <form class="form-signin" role="form">
                    <div class="form-group has-success has-feedback">
                        <label>
                            <input type="text" name="roleName" class="form-control"
                                    placeholder="请输入角色名称" autofocus>
                        </label>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="updateRoleBtn" type="button" class="btn btn-success">修改</button>
            </div>
        </div>
    </div>
</div>
