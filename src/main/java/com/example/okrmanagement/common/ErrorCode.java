package com.example.okrmanagement.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 业务逻辑错误 - 用户操作
    USER_NOT_FOUND(11001, "用户不存在"),
    USERNAME_EXISTS(11002, "用户名已存在"),
    EMAIL_EXISTS(11003, "邮箱已被使用"),
    BAD_CREDENTIALS(11004, "用户名或密码错误"),
    NO_PERMISSION(11005, "没有操作权限"),
    OBJECTIVE_NOT_FOUND(11006, "目标不存在"),
    KEY_RESULT_NOT_FOUND(11007, "关键结果不存在"),
    TASK_NOT_FOUND(11008, "任务不存在"),
    ANNIVERSARY_NOT_FOUND(11009, "纪念日不存在"),
    TIME_RECORD_NOT_FOUND(11010, "时间记录不存在"),
    
    // 参数错误 - 用户操作
    INVALID_PARAMETER(21100, "参数无效"),
    MISSING_PARAMETER(21101, "缺少必要参数"),
    INVALID_USERNAME(21102, "用户名格式错误"),
    INVALID_EMAIL(21103, "邮箱格式错误"),
    INVALID_PASSWORD(21104, "密码格式错误"),
    
    // 系统错误 - 系统操作
    SYSTEM_ERROR(32200, "系统内部错误"),
    DATABASE_ERROR(32201, "数据库操作错误"),
    NETWORK_ERROR(32202, "网络连接错误"),
    
    // 第三方服务错误
    THIRD_PARTY_ERROR(13001, "第三方服务错误");
    
    private final int code;
    private final String message;
}
