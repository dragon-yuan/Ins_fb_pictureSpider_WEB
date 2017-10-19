package me.dragon.base.utils;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dragon on 4/26/2017.
 */
public class PicUtil implements Runnable{
    private Logger log = Logger.getLogger(this.getClass());
    private String imgUrl;
    protected HttpServletResponse response;

    public PicUtil(String imgUrl, HttpServletResponse response) {
        this.imgUrl = imgUrl;
        this.response = response;
    }

   /* @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }*/

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        String timeStr = sdf.format(new Date());
        System.out.println("时间：[" + timeStr + "] 图片流：" + response);
        if (imgUrl != null && !"".equals(imgUrl)) {
            File f = new File(imgUrl);
            InputStream inputStream = null;
            try {
                if (f.exists()) {
                    inputStream = new FileInputStream(f);
                    byte[] b = new byte[1024];
                    int len = -1;
                    while ((len = inputStream.read(b, 0, 1024)) != -1) {
                        response.getOutputStream().write(b, 0, len);
                    }
                }else {
                    inputStream = new InputStream() {
                        @Override
                        public int read() throws IOException {
                            return 0;
                        }
                    };
                }
            } catch (FileNotFoundException e) {
                log.error("图片文件不存在", e);
            } catch (IOException e) {
                log.error("读取图片IO错误", e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭图片IO错误", e);
                }
            }
        }
    }
}
