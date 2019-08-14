package com.central.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.central.core.exception.ServiceException;
import com.central.core.exception.enums.CoreExceptionEnum;
import  com.central.core.autoconfigure.properties.*;
import com.central.core.model.constants.SecurityConstants;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 高频方法集合
 */
@Slf4j
public class ToolUtil extends ValidateUtil {
    /**
     * 默认密码盐长度
     */
    public static final int SALT_LENGTH = 6;

    /**
     * 获取随机字符,自定义长度
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * md5加密(加盐)
     */
    public static String md5Hex(String password, String salt) {
        return md5Hex(password + salt);
    }

    /**
     * md5加密(不加盐)
     */
    public static String md5Hex(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(str.getBytes());
            StringBuffer md5StrBuff = new StringBuffer();
            for (int i = 0; i < bs.length; i++) {
                if (Integer.toHexString(0xFF & bs[i]).length() == 1)
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & bs[i]));
                else
                    md5StrBuff.append(Integer.toHexString(0xFF & bs[i]));
            }
            return md5StrBuff.toString();
        } catch (Exception e) {
            throw new ServiceException(CoreExceptionEnum.ENCRYPT_ERROR);
        }
    }

    /**
     * 过滤掉掉字符串中的空白
     */
    public static String removeWhiteSpace(String value) {
        if (isEmpty(value)) {
            return "";
        } else {
            return value.replaceAll("\\s*", "");
        }
    }

    /**
     * 获取某个时间间隔以前的时间 时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getCreateTimeBefore(int seconds) {
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date date = new Date(currentTimeInMillis - seconds * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取异常的具体信息
     */
    public static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter();
        try {
            e.printStackTrace(new PrintWriter(sw));
        } finally {
            try {
                sw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return sw.getBuffer().toString().replaceAll("\\$", "T");
    }

    /**
     * 获取应用名称
     */
    public static String getApplicationName() {
        try {
            AppNameProperties appNameProperties =
                    SpringUtil.getBean(AppNameProperties.class);
            if (appNameProperties != null) {
                return appNameProperties.getName();
            } else {
                return "";
            }
        } catch (Exception e) {
            log.error("获取应用名称错误！", e);
            return "";
        }
    }

    /**
     * 获取ip地址
     */
    public static String getIP() {
        try {
            StringBuilder IFCONFIG = new StringBuilder();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        IFCONFIG.append(inetAddress.getHostAddress().toString() + "\n");
                    }

                }
            }
            return IFCONFIG.toString();

        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取ip地址错误！", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拷贝属性，为null的不拷贝
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true));
    }

    /**
     * 判断是否是windows操作系统
     */
    public static Boolean isWinOs() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取临时目录
     */
    public static String getTempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 把一个数转化为int
     */
    public static Integer toInt(Object val) {
        if (val instanceof Double) {
            BigDecimal bigDecimal = new BigDecimal((Double) val);
            return bigDecimal.intValue();
        } else {
            return Integer.valueOf(val.toString());
        }

    }

    /**
     * 是否为数字
     */
    public static boolean isNum(Object obj) {
        try {
            Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取项目路径
     */
    public static String getWebRootPath(String filePath) {
        try {
            String path = ToolUtil.class.getClassLoader().getResource("").toURI().getPath();
            path = path.replace("/WEB-INF/classes/", "");
            path = path.replace("/target/classes/", "");
            path = path.replace("file:/", "");
            if (ToolUtil.isEmpty(filePath)) {
                return path;
            } else {
                return path + "/" + filePath;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件后缀名 不包含点
     */
    public static String getFileSuffix(String fileWholeName) {
        if (ToolUtil.isEmpty(fileWholeName)) {
            return "none";
        }
        int lastIndexOf = fileWholeName.lastIndexOf(".");
        return fileWholeName.substring(lastIndexOf + 1);
    }

    /**
     * 判断一个对象是否是时间类型
     */
    public static String dateType(Object o) {
        if (o instanceof Date) {
            return DateUtil.formatDate((Date) o);
        } else {
            return o.toString();
        }
    }

    /**
     * 当前时间
     */
    public static String currentTime() {
        return DateUtil.formatDateTime(new Date());
    }


    /*
    * 获取请求头中的某个参数的值
    * */
    public static String getRequesHeader(String param) {
        HttpServletRequest request = HttpContext.getRequest();
        String result = request.getHeader(param);
        if (!StrUtil.isNotEmpty(result)) {
            result="";
        }
        return result;
    }


    /**
     * 获取requet(head/param)中的token
     * @param request
     * @return
     */
    public static String extractToken(HttpServletRequest request) {
        String token = extractHeaderToken(request);
        if (token == null) {
            token = request.getParameter(SecurityConstants.ACCESS_TOKEN);
            if (token == null) {
                log.error("请求中没发一面Token相关的参数");
            }
        }
        return token;
    }

    /**
     * 解析head中的token
     * @param request
     * @return
     */
    private static String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(SecurityConstants.AUTH_HEADER);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(SecurityConstants.BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(SecurityConstants.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }

}