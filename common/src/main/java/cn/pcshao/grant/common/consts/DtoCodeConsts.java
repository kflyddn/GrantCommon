package cn.pcshao.grant.common.consts;

/**
 * @author pcshao.cn
 * @date 2018-11-24
 * 错误代码常量类
 *  自定义页面返回等错误代码
 */
public class DtoCodeConsts {

    public static final int VIEW_SUCCESS = 10;
    public static final String VIEW_SUCCESS_MSG = "操作成功！";
    public static final int VIEW_ERROR = -10;
    public static final String VIEW_ERROR_MSG = "操作失败，请联系管理员！";

    public static final int LOGIN_FAILUR = -11;
    public static final String LOGIN_FAILUR_MSG = "登录失败，请检查用户名和密码！";

    public static final int USER_EXISTS = -12;
    public static final String USER_EXISTS_MSG = "此用户已存在";
    public static final int ROLE_EXISTS = -13;
    public static final String ROLE_EXISTS_MSG = "此角色已存在";
    public static final int NO_PERMISSION = -14;
    public static final String NO_PERMISSION_MSG = "权限不足";

    public static final int ALBUM_PIC_EMPTY = -20;
    public static final String ALBUM_PIC_EMPTY_MSG = "图片资源为空！";
    public static final int ALBUM_PIC_NO = -21;
    public static final String ALBUM_PIC_NO_MSG = "不是图片资源文件！";

    public static final int DB_PRIMARY_EXIST = -50;
    public static final String DB_PRIMARY_EXIST_MSG = "请检查数据序列ID";


}
