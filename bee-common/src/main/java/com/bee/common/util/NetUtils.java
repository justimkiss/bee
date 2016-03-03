package com.bee.common.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * Created by jeoy.zhou on 12/16/15.
 */
public class NetUtils {

    /**
     * 获取本地IP
     * @return
     */
    public static String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断本地指定端口是否已经被占用
     * @param port
     * @return
     */
    public static boolean isPortOnUse(int port) {
        boolean isUse = false;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            isUse = false;
        } catch (IOException e) {
            e.printStackTrace();
            isUse = true;
        } finally {
             if(serverSocket != null) {
                 try {
                     serverSocket.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
        }
        return isUse;
    }
}
