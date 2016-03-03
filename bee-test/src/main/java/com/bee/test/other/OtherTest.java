package com.bee.test.other;

import com.bee.common.extension.ExtensionLoader;
import com.bee.remote.provider.server.Server;

import java.util.List;

/**
 * Created by jeoy.zhou on 12/25/15.
 */
public class OtherTest {

    public static void main(String[] args) {
        List<Server> servers = ExtensionLoader.newExtensionList(Server.class);
        for(Server server : servers) {
            System.out.println(server.getClass().getName());
        }
    }
}
