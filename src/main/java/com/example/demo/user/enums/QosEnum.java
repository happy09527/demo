package com.example.demo.user.enums;

public enum QosEnum {
    Qos0(0),Qos1(1),Qos2(2);

    private final int value;
    QosEnum(int value) {
        this.value = value;
    }
    public int value(){
        return this.value;
    }
}
