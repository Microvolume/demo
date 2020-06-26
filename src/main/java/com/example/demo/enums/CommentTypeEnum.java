package com.example.demo.enums;

public enum CommentTypeEnum {
    //如果是Question，就是1
    QUESTION(1),
    //如果是Comment，就是2
    COMMENT(2);
    private Integer type;

    public Integer getType() {
        return type;
    }

    // 枚举类的构造方法
    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
