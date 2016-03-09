package com.bee.test.component.domain;

import java.io.Serializable;

/**
 * Created by jeoy.zhou on 3/4/16.
 */
public class Member implements Serializable {

    private static final long serialVersionUID = 6759027583399834340L;

    private String memberCode;
    private String email;
    private String memberName;
    private int age;

    public Member(String memberCode, String email, String memberName, int age) {
        this.memberCode = memberCode;
        this.email = email;
        this.memberName = memberName;
        this.age = age;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
