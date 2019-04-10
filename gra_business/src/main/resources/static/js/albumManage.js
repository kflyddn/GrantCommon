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

    function openUploadAlbumFrame(){
        layer.open({
            title: '相册资源上传',
            area: ['800px', '600px'],
            //skin: 'layui-layer-lan',
            type: 2,
            content: ['/album/upload', 'no'] //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
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