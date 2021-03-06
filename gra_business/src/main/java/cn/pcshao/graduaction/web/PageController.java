package cn.pcshao.graduaction.web;

import cn.pcshao.grant.common.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 基础页面控制器
 * @author pcshao
 * @date 2018-11-28
 */
@Controller
public class PageController extends BaseController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/login")
    public String login(){
        return "userlogin";
    }
    @GetMapping("/403")
    public String NB1(){
        return "error/403";
    }
    @GetMapping("/404")
    public String NB2(){
        return "error/404";
    }

    @GetMapping("/user")
    public String systemManage(){
        return "systemManage";
    }
    @GetMapping("/user/import")
    public String userImport(){
        return "upload/importUsers";
    }

    @GetMapping("/graduation")
    public String graduation(){
        return "graduation/dashboard";
    }
    @GetMapping("/graduation/taskResult")
    public ModelAndView taskResult(@RequestParam String taskId){
        //现在是跳转后的页面自己去url中拿参数，不是model传参
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("taskId", taskId);
        modelAndView.setViewName("graduation/taskResult");
        return modelAndView;
    }
    @GetMapping("/huser/import")
    public String fieImport(){
        return "upload/importFiles";
    }
}
