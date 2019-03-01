var table;
var form;
var element;
/**
 * 进度条
 * 注意进度条依赖 element 模块，否则无法进行正常渲染和功能性操作
 */
layui.use('element', function(){
    element = layui.element;
});

layui.use('table', function () {
    let table = layui.table;

    loadHUserTable();
    loadTaskManageTable();
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
            let fileData = getHUserFile(data.userId);
            if(null === fileData) {
                layer.confirm(
                    obj.data.username + "-没有挂载档案，是否挂载？",
                    {
                        btn: ['确定', '取消'],
                        title: "提示"
                    },
                    function () {
                        layer.close(layer.index);
                        let userId = data.userId;
                        //弹出挂载页面
                        form.val("hUserAddForm", {
                            "userId": userId
                        });
                        $("#hUserAdd").modal();
                    });
            }else {
                form.val("hUserFileForm", {
                    "idCard": fileData.idCard
                    , "name": fileData.name
                    , "sex": fileData.sex == true ? '男' : '女'
                    , "telephone": fileData.telephone
                    , "email": fileData.email
                    , "huserId": fileData.huserId
                    , "userId": fileData.userId
                });
                $("#hUserFile").modal();
            }
        }
    });
    table.on('tool(task)', function (obj) {
        let data = obj.data;
        if(obj.event === 'processBB'){
            console.log(data)
            element.progress('taskProcessBar', data.process+ '%');
        }else if(obj.event === 'restart'){
            restartTask(data.taskId);
        }else if(obj.event === 'look'){
            form.val("taskDetailForm", {
                "type": data.type
                , "describe": data.describe
                , "param": data.param
                , "taskId": data.taskId
            });
            $("#taskDetail").modal();
        }else if(obj.event === 'stop'){
            stopTask(data.taskId);
        }else if(obj.event === 'remove'){
            layer.confirm("确认移除任务" + obj.data.serialNumber, {btn: ['确定', '取消'], title: "提示"}, function () {
                removeTask(data.taskId);
            });
        }
    });
});

function restartTask(taskId){
    let taskIdSet = new Set();
    taskIdSet.add(taskId);
    let taskIdList = Array.from(taskIdSet)
    $.ajax({
        url: '/task/restartTask',
        type: 'POST',
        data: JSON.stringify(Array.from(taskIdList)),
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 10) {
                loadTaskManageTable();
            }
        }
    });
}
function stopTask(taskId){
    let taskIdSet = new Set();
    taskIdSet.add(taskId);
    let taskIdList = Array.from(taskIdSet)
    $.ajax({
        url: '/task/stopTask',
        type: 'POST',
        data: JSON.stringify(Array.from(taskIdList)),
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 10) {
                loadTaskManageTable();
            }
        }
    });
}
function removeTask(taskId){
    let taskIdSet = new Set();
    taskIdSet.add(taskId);
    let taskIdList = Array.from(taskIdSet)
    $.ajax({
        url: '/task/remove',
        type: 'POST',
        data: JSON.stringify(Array.from(taskIdList)),
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 10) {
                loadTaskManageTable();
            }
                layer.alert(result.msg);
            return result;
        }
    });
}

/**
 * 刷新挂载档案页面，置空
 */
function refreshHUserAddForm(){
    form.val("hUserAddForm", {
        "userId": null
    });
}

/**
 * 取到用户档案
 * @param hUserId
 * @returns {*}
 */
function getHUserFile(hUserId) {
    let fileData = null;
    $.ajax({
        url: '/huser/getHUserFile' + '?hUserId='+ hUserId,
        type: 'POST',
        async: false,
        contentType: 'application/json',
        success: function (result) {
            if (result.code == 10) {
                fileData = result.data;
            }
        }
    });
    return fileData;
}

/**
 * 刷新数据表格
 * @param param
 */
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

function refreshProcessBar(currProcess) {
    //刷新进度条进度
    element.progress('taskProcessBar', currProcess+ '%');
}

window.setInterval(loadTaskManageTable, 3000);
/**
 * 刷新任务管理数据表格
 * @param param
 */
