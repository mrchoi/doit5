package com.ckstack.ckpush.common.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 7. 2..
 */
public class DownloadView extends AbstractView {
    private final static Logger LOG = LoggerFactory.getLogger(DownloadView.class);

    public DownloadView() {
        setContentType("applicaiton/download;charset=utf-8");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        OutputStream out = null;
        FileInputStream in = null;

        try {
            this.setResponseContentType(request, response);
            File downloadFile = (File) model.get("downloadFile");
            String fileName = (String) model.get("fileName");

            LOG.info("try download file [" + downloadFile.toString() + "], fileName [" + fileName + "]");

            String userAgent = request.getHeader("User-Agent");

            boolean isIe = false;
            if(userAgent.indexOf("MSIE") != -1 || userAgent.indexOf("Trident") != -1) isIe = true;
            //boolean isIe = userAgent.indexOf("MSIE") != -1;

            if(isIe)    fileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+","%20");
            else        fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");

            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");

            response.setContentLength((int) downloadFile.length());

            out = response.getOutputStream();
            in = new FileInputStream(downloadFile);

            FileCopyUtils.copy(in, out);
            out.flush();

        } catch (Exception e) {

        } finally {
            try { if (in != null) in.close(); } catch (Exception ioe) {}
            try { if (out != null) out.close(); } catch (Exception ioe) {}
        }
    }
}
