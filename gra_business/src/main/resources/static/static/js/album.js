layui.use('upload', function(){
    var upload = layui.upload;

    //执行实例
    var uploadInst = upload.render({
        elem: '#picUpload' //绑定元素
        ,url: '/album/add' //上传接口
        ,done: function(res){
            if(res.code == 10){
                alert("上传成功！")
            }
                alert(res.msg)
        }
        ,error: function(){
            //请求异常回调
        }
        ,size: 40*1024
        ,accept: 'images' //images（图片）、file（所有文件）、video（视频）、audio（音频）
    });
});

layui.use('table', function(){
    var table = layui.table;

    table.render({
        elem: '#square'
        ,url: '/album/square'
        ,method: 'post'
        ,contentType: 'application/json'
        ,page: true //开启分页
        ,cols: [[ //表头
            {field: 'name', title: '名称'},
            ,{field: 'pathFtp', title: '图片'}
            ,{field: 'userNickname', title: '上传用户昵称'}
            ,{field: 'createtime', title: '上传时间'}
        ]]
        ,parseData: function (res) {
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