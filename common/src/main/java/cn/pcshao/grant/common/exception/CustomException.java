package cn.pcshao.grant.common.exception;

/**
 * @author pcshao.cn
 * @date 2018-11-24
 * 页面异常类
 *  适合在service层抛出后自动处理（一般是处理成Dto，抛出时指定错误msg，同样可以处理到Dto）
 */
public class CustomException extends RuntimeException {

    /**
     * 页面错误代码封装
     */
    private Integer code;

    public CustomException(Integer code, String message){
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
