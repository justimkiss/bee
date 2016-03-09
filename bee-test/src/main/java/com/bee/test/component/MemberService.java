package com.bee.test.component;

import com.bee.test.component.domain.Member;

import java.util.List;

/**
 * Created by jeoy.zhou on 3/4/16.
 */
public interface MemberService {

    public Member getMemberByName(String memberName);

    public List<Member> getAllMember();
}
