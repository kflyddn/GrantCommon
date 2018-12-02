package cn.pcshao.graduaction.web;

import cn.pcshao.grant.common.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 基础页面控制器
 * @author pcshao
 * @date 2018-11-28
 */
@Controller
public class PageController extends BaseController {

    @GetMapping("/login")
    @ResponseBody
    public String login(){
        return "you need login!";
    }
    @GetMapping("/403")
    public String NB1(){
        return "403";
    }
    @GetMapping("/404")
    public String NB2(){
        return "404";
    }
    @GetMapping("initPage")
    public String initPage(){
        return "init";
    }

}
