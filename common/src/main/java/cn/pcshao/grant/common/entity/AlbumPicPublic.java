package cn.pcshao.grant.common.entity;

import cn.pcshao.grant.common.bo.AlbumSource;

import java.io.Serializable;
import java.util.Date;

public class AlbumPicPublic extends AlbumSource implements Serializable {
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

    private Boolean display;

    private Date createtime;

    private Double filesize;

    private static final long serialVersionUID = 1L;

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
        this.type1 = type1 == null ? null : type1.trim();
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2 == null ? null : type2.trim();
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3 == null ? null : type3.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ == null ? null : describ.trim();
    }

    public String getPathLocal() {
        return pathLocal;
    }

    public void setPathLocal(String pathLocal) {
        this.pathLocal = pathLocal == null ? null : pathLocal.trim();
    }

    public String getPathFtp() {
        return pathFtp;
    }

    public void setPathFtp(String pathFtp) {
        this.pathFtp = pathFtp == null ? null : pathFtp.trim();
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
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname == null ? null : userNickname.trim();
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

    public Double getFilesize() {
        return filesize;
    }

    public void setFilesize(Double filesize) {
        this.filesize = filesize;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type1=").append(type1);
        sb.append(", type2=").append(type2);
        sb.append(", type3=").append(type3);
        sb.append(", name=").append(name);
        sb.append(", describ=").append(describ);
        sb.append(", pathLocal=").append(pathLocal);
        sb.append(", pathFtp=").append(pathFtp);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", userNickname=").append(userNickname);
        sb.append(", display=").append(display);
        sb.append(", createtime=").append(createtime);
        sb.append(", filesize=").append(filesize);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}