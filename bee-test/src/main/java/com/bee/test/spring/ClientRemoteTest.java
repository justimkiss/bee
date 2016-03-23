package com.bee.test.spring;

import com.bee.test.JUnit4ClassRunner;
import com.bee.test.component.MemberService;
import com.bee.test.component.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * Created by jeoy.zhou on 3/4/16.
 */
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/spring-client.xml"})
public class ClientRemoteTest {

    @Autowired
    @Qualifier("memberServiceRemote")
    private MemberService memberService;

    @Test
    public void testGetMemberByName() throws InterruptedException {
        while (true) {
            try {
                Member member = memberService.getMemberByName("M1");
                System.out.println(member.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(1000);
        }
//        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testGetAllMember() {
        List<Member> members = memberService.getAllMember();
        System.out.println(members);
    }

}
