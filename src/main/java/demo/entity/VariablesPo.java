package demo.entity;

import org.springframework.web.multipart.MultipartFile;

/**
 * Author:许清远
 * Data:2018/6/27
 * Description:
 */
public class VariablesPo {

    private MultipartFile multipartFile;
    private String textF;
    private String textZ;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getTextF() {
        return textF;
    }

    public void setTextF(String textF) {
        this.textF = textF;
    }

    public String getTextZ() {
        return textZ;
    }

    public void setTextZ(String textZ) {
        this.textZ = textZ;
    }
}
