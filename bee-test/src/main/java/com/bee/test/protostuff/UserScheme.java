package com.bee.test.protostuff;

import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.UninitializedMessageException;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public class UserScheme implements Schema<User> {

    private static final HashMap<String, Integer> fieldMap = new HashMap<String, Integer>();

    static {
        fieldMap.put("email", 1);
        fieldMap.put("firstName", 2);
        fieldMap.put("lastName", 3);
        fieldMap.put("friends", 4);
    }

    @Override
    public String getFieldName(int number) {
        switch (number) {
            case 1:
                return "email";
            case 2:
                return "firstName";
            case 3:
                return "lastName";
            case 4:
                return "friends";
            default:
                return null;
        }
    }

    @Override
    public int getFieldNumber(String name) {
        Integer number = fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    @Override
    public boolean isInitialized(User message) {
        return message != null && StringUtils.isNotBlank(message.getEmail());
    }

    @Override
    public User newMessage() {
        return new User();
    }

    @Override
    public String messageName() {
        return User.class.getSimpleName();
    }

    @Override
    public String messageFullName() {
        return User.class.getName();
    }

    @Override
    public Class<? super User> typeClass() {
        return User.class;
    }

    @Override
    public void mergeFrom(Input input, User message) throws IOException {
        while (true) {
            int number = input.readFieldNumber(this);
            switch (number) {
                case 0 :
                    return;
                case 1 :
                    message.setEmail(input.readString());
                    break;
                case 2 :
                    message.setFirstName(input.readString());
                    break;
                case 3 :
                    message.setLastName(input.readString());
                    break;
                case 4 :
                    if(message.getFriends() == null) {
                        List<User> firends = Lists.newArrayListWithCapacity(message.getFriends().size());
                        message.setFriends(firends);
                    }
                    message.getFriends().add(input.mergeObject(null, this));
                    break;
                default:
                    input.handleUnknownField(number, this);
            }
        }
    }

    @Override
    public void writeTo(Output output, User message) throws IOException {
        if (message == null) {
            throw new UninitializedMessageException(message, this);
        }
        output.writeString(1, message.getEmail(), false);
        output.writeString(2, message.getFirstName(), false);
        output.writeString(3, message.getLastName(), false);
        if(CollectionUtils.isNotEmpty(message.getFriends())) {
            for (User friend : message.getFriends()) {
                output.writeObject(4, friend, this, true);;

            }
        }
    }
}
