package cn.pcshao.grant.common.base;

/**
 * @author pcshao.cn
 * @date 2018-12-08
 */
public abstract class BasePageBo {

    Integer pageNum;
    Integer pageSize;

    public boolean checkSelf(){
        if(null != pageNum || null != pageSize)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "BasePageBo{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
