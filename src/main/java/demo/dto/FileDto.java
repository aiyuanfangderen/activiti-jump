package demo.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * Author:许清远
 * Data:2018/7/7
 * Description:
 */
public class FileDto {
    private MultipartFile file;
    private String fileMsg;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFileMsg() {
        return fileMsg;
    }

    public void setFileMsg(String fileMsg) {
        this.fileMsg = fileMsg;
    }
}
