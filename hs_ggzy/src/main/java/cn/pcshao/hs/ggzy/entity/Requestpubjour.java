package cn.pcshao.hs.ggzy.entity;

/**
 * 申请流水表
 * @author pcshao.cn
 * @date 2020-02-22
 */
public class Requestpubjour {

    public String SERIAL_NO;
    public String CURR_DATE;
    public String OPERATOR_NO;
    public String OP_REMARK;
    public String BUSINESS_STATUS;
    public String MOD_NO;
    public String BUSINESS_FLAG;
    public String MOD_TABLE;
    public String NEXTLEVEL;
    public String IS_EMERGENCY;
    public String FLAG;
    public String PRODUCT_NAME;

    @Override
    public String toString() {
        return "Requestpubjour{" +
                "SERIAL_NO='" + SERIAL_NO + '\'' +
                ", CURR_DATE='" + CURR_DATE + '\'' +
                ", OPERATOR_NO='" + OPERATOR_NO + '\'' +
                ", OP_REMARK='" + OP_REMARK + '\'' +
                ", BUSINESS_STATUS='" + BUSINESS_STATUS + '\'' +
                ", MOD_NO='" + MOD_NO + '\'' +
                ", BUSINESS_FLAG='" + BUSINESS_FLAG + '\'' +
                ", MOD_TABLE='" + MOD_TABLE + '\'' +
                ", NEXTLEVEL='" + NEXTLEVEL + '\'' +
                ", IS_EMERGENCY='" + IS_EMERGENCY + '\'' +
                ", FLAG='" + FLAG + '\'' +
                ", PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
                '}';
    }
}
