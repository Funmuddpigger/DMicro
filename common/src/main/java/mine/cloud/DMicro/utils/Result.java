package mine.cloud.DMicro.utils;

import lombok.Data;

import java.io.Serializable;


@Data
public class Result implements Serializable {

    private Integer code; //状态码
    private String msg; //消息
    private Object data; //数据对象

    public void setData(Object data) {
        this.data = data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     *  成功状态
     * @param data
     * @return
     */
    public static Result success(Object data){
        Result result = new Result(); //别拿出去方法外面new,tm占用内存
        result.setCode(200);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    /**
     * @Override
     * @param data
     * @param msg
     * @return
     */
    public static  Result success(String msg,Object data){
        Result result = new Result(); //别拿出去方法外面new,tm占用内存
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }


    /**
     *  失败状态
     *  请求失败,500是服务器问题,我们内部问题
     * @return
     */
    public static Result serverfail(String msg){
        Result result = new Result();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * @Override
     * @param data
     * @param msg
     * @return
     */
    public static Result serverfail(String msg,Object data){
        Result result = new Result();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 请求未认证,跳转登录页
     * @param msg
     * @param data
     * @return
     */
    public static Result certificationfail(String msg, Object data){
        Result result = new Result();
        result.setCode(401);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * @Override
     * @param msg
     * @return
     */
    public static Result certificationfail(String msg){
        Result result = new Result();
        result.setCode(401);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * 请求未授权,跳转授权页
     * @param msg
     * @param data
     * @return
     */
    public static Result authorizationfail(String msg, Object data){
        Result result = new Result();
        result.setCode(406);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * @Override
     * @param msg
     * @return
     */
    public static Result authorizationfail(String msg){
        Result result = new Result();
        result.setCode(406);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static Result successjson(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 陈赫成功
     * @param msg
     * @return
     */
    public static Result audit(String msg){
        Result result = new Result();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static Result requestfail(int code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}
