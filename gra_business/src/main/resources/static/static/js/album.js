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