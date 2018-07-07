package demo.entity;

/**
 * Author:许清远
 * Data:2018/7/3
 * Description:
 */
public class TaskPo {

    private String startTime;
    private String name;
    private String textF;
    private String textZ;
    private String display;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
