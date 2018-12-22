package cn.pcshao.pic.web;

import cn.pcshao.grant.common.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 相册页面控制器
 * @author pcshao.cn
 * @date 2018-12-4
 */
@Controller
@RequestMapping("/album")
public class AlbumPageController extends BaseController {

    @GetMapping("/albumManage")
    public String album(){
        return "albumManage";
    }
    @GetMapping("/albumSquare")
    public String albumSquare(){
        return "album";
    }

}
