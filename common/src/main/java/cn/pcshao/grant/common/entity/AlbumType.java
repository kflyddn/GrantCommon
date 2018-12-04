package cn.pcshao.grant.common.entity;

import java.io.Serializable;

public class AlbumType implements Serializable {
    private Integer id;

    private String type1;

    private String type1name;

    private String type2;

    private String type2name;

    private String type3;

    private String type3name;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1 == null ? null : type1.trim();
    }

    public String getType1name() {
        return type1name;
    }

    public void setType1name(String type1name) {
        this.type1name = type1name == null ? null : type1name.trim();
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2 == null ? null : type2.trim();
    }

    public String getType2name() {
        return type2name;
    }

    public void setType2name(String type2name) {
        this.type2name = type2name == null ? null : type2name.trim();
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3 == null ? null : type3.trim();
    }

    public String getType3name() {
        return type3name;
    }

    public void setType3name(String type3name) {
        this.type3name = type3name == null ? null : type3name.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type1=").append(type1);
        sb.append(", type1name=").append(type1name);
        sb.append(", type2=").append(type2);
        sb.append(", type2name=").append(type2name);
        sb.append(", type3=").append(type3);
        sb.append(", type3name=").append(type3name);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}