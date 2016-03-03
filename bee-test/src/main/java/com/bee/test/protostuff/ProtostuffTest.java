package com.bee.test.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.common.collect.Lists;
import org.junit.Test;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public class ProtostuffTest {

    @Test
    public void test() {
        Schema<User> UserScheme = RuntimeSchema.getSchema(User.class);
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("user@user.com");
        LinkedBuffer buffer = LinkedBuffer.allocate(1024);
        byte[] data = ProtobufIOUtil.toByteArray(user, UserScheme, buffer);
        System.out.println(data);

        User result = new User();
        ProtobufIOUtil.mergeFrom(data, result, UserScheme);
        System.out.println(result);
    }


    @Test
    public void test1() {
        Schema<Person> personSchema = RuntimeSchema.createFrom(Person.class);
        Person person = new Person();
        person.setFirstName("firstName");
        person.setLastName("lastName");
        person.setEmail("user@user.com");
        Person person1 = new Person();
        person1.setFirstName("first");
        person1.setLastName("last");
        person1.setEmail("per@user.com");
        person.setPersons(Lists.newArrayList(person1));
        LinkedBuffer buffer = LinkedBuffer.allocate(1024);
        byte[] data = ProtobufIOUtil.toByteArray(person, personSchema, buffer);
        System.out.println(data);

        Person result = new Person();
        ProtobufIOUtil.mergeFrom(data, result, personSchema);
        System.out.println(result);
    }
}
