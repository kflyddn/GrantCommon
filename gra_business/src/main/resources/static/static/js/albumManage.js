    layui.use(['upload', 'form'], function(){
        var upload = layui.upload;
        var form = layui.form;
        var dataParam;

        /**
         * 监听上传按钮，准备request参数
         */
        form.on('submit(picUpload)', function(data){
            dataParam = data.field;
            console.log(dataParam);
        });

        /**
         * 上传组件
         */
        var uploadInst = upload.render({
            elem: '#picUpload' //绑定元素
            ,url: '/album/add' //上传接口
            ,multiple: false
            ,before: function(obj){ //加载除文件之外的参数
                this.data = dataParam;
                layer.load(); //开始上传之后打开load层
            }
            ,done: function(res){
                if(res.code == 10){
                    alert("done！")
                }
                    alert(res.msg)
                location.reload();
            }
            ,error: function(){
                //请求异常回调
            }
            ,size: 40*1024
            ,accept: 'images' //images（图片）、file（所有文件）、video（视频）、audio（音频）
        });
    });

    function loadAlbumTable(){
        layui.use('table', function() {
            var table = layui.table;

            table.render({
                elem: '#square'
                , url: '/album/square'
                , method: 'post'
                , contentType: 'application/json'
                , page: true //开启分页
                , cols: [[ //表头
                    {field: 'name', width: 150, title: '名称'},
                    , {field: 'pathFtp', width: 200, title: '图片'}
                    , {field: 'userNickname', width: 150, title: '上传用户昵称'}
                    , {field: 'createtime', width: 130, title: '上传时间'}
                    , {fixed: 'right', width: 150, align: 'center', toolbar: '#albumOption'},
                ]]
                , parseData: function (res) {
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


    layui.use('table', function(){
        var table = layui.table;
        var $ = layui.jquery;

        loadAlbumTable();

        /**
         * 监听option
         */
        table.on('tool(album)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                layer.confirm("确认删除资源-"+obj.data.name, {btn: ['确定', '取消'],title:"提示"},function () {
                    var idList = data.id;
                    var sourceType = "picPublic";
                    $.ajax({
                        url: '/album/delete',
                        type: 'post',
                        data: {
                            idList: idList,
                            sourceType: sourceType
                        },
                        dataType: 'json',
                        success: function (result) {
                            if(result.code == 10){
                                loadAlbumTable();
                            }
                            layer.alert(result.msg);
                        },
                        failure : function() {
                            layer.alert('操作超时!');
                        }
                    });
                });
            }
        });

    });