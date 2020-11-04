package com.qqgsd.crowd.util;

/**
 * 统一项目的Ajax请求返回结果
 * @param <T>
 */
public class ResultEntity<T> {

    public static final String SUCCESS="SUCCESS";
    public static final String FAILED="FAILED";

    //当前请求处理结果成功还是失败
    private String result;

    //请求处理失败时返回错误消息
    private String message;

    //要返回的数据
    private T data;

    /**
     * 请求处理成功,不需要返回数据
     */
    public static <E> ResultEntity<E> successWithoutData(){
        return new ResultEntity<E>(SUCCESS,null,null);
    }

    /**
     * 请求处理成功,需要返回数据
     * @param data 需要返回数据
     */
    public static <E> ResultEntity<E> successWithData(E data){
        return new ResultEntity<E>(SUCCESS,null,data);
    }

    /**
     *请求处理失败
     * @param message 失败的错误消息
     */
    public static <E> ResultEntity<E> failed(String message){
        return new ResultEntity<E>(FAILED,message,null);
    }


    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
