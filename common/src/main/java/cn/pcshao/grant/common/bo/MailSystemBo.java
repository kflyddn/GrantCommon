package cn.pcshao.grant.common.bo;

import java.util.Map;

/**
 * @author pcshao.cn
 * @date 2018/12/13
 */
public class MailSystemBo {

    private String from_address;

    private String subject;

    private String text;

    private Map toMailAddressRecordMap;

    @Override
    public String toString() {
        return "MailSystemBo{" +
                "from_address='" + from_address + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", toMailAddressRecordMap=" + toMailAddressRecordMap +
                '}';
    }

    public Map getToMailAddressRecordMap() {
        return toMailAddressRecordMap;
    }

    public void setToMailAddressRecordMap(Map toMailAddressRecordMap) {
        this.toMailAddressRecordMap = toMailAddressRecordMap;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
