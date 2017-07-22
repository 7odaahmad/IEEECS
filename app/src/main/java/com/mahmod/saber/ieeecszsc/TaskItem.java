package com.mahmod.saber.ieeecszsc;

/**
 * Created by 7odaahmad on 7/19/2017.
 */

public class TaskItem {
    private String head;
    private String desc;
    private String address;



    public TaskItem(String head, String desc, String address) {
        this.head = head;
        this.desc = desc;
        this.address = address;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;

    }

    public String getAddress() {
        return address;
    }
}