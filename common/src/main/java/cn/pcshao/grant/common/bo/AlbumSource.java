package cn.pcshao.grant.common.bo;

import java.util.Date;

/**
 * 相册资源基础类
 * @author pcshao.cn
 * @date 2018/12/4
 */
public class AlbumSource {

    private Long id;

    private String type1;

    private String type2;

    private String type3;

    private String name;

    private String describ;

    private String pathLocal;

    private String pathFtp;

    private Long userId;

    private String userName;

    private String userNickname;

    private Date createtime;

    private Float filesize;

    //子类字段

    private Boolean isPrivate;

    private Boolean display;

    @Override
    public String toString() {
        return "AlbumSource{" +
                "id=" + id +
                ", type1='" + type1 + '\'' +
                ", type2='" + type2 + '\'' +
                ", type3='" + type3 + '\'' +
                ", name='" + name + '\'' +
                ", describ='" + describ + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userNickname='" + userNickname + '\'' +
                '}';
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Float getFilesize() {
        return filesize;
    }

    public void setFilesize(Float filesize) {
        this.filesize = filesize;
    }

    public String getPathLocal() {
        return pathLocal;
    }

    public void setPathLocal(String pathLocal) {
        this.pathLocal = pathLocal;
    }

    public String getPathFtp() {
        return pathFtp;
    }

    public void setPathFtp(String pathFtp) {
        this.pathFtp = pathFtp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
