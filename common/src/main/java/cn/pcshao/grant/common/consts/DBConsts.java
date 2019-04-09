package cn.pcshao.grant.common.consts;

/**
 * 数据库字典
 *  检查与数据库的注释
 * @author pcshao.cn
 * @date 2019-04-09
 */
public class DBConsts {
    public static final byte M2H_STATE_NO_0 = 0;
    public static final String M2H_STATE_INFO_0 = "未同步";
    public static final byte M2H_STATE_DONE_1 = 1;
    public static final String M2H_STATE_INFO_1 = "已同步";
    public static final byte M2H_STATE_REC_2 = 2;
    public static final String M2H_STATE_INFO_2 = "RECOVERY模式下的数据";
    public static final byte M2H_STATE_ALREADY_3 = 3;
    public static final String M2H_STATE_INFO_3 = "RECOVERY模式下DB中已存在的数据";

    public static final byte TASK_STATE_STOP_0 = 0;
    public static final String TASK_STATE_INFO_0 = "停止";
    public static final byte TASK_STATE_DONE_1 = 1;
    public static final String TASK_STATE_INFO_1 = "完毕";
    public static final byte TASK_STATE_ING_2 = 2;
    public static final String TASK_STATE_INFO_2 = "进行中";
    public static final byte TASK_STATE_RESTART_3 = 3;
    public static final String TASK_STATE_INFO_3 = "重新开始任务";
}
