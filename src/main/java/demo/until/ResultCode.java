package demo.until;

/**
 * Author:许清远
 * Data:2018/7/5
 * Description:
 */
public enum ResultCode {
    
    SUCCESS(0, "请求成功"),
    WARN(-1, "网络异常，请稍后重试"),
    ERROR(-2, "服务器端异常"),
    LOGIN_SUCCESS(1,"登陆成功"),
    LOGIN_FAIL(2,"登陆失败"),
    LOGIN_OVERTIME(3,"登陆失效");


    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