function loadTaskManageTable(param) {
    layui.use('table', function () {
        table = layui.table;
        //任务列表渲染
        table.render({
            elem: '#tableTaskList',
            url: '/task/getTaskList',
            where: param,
            method: 'post',
            contentType: 'application/json',
            page: true,
            cols: [[
                {field: 'taskId', title: 'ID', fixed: 'left', sort: true},
                {field: 'serialNumber', title: '序列号'},
                {field: 'type', title: '任务类型'},
                {field: 'describe', title: '描述'},
                {field: 'createTime', title: '创建时间'},
                {field: 'state', title: '状态0停 1完成 2进行 3等待'},
                {field: 'process', title: '任务进度', width: 220, align: 'center'/*, toolbar: '#taskProcessBar'*/},
                {fixed: 'right', width: 220, align: 'center', toolbar: '#taskOption'},
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
 * 表单监听form
 */
layui.use('form', function(){
    form = layui.form;
    form.on('submit(taskAdd)', function(data){
        // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
        // alert(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        $.ajax({
            url: '/task/add',
            type: 'POST',
            data: JSON.stringify(data.field),
            contentType: "application/json",
            success: function (result) {
                if(result.code == 10){
                    loadTaskManageTable();
                }
                layer.alert(result.msg);
            },
            failure : function() {
                layer.alert('操作超时!');
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(hUserAdd)', function(data){
        // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
        // alert(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        $.ajax({
            url: '/huser/addFile',
            type: 'POST',
            data: JSON.stringify(data.field),
            contentType: "application/json",
            success: function (result) {
                if(result.code == 10){
                    loadHUserTable();
                }
                layer.alert(result.msg);
            },
            failure : function() {
                layer.alert('操作超时!');
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(hUserEdit)', function(data){
        $.ajax({
            url: '/user/editUser',
            type: 'POST',
            data: JSON.stringify(data.field),
            contentType: "application/json",
            success: function (result) {
                if(result.code == 10){
                    loadHUserTable();
                }
                layer.msg(result.msg);
            }
        });
        return false;
    });
    form.on('submit(taskResult)', function(data){
        let taskId = data.field.taskId;
        console.log(taskId)
        let url = "/graduation/taskResult"+ "?taskId="+ taskId;
        window.open(url);
        return false;
    });
    form.on('submit(queryHUser)', function (data) {
        let jdata = {
            "grantUser": data.field
        }
        loadHUserTable(jdata);
    });
});

/**
 * 打开批量导入用户及用户档案列表页面
 */
function openImportHUsersFrame(){
    layer.open({
        title: '数据导入',
        area: ['400px', '300px'],
        //skin: 'layui-layer-lan',
        type: 2,
        content: ['/huser/import', 'no'] //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
    });
}

/**
 * 开始同步
 *  Mysql->Hdfs
 *  需要引入echarts.min.js
 */
function hdfsNow(){
    // 基于准备好的dom，初始化echarts实例
    let myChart = echarts.init(document.getElementById('hdfsChart'));
    let updateChart = echarts.init(document.getElementById('mysqlChart'));
    // 指定图表的配置项和数据
    option = {
        tooltip : {
            formatter: "{a} <br/>{b} : {c}%"
        },
        toolbox: {
            feature: {
                restore: {},
                saveAsImage: {}
            }
        },
        series: [
            {
                name: '同步状态',
                type: 'gauge',
                detail: {formatter:'{value}%'},
                data: [{value: 50, name: '完成率'}]
            }
        ]
    };
    updateOption = {
        tooltip : {
            formatter: "{a} <br/>{b} : {c}%"
        },
        toolbox: {
            feature: {
                restore: {},
                saveAsImage: {}
            }
        },
        series: [
            {
                name: '上传进度',
                type: 'gauge',
                detail: {formatter:'{value}%'},
                data: [{value: 50, name: '完成率'}]
            }
        ]
    };
    setInterval(function () {
        // option.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
        let process = 50;
        let updateProcess = 50;
        $.ajax({
            url: '/huser/hdfsNow',
            type: 'GET',
            async: false,
            contentType: "application/json",
            success: function (result) {
                if(result.code == 10){
                    process = result.data;
                }
            }
        });
        $.ajax({
            url: '/huser/mysqlNow',
            type: 'GET',
            async: false,
            contentType: "application/json",
            success: function (result) {
                if(result.code == 10){
                    updateProcess = result.data;
                }
            }
        });
        option.series[0].data[0].value = process.toFixed(2) - 0;
        updateOption.series[0].data[0].value = updateProcess.toFixed(2) - 0;
        myChart.setOption(option, true);
        updateChart.setOption(updateOption, true);
    },2000);
}


