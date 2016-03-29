package com.bee.common.util;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by jeoy.zhou on 12/16/15.
 */
public class NetUtils {

    public static List<InetAddress> getAllLocalAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            List<InetAddress> addresses = new ArrayList<InetAddress>();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    addresses.add(inetAddress);
                }
            }

            return addresses;
        } catch (SocketException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<String> getAllLocalIp() {
        List<String> noLoopbackAddresses = new ArrayList<String>();
        List<InetAddress> allInetAddresses = getAllLocalAddress();

        for (InetAddress address : allInetAddresses) {
            if (!address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
                noLoopbackAddresses.add(address.getHostAddress());
            }
        }

        return noLoopbackAddresses;
    }

    public static String getFirstLocalIp() {
        List<String> allNoLoopbackAddresses = getAllLocalIp();
        if (allNoLoopbackAddresses.isEmpty()) {
            throw new IllegalStateException("Sorry, seems you don't have a network card :( ");
        }
        return allNoLoopbackAddresses.get(allNoLoopbackAddresses.size() - 1);
    }

    public static int getAvailablePort() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket();
            ss.bind(null);
            return ss.getLocalPort();
        } catch (IOException e) {
            throw new IllegalStateException("", e);
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
        }
    }

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
