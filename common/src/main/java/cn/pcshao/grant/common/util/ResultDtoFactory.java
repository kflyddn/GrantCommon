package cn.pcshao.grant.common.util;

import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.dto.ResultDto;

/**
 * @author pcshao.cn
 * @date 2018-11-24
 * Date Transfer Object Factory
 *  工具类，提供DTO的成功、失败状态信息
 */
public class ResultDtoFactory {

    private static ResultDto resultDto;

    public static ResultDto success(){
        return new ResultDto(DtoCodeConsts.VIEW_SUCCESS, DtoCodeConsts.VIEW_SUCCESS_MSG, null);
    }
    public static ResultDto success(Object data){
        return new ResultDto(DtoCodeConsts.VIEW_SUCCESS, DtoCodeConsts.VIEW_SUCCESS_MSG, data);
    }
    public static ResultDto success(Object data, String msg){
        return new ResultDto(DtoCodeConsts.VIEW_SUCCESS, msg, data);
    }
    public static ResultDto error(){
        return new ResultDto(DtoCodeConsts.VIEW_ERROR, DtoCodeConsts.VIEW_ERROR_MSG);
    }
    public static ResultDto error(Integer code){
        return new ResultDto(code, DtoCodeConsts.VIEW_ERROR_MSG);
    }
    public static ResultDto error(Integer code, String msg){
        return new ResultDto(code, msg);
    }
}
