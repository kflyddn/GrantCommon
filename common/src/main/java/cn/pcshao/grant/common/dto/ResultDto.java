package cn.pcshao.grant.common.dto;

/**
 * @author pcshao.cn
 * @date 2018-11-24
 * Date Transfer Object
 *  Normal
 */
public class ResultDto<T> {

    private Integer code;
    private String msg;
    private T data;

    public ResultDto(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultDto(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultDto() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDto{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
