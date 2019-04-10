package cn.pcshao.graduaction.web;

import cn.pcshao.grant.common.base.BaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 下载资源用
 * @author pcshao.cn
 * @date 2019-04-10
 */
@Controller
@RequestMapping("/download")
public class DownloadController extends BaseController {

    private boolean checkResource(InputStream resource, String content){
        if(null!=resource) {
            logger.debug("模板找到，正在下载：" + content);
            return true;
        }else{
            logger.info("模板资源失败："+ content);
            return false;
        }
    }

    @ApiOperation("下载用户上传模板")
    @GetMapping("/userFileTemplate")
    public void downloadUserFileTemplate(HttpServletResponse response) {
        String filePath = "xls/importUsersTemplate.xls";
        InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if(checkResource(resource, filePath))
            writeFile2Response(resource, response, filePath.split("/")[1]);
    }

    @ApiOperation("下载档案用户上传模板")
    @GetMapping("/huserFileTemplate")
    public void downloadHUserFileTemplate(HttpServletResponse response) {
        String filePath = "xls/importHUsersTemplate.xls";
        InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if(checkResource(resource, filePath))
            writeFile2Response(resource, response, filePath.split("/")[1]);
    }

    private void writeFile2Response(InputStream inputStream, HttpServletResponse response, String fileName){
        writeFile2Response(inputStream, response, 1024, fileName);
    }
    /**
     * 将文件写到流里
     *  缓存写
     * @param inputStream
     * @param response HttpServletResponse
     * @param bufferSize 缓冲大小
     */
    private void writeFile2Response(InputStream inputStream, HttpServletResponse response, int bufferSize, String fileName){
        //重置buffer
        response.resetBuffer();
        //设定编码为UTF-8
        response.setCharacterEncoding("UTF-8");
        //设置头部为下载信息
        response.setHeader("Content-type", "application/force-download;charset=UTF-8");
        response.setHeader("content-disposition", "attachment;filename="+ fileName);

        InputStream is = null;
        OutputStream os = null;
        BufferedInputStream bis = null;
        try {
            is = inputStream;
            os = response.getOutputStream();
            bis = new BufferedInputStream(is);
            byte[] buffer = new byte[bufferSize];
            int c = 0;
            while((c = bis.read(buffer)) != -1){
                os.write(buffer, 0, c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
