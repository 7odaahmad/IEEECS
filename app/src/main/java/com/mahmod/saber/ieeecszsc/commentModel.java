package com.mahmod.saber.ieeecszsc;

/**
 * Created by 7odaahmad on 7/21/2017.
 */

public class commentModel {
    private String member;
    private String content;

    public commentModel(String member, String content) {
        this.member = member;
        this.content = content;
    }

    public String getMember() {
        return member;
    }

    public String getContent() {
        return content;
    }
}
