package cn.pcshao.graduaction.web;

import cn.pcshao.grant.common.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 基础页面控制器
 * @author pcshao
 * @date 2018-11-28
 */
@Controller
public class PageController extends BaseController {

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/403")
    public String NB1(){
        return "403";
    }
    @RequestMapping("/404")
    public String NB2(){
        return "404";
    }

    @RequestMapping("initPage")
    public String initPage(){
        return "init";
    }

}
