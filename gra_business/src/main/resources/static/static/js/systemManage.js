    var table;
    layui.use('table', function () {
        table = layui.table;

        //用户列表渲染
        table.render({
            elem: '#tableUser',
            url: '/user/queryUser',
            method: 'post',
            contentType: 'application/json',
            page: true,
            cols: [[
                {field: 'userId', title: 'ID', fixed: 'left', sort: true},
                {field: 'username', title: '用户账号'},
                {field: 'nickname', title: '用户姓名'},
                {field: 'sex', title: '性别'},
                {field: 'email', title: '邮箱地址'},
                {field: 'tel', title: '电话号码'},
                {field: 'isUse', title: '可用'},
                    {fixed: 'right', width: 150, align: 'center', toolbar: '#userOption'},
            ]],
            parseData: function (res) {
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "data": res.data,
                    "count": res.data.size,
                };
            },
            request:{
                pageName: 'pageNum', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            },
            response:{
                statusCode: 10
            }
        });
        //角色列表渲染
        table.render({
            elem: '#tableRole',
            url: '/user/queryRole',
            method: 'post',
            contentType: 'application/json',
            page: true,
            cols: [[
                {field: 'roleId', title: 'ID', fixed: 'left', sort: true},
                {field: 'roleName', title: '角色名'},
                {field: 'roleRemark', title: '角色描述'},
                {fixed: 'right', width: 150, align: 'center', toolbar: '#userOption'},
            ]],
            parseData: function (res) {
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "data": res.data,
                    "count": res.data.size,
                };
            },
            request:{
                pageName: 'pageNum', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            },
            response:{
                statusCode: 10
            }
        });
        //权限列表渲染
        table.render({
            elem: '#tablePermission',
            url: '/user/queryPermission',
            method: 'post',
            contentType: 'application/json',
            page: true,
            cols: [[
                {field: 'permissionId', title: 'ID', fixed: 'left', sort: true},
                {field: 'permissionName', title: '权限名'},
                {fixed: 'right', width: 150, align: 'center', toolbar: '#userOption'},
            ]],
            parseData: function (res) {
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "data": res.data,
                    "count": res.data.size,
                };
            },
            request:{
                pageName: 'pageNum', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            },
            response:{
                statusCode: 10
            }
        });
        //系统日志列表渲染
        table.render({
            elem: '#tableDataLog',
            // url: '/allSysLogByPage', //数据接口
            page: true, //开启分页
            cols: [[ //表头
                {field:'zizeng', title: 'NO',fixed: 'left',templet:'#zizeng'},
                {field: 'gmtCreate', title: '日期',templet:"<div>{{layui.util.toDateString(d.gmtCreate, 'yyyy-MM-dd HH:mm:ss')}}</div>"},
               {field: 'userName', title: '用户名'},
                {field: 'operation', title: '操作'},
            ]],
            parseData: function (res) { //res 即为原始返回的数据
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": res.data.total, //解析数据长度
                    "data": res.data.list, //解析数据列表
                };
            },
            request:{
                pageName: 'pageNum', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            },
        });

        //监听toolbar 筛选
        table.on('tool(user)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                layer.confirm("确认删除用户"+obj.data.username, {btn: ['确定', '取消'],title:"提示"},function () {
                    if("admin" == data.username)
                        return;
                    var userIdList = data.userId;
                    $.ajax({
                        url: '/user/removeUser',
                        type: 'post',
                        data: {
                            userIdList: userIdList
                        },
                        dataType: 'json',
                        success: function (result) {
                            if(result.code == 10){
                                window.location.reload()
                            }else {
                                layer.alert(result.msg);
                            }
                        },
                        failure : function() {
                            layer.alert('操作超时!');
                        }
                    });
                });
            }else if(obj.event === 'edit'){
                if('admin'!=data.username) {
                    $("#userEdit input[name ='loginname']").val(data.username);
                    $("#userEdit input[name ='pwd']").val(data.password);
                    $("#userEdit input[name ='rePwd']").val(data.password);
                    $("#userEdit input[name ='realname']").val(data.nickname);
                    $("#userEdit input[name ='realname']").val(data.sex);
                    $("#userEdit input[name ='dsc']").val(data.email);
                    $("#userEdit input[name ='tel']").val(data.tel);
                    $("#userEdit input[name ='dsc']").val(data.isUse);
                    $("#userEdit input[name ='id']").val(data.userId);
                    $("#userEdit").modal();
                }else{
                    layer.alert('系统用户默认不能编辑！')
                }
            }
        });
        table.on('tool(role)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                layer.confirm("确认删除角色"+obj.data.roleName, {btn: ['确定', '取消'],title:"提示"},function () {
                    var roleIdList = data.roleId;
                    $.ajax({
                        url: '/user/removeRole',
                        type: 'post',
                        data: {
                            roleIdList: roleIdList
                        },
                        dataType: 'json',
                        success: function (result) {
                            if(result.code == 10){
                                window.location.reload()
                            }else {
                                layer.alert(result.msg);
                            }
                        },
                        failure : function() {
                            layer.alert('操作超时!');
                        }
                    });
                });
            }else if(obj.event === 'edit'){
                if('admin'!=data.roleName) {
                    $("#userEdit input[name ='loginname']").val(data.roleName);
                    $("#userEdit input[name ='pwd']").val(data.roleRemark);
                    $("#userEdit input[name ='id']").val(data.roleId);
                    $("#userEdit").modal();
                }else{
                    layer.alert('系统默认最高权限角色不能编辑！')
                }
            }
        });
        table.on('tool(permission)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                layer.confirm("确认移除权限"+obj.data.permissionName, {btn: ['确定', '取消'],title:"提示"},function () {
                    var permissionIdList = data.userId;
                    $.ajax({
                        url: '/user/removeUser',
                        type: 'post',
                        data: {
                            permissionIdList: permissionIdList
                        },
                        dataType: 'json',
                        success: function (result) {
                            if(result.code == 10){
                                window.location.reload()
                            }else {
                                layer.alert(result.msg);
                            }
                        },
                        failure : function() {
                            layer.alert('操作超时!');
                        }
                    });
                });
            }else if(obj.event === 'edit'){
                if('系统管理'!=data.permissionName) {
                    $("#userEdit input[name ='loginname']").val(data.permissionName);
                    $("#userEdit input[name ='id']").val(data.permissionId);
                    $("#userEdit").modal();
                }else{
                    layer.alert('系统默认最高权限不能移除！')
                }
            }
        });

    });

    /**
     * 提交用户
     *  新增或者编辑
      * @param tag
     */
    function submitUser(tag) {
    var obj = {};

    if (tag == 1) { //新增
        obj.loginname = $("#userAdd input[name ='loginname']").val();
        obj.pwd = $("#userAdd input[name ='pwd']").val();
        obj.realname = $("#userAdd input[name ='realname']").val();
        obj.tel = $("#userAdd input[name ='tel']").val();
        obj.dsc = $("#userAdd input[name ='dsc']").val();
        if (obj.pwd != $("#userAdd input[name ='rePwd']").val()) {
            layer.alert('密码不一致！');
            return
        }
    } else { //编辑
        obj.loginname = $("#userEdit input[name ='loginname']").val();
        obj.pwd = $("#userEdit input[name ='pwd']").val();
        obj.realname = $("#userEdit input[name ='realname']").val();
        obj.tel = $("#userEdit input[name ='tel']").val();
        obj.dsc = $("#userEdit input[name ='dsc']").val();
        obj.id = $("#userEdit input[name ='id']").val();
        if (obj.pwd != $("#userEdit input[name ='rePwd']").val()) {
            layer.alert('密码不一致！');
            return
        }
    }

    //正则校验，检测特殊字符
    if(obj.loginname.match(/[^a-zA-Z0-9\_\u4e00-\u9fa5]/g,'') || obj.pwd.match(/[^a-zA-Z0-9\_\u4e00-\u9fa5]/g,'')){
        layer.alert("账户名密码不能含有特殊字符")
        return;
    }

    if (obj.loginname == "" || obj.pwd == "" || obj.realname == "") {
        layer.alert('带*为必填项！');
        return
    }

    if (obj.pwd.length < 6) {
        layer.alert("允许密码最小长度6位！")
        return
    }

    $.ajax({
        url: '/user/save',
        type: 'post',
        data:obj,
        dataType: 'json',
        success: function (data) {
            if(data.code == 0){
                $("#userAdd").modal("hide");
                layer.msg(data.msg);
                window.location.reload()
            }else {
                layer.msg(data.msg);
            }

        },
        failure : function() {
            layer.alert('操作超时!');
        }
    });

}

    /**
     * 清除 用户列表
     */
    function  clearUser() {
   $("#userAdd input[name ='loginname']").val("");
   $("#userAdd input[name ='pwd']").val("");
   $("#userAdd input[name ='realname']").val("");
   $("#userAdd input[name ='tel']").val("");
   $("#userAdd input[name ='dsc']").val("");
   $("#userAdd input[name ='rePwd']").val("")
}

    /**
     * 登出
     */
    function logOut() {
    $.ajax({
        url: '/user/logout',
        type: 'get',
        data: {},
        dataType: 'json',
        success: function (res) {
            if (res.code == 10) {
                alert('注销成功');
                window.location.href = "/login";
            } else {
                alert("注销失败");
            }
        }
    });
}