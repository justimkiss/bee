## bee使用指南

bee是一个分布式服务RPC框架，在看了点评的开源RPC框架Pigeon，按照其设计思路实现了bee。

#### 依赖

bee依赖JDK1.6+

#### 准备工作

1. 下载代码后，通过maven构建项目

   git clone git@github.com:justimkiss/bee.git

   cd bee

   mvn clean install -Dmaven.test.skip=true

2. 环境准备

   bee只支持zookeeper作为协调服务，需要安装zookeeper服务。

   zookeeper配置文件指定存放目录`resources/config/zookeeper/zookeeper.proerties`；配置文件内容如下：

   ```properties
   zookeeper.services.url=127.0.0.1:2181
   zookeeper.services.namespace=bee/server
   zookeeper.session.timeout=30000
   zookeeper.connection.timeout=15000
   zookeeper.retry.time.limit=500
   zookeeper.retry.interval=3000
   ```

   需要修改的是`zookeeper.services.url`与`zookeeper.services.namespac`，分别表示zookeeper的服务地址以及bee存放的跟路径。

#### 快速入门

相关代码可以参考bee-test子模块中：

1、定义服务

MemberService.java

```java
package com.bee.test.component;
import com.bee.test.component.domain.Member;
import java.util.List;

public interface MemberService {
    public Member getMemberByName(String memberName);
    public List<Member> getAllMember();
}
```

服务提供对接口的实现：

MemberServiceImpl.java

```java
package com.bee.test.component.impl;
import com.bee.test.component.MemberService;
import com.bee.test.component.domain.Member;
import org.springframework.stereotype.Service;
import java.util.*;

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
```

2、服务提供者在spring配置声明如下：

```xml
<bean id="serviceRegister" class="com.bee.remote.spring.ServiceRegistry" init-method="init" lazy-init="false">
  	<!-- 服务端口 -->
    <property name="port" value="3333"/>
  	<!-- 服务权重 -->
     <property name="weight" value="10"/>
  	<!-- 服务列表 -->
     <property name="services">
		<map>
          	<!-- 服务全局唯一key -->
			<entry key="com.bee.test.component.MemberService_1.0.0" value-ref="memberService"/>
		</map>
      </property>
</bean>

<bean id="memberService" class="com.bee.test.component.impl.MemberServiceImpl">
	<property name="args" value="3333"/>
</bean>
```

3、服务调用者

服务调用者spirng的配置如下：

```xml
<bean id="memberServiceRemote" class="com.bee.remote.spring.ProxyBeanFactory" init-method="init">
  	<!-- 服务全局唯一key -->
	<property name="serviceName" value="com.bee.test.component.MemberService_1.0.0"/>
  	<!-- 对应该服务的接口定义 -->
	<property name="interfaceName" value="com.bee.test.component.MemberService"/>
  	<!-- 调用方式 -->
    <property name="callMethod" value="sync"/>
  	<!-- 超时时间，如果超过改时间，客户端就不再等待服务提供者返回 -->
    <property name="timeOut" value="2000"/>
</bean>
```

4、测试程序

在bee-test子模块中，`com.bee.test.spring.ClientRemoteTest`与`com.bee.test.spring.ServiceRegisterTest`。先运行`ServiceRegisterTest`，然后运行`ClientRemoteTest`。

   ​

   ​

