package com.hana.service.Enum;


import com.hana.service.Common.Const;
import com.hana.service.Common.FunctionPath;

import java.util.Optional;
import java.util.stream.Stream;

public enum OperationEnum {
    ADD_USER(FunctionPath.user.create, "Add User", Const.LOG_TYPE_OPERATION),
    delete(FunctionPath.user.delete, "Delete User", Const.LOG_TYPE_OPERATION),
    update(FunctionPath.user.update, "Update User", Const.LOG_TYPE_OPERATION),
    getAll(FunctionPath.user.getAll, "Get All User", Const.LOG_TYPE_OPERATION),
    getUserById(FunctionPath.user.getUserById, "Get User Info", Const.LOG_TYPE_OPERATION),
    changePassword(FunctionPath.user.changePassword, "Change Password", Const.LOG_TYPE_OPERATION),

    LOGIN(FunctionPath.AUTH.login, "Login", Const.LOG_TYPE_SYSTEM),
    LOGOUT(FunctionPath.AUTH.logout, "Logout", Const.LOG_TYPE_SYSTEM);

    OperationEnum(String path, String type, String logType){
        this.path = path;
        this.type = type;
        this.logType = logType;
    }

    private String path;
    private String type;
    private String logType;

    public String getLogType() {
        return logType;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public static  Optional<OperationEnum> checkPathReturnType(String path) {
        Optional<OperationEnum> optional = Stream.of(values()).filter(e -> e.getPath().equals(path)).findFirst();
        return optional;
    }
}
