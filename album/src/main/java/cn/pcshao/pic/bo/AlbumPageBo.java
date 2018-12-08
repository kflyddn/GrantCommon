package cn.pcshao.pic.bo;

import cn.pcshao.grant.common.base.BasePageBo;

/**
 * @author pcshao.cn
 * @date 2018-12-08
 */
public class AlbumPageBo extends BasePageBo {

    private String username;
    private String userNickname;
    private String name;
    private String describe;

    @Override
    public String toString() {
        return "AlbumPageBo{" +
                "username='" + username + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
