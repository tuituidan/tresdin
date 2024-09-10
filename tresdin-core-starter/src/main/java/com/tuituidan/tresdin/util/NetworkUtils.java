package com.tuituidan.tresdin.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * NetworkUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@UtilityClass
@Slf4j
public class NetworkUtils {

    private static String localIp = "";

    private static String localUrl = "";

    /**
     * isConnected
     *
     * @param ip ip
     * @param port port
     * @param timeout timeout
     * @return boolean
     */
    public static boolean isConnected(String ip, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), timeout);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 本地地址
     *
     * @param environment spring环境变量，用于获取port和context-path
     * @return String
     */
    public static String getLocalUrl(Environment environment) {
        if (StringUtils.hasText(localUrl)) {
            return localUrl;
        }
        // docker环境可能就需要单独配置clientUrl
        String clientUrl = environment.getProperty("clientUrl");
        if (StringUtils.hasText(clientUrl)) {
            localUrl = clientUrl;
            return localUrl;
        }
        String port = environment.getProperty("server.port",
                environment.getProperty("local.server.port",
                        "8080"));
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        localUrl = StringExtUtils.format("http://{}:{}{}", getLocalIp(), port, contextPath);
        return localUrl;
    }

    /**
     * 获取本机IP.
     *
     * @return IP
     */
    public static String getLocalIp() {
        if (StringUtils.hasText(localIp)) {
            return localIp;
        }
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface iface = ifaces.nextElement();
                if (!isValidInterface(iface)) {
                    continue;
                }
                Enumeration<InetAddress> inetAddrs = iface.getInetAddresses();
                while (inetAddrs.hasMoreElements()) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    if (isValidAddress(inetAddr)) {
                        localIp = inetAddr.getHostAddress();
                        break;
                    }
                }
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("InetAddress.getLocalHost获取失败.");
            }
            localIp = jdkSuppliedAddress.getHostAddress();
        } catch (Exception ex) {
            log.error("获取本机IP失败", ex);
        }
        return localIp;
    }

    /**
     * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
     *
     * @param ni 网卡
     * @return boolean
     * @throws SocketException SocketException
     */
    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
        return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                && (ni.getName().startsWith("eth") || ni.getName().startsWith("ens"));
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     *
     * @param address address
     * @return boolean
     */
    private static boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

}
