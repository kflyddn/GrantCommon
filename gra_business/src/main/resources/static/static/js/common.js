
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
                $("#currUsername").html("当前用户: "+ user.username);
            }
        }
    });
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
/**
 * 点击h1返回首页
 */
$(".title").click(function () {
    console.log("最伟大的Grant author@pcshao.cn");
    location.href="/";
});

/**
 * 重置数据库与HDFS
 */
function resetDBandHDFS(){
    alert("请稍等...成功完会弹窗提示，请不要重复点击");
    $.ajax({
        url: '/huser/resetDBandHDFS',
        type: 'get',
        data: {},
        dataType: 'json',
        success: function (res) {
            if (res.code == 10) {
                alert('重置成功');
            } else {
                alert("操作失败");
            }
        }
    });
}