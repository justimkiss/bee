package com.bee.test.component.impl;

import com.bee.test.component.MemberService;
import com.bee.test.component.domain.Member;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeoy.zhou on 3/4/16.
 */
@Service("memberService")
public class MemberServiceImpl implements MemberService {

    private static Map<String, Member> memberMap = new HashMap<String, Member>();
    private String args;

    static {
        Member member = new Member("M111", "tt@tt", "M1", 12);
        memberMap.put(member.getMemberName(), member);
        member = new Member("M2222", "ss@ww.com", "M2", 22);
        memberMap.put(member.getMemberName(), member);
    }

    @Override
    public Member getMemberByName(String memberName) {
        Member member = memberMap.get(memberName);
        member.setEmail(args);
        return member;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    @Override
    public List<Member> getAllMember() {
        return new ArrayList<Member>(memberMap.values());
    }
}
