var table;
var form;
layui.use('table', function () {
    let table = layui.table;

    loadHUserTable();
    loadCurrUser();
    //监听toolbar 筛选，前端lay-filter判断(user)是哪个tool，前台的lay-event可以拿来判断操作是什么
    table.on('tool(hUser)', function(obj) {
        let data = obj.data;

        if (obj.event === 'del') {
            if ("admin" == data.username)
                return;
            layer.confirm("确认删除用户" + obj.data.username, {btn: ['确定', '取消'], title: "提示"}, function () {
                let userIdList = data.userId;
                $.ajax({
                    url: '',
                    type: 'post',
                    data: {
                        userIdList: userIdList
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.code == 10) {
                            loadHUserTable();
                        }
                        layer.alert(result.msg);
                    },
                    failure: function () {
                        layer.alert('操作超时!');
                    }
                });
            });
        } else if (obj.event === 'edit') {
            $("#hUserEdit input[name ='username']").val(data.username);
            $("#hUserEdit input[name ='password']").val(data.password);
            $("#hUserEdit input[name ='password']").val(data.password);
            $("#hUserEdit input[name ='nickname']").val(data.nickname);
            $("#hUserEdit select[name ='sex']").val(data.sex);
            $("#hUserEdit input[name ='email']").val(data.email);
            $("#hUserEdit input[name ='tel']").val(data.tel);
            $("#hUserEdit input[name ='isUse']").val(data.isUse);
            $("#hUserEdit input[name ='userId']").val(data.userId);
            $("#hUserEdit").modal();
        } else if (obj.event === 'files') {
            //TODO 一人一档
            let fileData = getHUserFile(data.userId);
            $("#hUserFileContent").val(fileData);
            $("#hUserFile").modal();
        }
    });
});

function getHUserFile(userId) {
    let fileData = new Object;
    $.ajax({
        url: '/graduation/getHUserFile',
        type: 'POST',
        data: JSON.stringify(userId),
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 10) {
                fileData = result.data;
            }
        }
    });
    return fileData;
}

function loadHUserTable(param){
    layui.use('table', function () {
        table = layui.table;
        //用户列表渲染
        table.render({
            elem: '#tableHUser',
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
                {fixed: 'right', width: 180, align: 'center', toolbar: '#hUserOption'},
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

/**
 * 表单监听
 */
layui.use('form', function(){
    form = layui.form;
/*
    form.on('submit(userAdd)', function(data){
        // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
        // alert(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        $.ajax({
            url: '/user/register'/!*+ '?roleIdList='+ data.field.roleIdList.join(',')*!/,
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
    });*/
    form.on('submit(queryHUser)', function (data) {
        var jdata = {
            "grantUser": data.field
        }
        loadUserTable(jdata);
    });
});

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
function openImportHUsersFrame(){
    layer.open({
        title: '数据导入',
        area: ['400px', '300px'],
        //skin: 'layui-layer-lan',
        type: 2,
        content: ['/user/import', 'no'] //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
    });
}