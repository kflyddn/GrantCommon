package cn.pcshao.pic.bo;

import cn.pcshao.grant.common.bo.MailSystemBo;

/**
 * 监控页面Business Object
 * @author pcshao.cn
 * @date 2018/12/13
 */
public class AlbumMonitorBo {

    private int picPublicDisplay;
    private int picPublicDisplayNone;

    private int picPersonalPrivate;
    private int picPersonalPrivateNone;

    private MailSystemBo mailSystem;

    @Override
    public String toString() {
        return "AlbumMonitorBo{" +
                "picPublicDisplay=" + picPublicDisplay +
                ", picPublicDisplayNone=" + picPublicDisplayNone +
                ", picPersonalPrivate=" + picPersonalPrivate +
                ", picPersonalPrivateNone=" + picPersonalPrivateNone +
                ", mailSystem=" + mailSystem +
                '}';
    }

    public int getPicPublicDisplay() {
        return picPublicDisplay;
    }

    public void setPicPublicDisplay(int picPublicDisplay) {
        this.picPublicDisplay = picPublicDisplay;
    }

    public int getPicPublicDisplayNone() {
        return picPublicDisplayNone;
    }

    public void setPicPublicDisplayNone(int picPublicDisplayNone) {
        this.picPublicDisplayNone = picPublicDisplayNone;
    }

    public int getPicPersonalPrivate() {
        return picPersonalPrivate;
    }

    public void setPicPersonalPrivate(int picPersonalPrivate) {
        this.picPersonalPrivate = picPersonalPrivate;
    }

    public int getPicPersonalPrivateNone() {
        return picPersonalPrivateNone;
    }

    public void setPicPersonalPrivateNone(int picPersonalPrivateNone) {
        this.picPersonalPrivateNone = picPersonalPrivateNone;
    }

    public MailSystemBo getMailSystem() {
        return mailSystem;
    }

    public void setMailSystem(MailSystemBo mailSystem) {
        this.mailSystem = mailSystem;
    }
}
