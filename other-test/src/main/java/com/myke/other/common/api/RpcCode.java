package com.myke.other.common.api;

/**
 * @author zhangjianbin
 * @date 2021年07月28日12:19
 */
public enum RpcCode implements IErrorCode{
    /**操作成功**/
    RC100(100,"操作成功"),
    /**操作失败**/
    RC999(999,"操作失败"),
    /**服务限流**/
    RC200(200,"服务开启限流保护,请稍后再试!"),
    /**服务降级**/
    RC201(201,"服务开启降级保护,请稍后再试!"),
    /**热点参数限流**/
    RC202(202,"热点参数限流,请稍后再试!"),
    /**系统规则不满足**/
    RC203(203,"系统规则不满足要求,请稍后再试!"),
    /**授权规则不通过**/
    RC204(204,"授权规则不通过,请稍后再试!"),
    /**access_denied**/
    RC403(403,"无访问权限,请联系管理员授予权限"),
    /**access_denied**/
    RC401(401,"匿名用户访问无权限资源时的异常"),
    /**服务异常**/
    RC500(500,"系统异常，请稍后重试"),

    ;
    private long code;
    private String message;

    RpcCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}