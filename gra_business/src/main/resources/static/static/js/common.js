
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