package demo.until;

/**
 * Author:许清远
 * Data:2018/7/5
 * Description:
 */
public class Result {

    private int code;
    private String msg;
    private Object data;

    public Result() {
        super();
    }

    public Result(ResultCode resultCode, Object data) {
        this.setResultCode(resultCode);
        this.data = data;
    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public void setResultCodeAndData(ResultCode resultCode, Object data) {
        this.setResultCode(resultCode);
        this.msg = resultCode.getMsg();
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }

}
