var form;
var table;
    layui.use('table', function () {
        var table = layui.table;

        loadUserTable();
        loadRoleTable();
        loadPermissionTable();
        loadCurrUser();
        //监听toolbar 筛选，前端lay-filter判断(user)是哪个tool，前台的lay-event可以拿来判断操作是什么
        table.on('tool(user)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                if("admin" == data.username)
                    return;
                layer.confirm("确认删除用户"+obj.data.username, {btn: ['确定', '取消'],title:"提示"},function () {
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
                                loadUserTable();
                            }
                                layer.alert(result.msg);
                        },
                        failure : function() {
                            layer.alert('操作超时!');
                        }
                    });
                });
            }else if(obj.event === 'edit'){
                $("#userEdit input[name ='username']").val(data.username);
                $("#userEdit input[name ='password']").val(data.password);
                $("#userEdit input[name ='password']").val(data.password);
                $("#userEdit input[name ='nickname']").val(data.nickname);
                $("#userEdit select[name ='sex']").val(data.sex);
                $("#userEdit input[name ='email']").val(data.email);
                $("#userEdit input[name ='tel']").val(data.tel);
                $("#userEdit input[name ='isUse']").val(data.isUse);
                $("#userEdit input[name ='userId']").val(data.userId);
                $("#userEdit").modal();
            }else if(obj.event === 'grant'){
                if("admin" == data.username)
                    return;
                loadRoleList(data.userId);
                form.val("grant", {
                    "username": data.username // "name": "value"
                    ,"nickname": data.nickname
                    ,"email": data.email
                    ,"tel": data.tel
                    ,"isUse": data.isUse
                    ,"userId": data.userId
                });
                $("#grant").modal();
                //JQ写法
                /*$("#grant input[name ='username']").val(data.username);
                $("#grant input[name ='nickname']").val(data.nickname);
                $("#grant input[name ='email']").val(data.email);
                $("#grant input[name ='tel']").val(data.tel);
                $("#grant input[name ='isUse']").val(data.isUse);
                $("#grant input[name ='userId']").val(data.userId);
                $("#grant").modal();*/
            }
        });
        table.on('tool(role)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                if('admin' == data.roleName)
                    return;
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
                                loadRoleTable();
                            }
                                layer.alert(result.msg);
                        },
                        failure : function() {
                            layer.alert('操作超时!');
                        }
                    });
                });
            }else if(obj.event === 'edit'){
                if('admin' == data.roleName)
                    return;
                $("#roleEdit input[name ='roleName']").val(data.roleName);
                $("#roleEdit input[name ='roleRemark']").val(data.roleRemark);
                $("#roleEdit input[name ='roleId']").val(data.roleId);
                $("#roleEdit").modal();
            }else if(obj.event === 'grant'){
                loadPermissionList(data.roleId);
                form.val("grantRolePermission", {
                    "roleId": data.roleId
                    ,"roleName": data.roleName
                    ,"roleRemark": data.roleRemark
                });
                $("#grantRolePermission").modal();
            }
        });
        table.on('tool(permission)', function(obj){
            var data = obj.data;
            if('系统管理' == data.permissionName)
                return;
            if(obj.event === 'del'){
                layer.confirm("确认移除权限"+obj.data.permissionName, {btn: ['确定', '取消'],title:"提示"},function () {
                    var permissionIdList = data.permissionId;
                    $.ajax({
                        url: '/user/removePermission',
                        type: 'post',
                        data: {
                            permissionIdList: permissionIdList
                        },
                        dataType: 'json',
                        success: function (result) {
                            if(result.code == 10){
                                loadPermissionTable();
                            }
                                layer.alert(result.msg);
                        },
                        failure : function() {
                            layer.alert('操作超时!');
                        }
                    });
                });
            }else if(obj.event === 'edit'){
                $("#permissionEdit input[name ='permissionName']").val(data.permissionName);
                $("#permissionEdit input[name ='permissionId']").val(data.permissionId);
                $("#permissionEdit").modal();
            }
        });

    });

    function loadUserTable(param){
        layui.use('table', function () {
            table = layui.table;
            //用户列表渲染
            table.render({
                elem: '#tableUser',
                url: '/user/queryUser',
                where: param,
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
                    {fixed: 'right', width: 180, align: 'center', toolbar: '#userOption'},
                ]],
                parseData: function (res) {
                    return {
                        "code": res.code,
                        "msg": res.msg,
                        "data": res.data.list,
                        "count": res.data.total,
                    };
                },
                request: {
                    pageName: 'pageNum', //页码的参数名称，默认：page
                    limitName: 'pageSize' //每页数据量的参数名，默认：limit
                },
                response: {
                    statusCode: 10
                }
            });
        });
    }
    function loadRoleTable(param) {
        layui.use('table', function () {
            table = layui.table;
            //角色列表渲染
            table.render({
                elem: '#tableRole',
                url: '/user/queryRole',
                where: param,
                method: 'post',
                contentType: 'application/json',
                page: true,
                cols: [[
                    {field: 'roleId', title: 'ID', fixed: 'left', sort: true},
                    {field: 'roleName', title: '角色名'},
                    {field: 'roleRemark', title: '角色描述'},
                    {fixed: 'right', width: 180, align: 'center', toolbar: '#roleOption'},
                ]],
                parseData: function (res) {
                    return {
                        "code": res.code,
                        "msg": res.msg,
                        "data": res.data.list,
                        "count": res.data.total,
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
        });
    }
    function loadPermissionTable(param){
        layui.use('table', function(){
            table = layui.table;
            //权限列表渲染
            table.render({
                elem: '#tablePermission',
                url: '/user/queryPermission',
                where: param,
                method: 'post',
                contentType: 'application/json',
                page: true,
                cols: [[
                    {field: 'permissionId', title: 'ID', fixed: 'left', sort: true},
                    {field: 'permissionName', title: '权限名'},
                    {fixed: 'right', width: 180, align: 'center', toolbar: '#permissionOption'},
                ]],
                parseData: function (res) {
                    return {
                        "code": res.code,
                        "msg": res.msg,
                        "data": res.data.list,
                        "count": res.data.total,
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
        })
    }
    function loadRoleList(currUserId){
        let op = $("#roleList");
        let hadRoleId = new Set;
        //首次ajax获取当前用户已拥有的角色
        $.ajax({
            url: '/user/queryRole',
            type: 'POST',
            async: false,
            data: JSON.stringify({
                grantUser:{
                    userId: currUserId
                },
                //需要带上分页参数
                pageNum: 1,
                pageSize: 100
            }),
            contentType: "application/json",
            success: function (result) {
                if(result.code == 10){
                    hadRoleId = new Set;
                    let items = result.data.list;
                    $.each(items, function (index, data) {
                        hadRoleId.add(data.roleId);
                    });
                }
            }
        });
        //第二次ajax渲染
        $.ajax({
            url: '/user/queryRole',
            type: 'POST',
            async: false,
            data: JSON.stringify({
                pageNum: 1,
                pageSize: 100
            }),
            contentType: "application/json",
            success: function (result) {
                if(result.code == 10){
                    let items = result.data.list;
                    op.html('');
                    $.each(items, function (index, data) {
                        let temp;
                        if(hadRoleId.has(data.roleId)){
                            temp = '<input class="layui-input" checked="checked" lay-filter="roleItem" type="checkbox" name="roleItem" id="roleItem" title="'+ data.roleName+ '" value="'+ data.roleId+ '"/>';
                        }else {
                            temp = '<input class="layui-input" lay-filter="roleItem" type="checkbox" name="roleItem" id="roleItem" title="' + data.roleName + '" value="' + data.roleId + '"/>';
                        }
                        op.append(temp);
                    })
                    //重新渲染某元素
                    form.render('checkbox');
                }
            },
            failure : function() {
                layer.alert('操作超时!');
            }
        });
    }
    function loadPermissionList(currRoleId){
        let op = $("#permissionList");
        let hadPermissionId = new Set;
        //首次ajax获取当前用户已拥有的角色
        $.ajax({
            url: '/user/queryPermission',
            type: 'POST',
            async: false,
            data: JSON.stringify({
                grantRole:{
                    roleId: currRoleId
                },
                //需要带上分页参数
                pageNum: 1,
                pageSize: 100
            }),
            contentType: "application/json",
            success: function (result) {
                if(result.code == 10){
                    hadPermissionId = new Set;
                    let items = result.data.list;
                    $.each(items, function (index, data) {
                        hadPermissionId.add(data.permissionId);
                    });
                }
            }
        });
        //第二次ajax渲染
        $.ajax({
            url: '/user/queryPermission',
            type: 'POST',
            async: false,
            data: JSON.stringify({
                //需要带上分页参数
                pageNum: 1,
                pageSize: 100
            }),
            contentType: "application/json",
            success: function (result) {
                if(result.code == 10){
                    let items = result.data.list;
                    op.html('');
                    $.each(items, function (index, data) {
                        let temp;
                        if(hadPermissionId.has(data.permissionId)){
                            temp = '<input class="layui-input" checked="checked" lay-filter="permissionItem" type="checkbox" name="permissionItem" title="'+ data.permissionName+ '" value="'+ data.permissionId+ '"/>';
                        }else {
                            temp = '<input class="layui-input" lay-filter="permissionItem" type="checkbox" name="permissionItem" title="' + data.permissionName + '" value="' + data.permissionId + '"/>';
                        }
                        op.append(temp);
                    })
                    //重新渲染某元素
                    form.render('checkbox');
                }
            },
            failure : function() {
                layer.alert('操作超时!');
            }
        });
    }

    /**
     * 表单监听
     */
    layui.use('form', function(){
        form = layui.form;

        form.on('submit(userAdd)', function(data){
            // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
            // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
            // alert(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                url: '/user/register'/*+ '?roleIdList='+ data.field.roleIdList.join(',')*/,
                type: 'POST',
                data: JSON.stringify(data.field),
                contentType: "application/json",
                success: function (result) {
                    if(result.code == 10){
                        loadUserTable();
                    }
                    layer.alert(result.msg);
                },
                failure : function() {
                    layer.alert('操作超时!');
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        form.on('submit(roleAdd)', function(data){
            $.ajax({
                url: '/user/addRole',
                type: 'post',
                data: JSON.stringify(data.field),
                contentType: "application/json",
                success: function (result) {
                    if(result.code == 10){
                        loadRoleTable();
                    }
                    layer.alert(result.msg);
                },
                failure : function() {
                    layer.alert('操作超时!');
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        form.on('submit(permissionAdd)', function(data){
            $.ajax({
                url: '/user/addPermission',
                type: 'post',
                data: JSON.stringify(data.field),
                contentType: "application/json",
                success: function (result) {
                    if(result.code == 10){
                        loadPermissionTable();
                    }
                    layer.msg(result.msg);
                },
                failure: function() {
                    layer.alert('操作超时!');
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });

        form.on('submit(userEdit)', function(data){
            $.ajax({
                url: '/user/editUser',
                type: 'POST',
                data: JSON.stringify(data.field),
                contentType: "application/json",
                success: function (result) {
                    if(result.code == 10){
                        loadUserTable();
                    }
                    layer.msg(result.msg);
                }
            });
            return false;
        });
        form.on('submit(roleEdit)', function (data) {
            $.ajax({
                url: '/user/editRole',
                type: 'POST',
                data: JSON.stringify(data.field),
                contentType: "application/json",
                success: function (result) {
                    if(result.code == 10){
                        loadRoleTable();
                    }
                    layer.msg(result.msg);
                }
            });
            return false;
        });
        form.on('submit(permissionEdit)', function () {
            //预留
        });

        form.on('submit(queryUser)', function (data) {
            var jdata = {
                "grantUser": data.field
            }
            loadUserTable(jdata);
        });
        form.on('submit(queryRole)', function (data) {
            var jdata = {
                "grantRole": data.field
            }
            loadRoleTable(jdata);
        });
        form.on('submit(queryPermission)', function (data) {
            var jdata = {
                "grantPermission": data.field
            };
            loadPermissionTable(jdata);
        });

        form.on('submit(bindUserRole)', function (data) {
            let roleItemChecked = new Set;
                $.each($("input:checkbox:checked"), function () {
                roleItemChecked.add(Number(this.value));
            });
            let roleUserId = Array.from(roleItemChecked);
            if(roleUserId.length == 0){
                layer.msg("至少需要一个角色!");
                return false;
            }
            $.ajax({
                url: '/user/bindUserRoles'+ '?userId='+ data.field.userId,
                type: 'POST',
                data: JSON.stringify(roleUserId),
                contentType: 'application/json',
                success: function (result) {
                    if(result.code == 10){
                        loadUserTable();
                    }
                    layer.msg(result.msg);
                }
            });
            return false;
        });
        form.on('submit(bindRolePermission)', function (data) {
            let permissionItemChecked = new Set;
                $.each($("input:checkbox:checked"), function () {
                    permissionItemChecked.add(Number(this.value));
            });
            let permissionRoleId = Array.from(permissionItemChecked);
            if(permissionRoleId.length == 0){
                layer.msg("至少需要一个权限!");
                return false;
            }
            $.ajax({
                url: '/user/bindRolePermissions'+ '?roleId='+ data.field.roleId,
                type: 'POST',
                data: JSON.stringify(permissionRoleId),
                contentType: 'application/json',
                success: function (result) {
                    if(result.code == 10){
                        loadPermissionTable();
                    }
                    layer.msg(result.msg);
                }
            });
            return false;
        });
    });

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

    /**
     * 当前用户名
     */
    function loadCurrUser() {
        $.ajax({
            url: '/user/currUser',
            type: 'POST',
            success: function (result) {
                if (result.code == 10) {
                    let user = result.data;
                    $("#currUsername").html("用户名: "+ user.username);
                }
            }
        });
    }

    /**
     * 打开批量导入用户列表页面
     */
    function openImportUsersFrame(){
        layer.open({
            title: '数据导入',
            area: ['800px', '600px'],
            //skin: 'layui-layer-lan',
            type: 2,
            content: ['/user/import', 'no'] //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
        });
    }