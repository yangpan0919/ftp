package com.study.ftp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class FtpUtil {

    public static String tempPath = "E://tempFiles/";
    public static String pathFile = "E://pathFile";
    public static String winrarPath = "C:\\Program Files\\WinRAR\\WinRAR.exe";

    public static String path = "2019年04月";
    public static String parentPath = "";
    public static String tempPass = "_gsdata_";
    public static String ControlEncoding = "utf-8";
    public static String byteEncoding = "iso-8859-1";
    public static String byteCode = "utf-8";
    public static boolean isByte = false;


    private static Logger logger = Logger.getLogger(FtpUtil.class);
//    String url = "192.168.";
//    String userName = "";
//    String password = "";
    String url = "192.168.";
    String userName = "";
    String password = "";


    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月");
        LocalDateTime now = LocalDateTime.now();  //这一个月
        LocalDateTime of = LocalDateTime.of(2019, 2, 30, 0, 0);
        LocalDateTime ldt = now.minusMonths(1);   //上一个月
        LocalDateTime month2 = of.minusMonths(1);   //上一个月

        System.out.println(ldt.format(dtf));
        System.out.println(now.format(dtf));
        System.out.println(month2.format(dtf));




        try {
            File out = new File("E://lala/数据库表增加字段处理处理流程（不重新生成�?V1-0418.pptx");

//            File out = new File("E://lala/数据库表增加字段处理处理流程（不重新生成�?V1-0418.pptx");   //? 不能被识别
            FileOutputStream o = FileUtils.openOutputStream(out);
            o.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        File file = new File("C:\\Users\\86180\\Desktop\\新建文件夹 (2)_RESULT");
//        File[] files = file.listFiles();
//        for (int i = 0; i < files.length; i++) {
//            File file1 = files[i];
//            try {
//                File out = new File("E://lala/数据库表增加字段处理处理流程（不重新生成�?V1-0418.pptx");
//                FileOutputStream o = FileUtils.openOutputStream(out);
//                o.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//        System.out.println(new FtpUtil().unrar("\"C:\\Users\\86180\\Desktop\\新建文件夹 (4)\\A20309.24.rar\"", "\"C:\\Users\\86180\\Desktop\\新建文件夹 (4)\""));
    }

    public boolean unrar(String localPath, String targetPath) {
        String command = winrarPath + " x " + localPath + " " + targetPath;
        System.out.println(command);
        try {
            Process exec = Runtime.getRuntime().exec(command);
            return exec.waitFor(2, TimeUnit.MINUTES);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String findFile(String name) {
        long l = Instant.now().toEpochMilli();
        FTPClient ftpClient = connectFtpServer();
        List<String> list = new ArrayList<>();
        try {
            findFile("/" + path + "/", name, ftpClient, list);
            long l1 = Instant.now().toEpochMilli();
            logger.info("找文件耗时为：" + (l1 - l));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list.toString();
    }

    public FTPClient connectFtpServer() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(1000 * 30);//设置连接超时时间

        ftpClient.setControlEncoding(ControlEncoding);//设置ftp字符集

        ftpClient.enterLocalPassiveMode();//设置被动模式，文件传输端口设置
        try {
            if (isByte) {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//设置文件传输模式为二进制，可以保证传输的内容不会被改变
            }

            ftpClient.connect(url);
            ftpClient.login(userName, password);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.info("connect ftp " + url + " failed");
                ftpClient.disconnect();
                return null;
            }
            logger.info("replyCode===========" + replyCode);
        } catch (IOException e) {
            logger.error("ftp连接错误", e);
            return null;
        }
        return ftpClient;
    }

    public void downloadFile(String localPath, String remotePath) {
        FTPClient ftpClient = connectFtpServer();
        File file = new File(localPath);
        OutputStream os = null;
        try {
            os = FileUtils.openOutputStream(file);
            ftpClient.retrieveFile(remotePath, os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient != null) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<String> arFiles = new ArrayList<>();
    public ArrayList<String> nameList = new ArrayList<>();
    public int num = 0;

    public void fileList() {
        FTPClient ftpClient = connectFtpServer();
        try {
            long l = Instant.now().toEpochMilli();
            System.out.println(path);
            listTemp("/" + path + "/", null, ftpClient);
            long l1 = Instant.now().toEpochMilli();
            logger.info("遍历耗时：" + (l1 - l));
            logger.info("所需文件个数：" + arFiles.size());
            logger.info("总文件个数：" + num);
            Set<String> set = new HashSet<>();
            Map<String, List<String>> map = new HashMap<>();
            for (int i = 0; i < nameList.size(); i++) {
                int size = set.size();
                String s = nameList.get(i);
                s = s.substring(0, s.lastIndexOf(".") + 1);
                set.add(s);
                if (set.size() == size) {
                    map.get(s).add("文件名相同:" + nameList.get(i) + ";文件路径:" + arFiles.get(i));
                } else {
                    String temp = "文件名相同:" + nameList.get(i) + ";文件路径:" + arFiles.get(i);
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(temp);
                    map.put(s, strings);
                }
            }
            Set<String> strings = map.keySet();
            for (String string : strings) {
                if (map.get(string).size() > 1) {
                    logger.info(map.get(string));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
//
//    public void findFile(FTPFile ftpFile) {
//        if (ftpFile.isDirectory()) {
//            ftpFile.
//        }
//
//    }

    /**
     * 递归遍历目录下面指定的文件名
     *
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @param ext      文件的扩展名
     * @throws IOException
     */
    public void listTemp(String pathName, String ext, FTPClient ftpClient) throws IOException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            //更换目录到当前目录
            ftpClient.changeWorkingDirectory(pathName);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    num++;
                    if (file.getName().endsWith(".zip") || file.getName().endsWith(".rar")) {
                        arFiles.add(pathName + file.getName());
                        nameList.add(file.getName());
                    }
                } else if (file.isDirectory()) {
                    if (!".".equals(file.getName()) && !"..".equals(file.getName())) {
                        listTemp(pathName + file.getName() + "/", ext, ftpClient);
                    }
                }
            }
        }
    }


    /**
     * 递归遍历出目录下面所有文件
     *
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @throws IOException
     */
    public void list(String pathName, FTPClient ftpClient) throws IOException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            //更换目录到当前目录
            ftpClient.changeWorkingDirectory(pathName);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    arFiles.add(pathName + file.getName());
                } else if (file.isDirectory()) {
                    // 需要加此判断。否则，ftp默认将‘项目文件所在目录之下的目录（./）’与‘项目文件所在目录向上一级目录下的目录（../）’都纳入递归，这样下去就陷入一个死循环了。需将其过滤掉。
                    if (!".".equals(file.getName()) && !"..".equals(file.getName())) {
                        list(pathName + file.getName() + "/", ftpClient);
                    }
                }
            }
        }
    }

    /**
     * 递归遍历目录下面指定的文件名
     *
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @param ext      文件的扩展名
     * @throws IOException
     */
    public void list(String pathName, String ext, FTPClient ftpClient) throws IOException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            //更换目录到当前目录
            ftpClient.changeWorkingDirectory(pathName);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    if (file.getName().endsWith(ext)) {
                        arFiles.add(pathName + file.getName());
                    }
                } else if (file.isDirectory()) {
                    if (!".".equals(file.getName()) && !"..".equals(file.getName())) {
                        list(pathName + file.getName() + "/", ext, ftpClient);
                    }
                }
            }
        }
    }

    /**
     * 递归遍历目录下面指定的文件名
     *
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @param pre      文件名前部分
     * @throws IOException
     */
    public String findFile(String pathName, String pre, FTPClient ftpClient, List<String> list) throws IOException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            System.out.println(pathName);
            //更换目录到当前目录
            ftpClient.changeWorkingDirectory(pathName);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    if (file.getName().startsWith(pre) && (file.getName().endsWith(".rar") || file.getName().endsWith(".zip"))) {
                        String s = pathName + file.getName();
                        list.add(s);
                    }
                } else if (file.isDirectory()) {
                    if (!".".equals(file.getName()) && !"..".equals(file.getName()) && !tempPass.equals(file.getName())) {
                        findFile(pathName + file.getName() + "/", pre, ftpClient, list);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 递归遍历目录下面指定的文件名
     *
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @param pre      文件名前部分
     * @throws IOException
     */
    public void listPre(String pathName, String pre, FTPClient ftpClient) throws IOException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            System.out.println(pathName);
            //更换目录到当前目录
            ftpClient.changeWorkingDirectory(pathName);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    if (file.getName().startsWith(pre)) {
                        arFiles.add(pathName + file.getName());
                    }
                } else if (file.isDirectory()) {
                    if (!".".equals(file.getName()) && !"..".equals(file.getName())) {
                        listPre(pathName + file.getName() + "/", pre, ftpClient);
                    }
                }
            }
        }
    }

    public void readZipFile(String localPath) {
        //获取待读文件输入流
        InputStream inputStream = null;
        BufferedReader br = null;
        try {
            inputStream = FileUtils.openInputStream(new File(localPath));
            InputStream in = new BufferedInputStream(inputStream);
            ZipInputStream zin = new ZipInputStream(in);
//            ZipEntry nextEntry = zin.getNextEntry();
//            System.out.println(nextEntry);
            ZipEntry ze;
            int i = 0;
            while ((ze = zin.getNextEntry()) != null) {
                System.out.println(++i);
                if (ze.isDirectory()) {
                } else {
                    System.err.println("file - " + ze.getName() + " : "
                            + ze.getSize() + " bytes");
                    long size = ze.getSize();
                    if (size > 0) {
//                        BufferedReader br = new BufferedReader(
//                                new InputStreamReader(zf.getInputStream(ze)));
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }

                    }
                    System.out.println();
                }
            }
            zin.closeEntry();


            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String tmpString;
            while ((tmpString = br.readLine()) != null) {
                System.out.println(tmpString);
            }

        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void ceshi() {
        FTPClient ftpClient = connectFtpServer();
        String temp;
        if (StringUtils.isEmpty(parentPath)) {
            temp = "/";
        } else {
            temp = "/" + parentPath + "/";
        }
        try {
            ftpClient.changeWorkingDirectory(temp);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (int i = 0; i < ftpFiles.length; i++) {
                FTPFile ftpFile = ftpFiles[i];
                lala(ftpFile, ftpClient, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lala(FTPFile ftpFile, FTPClient ftpClient, int count, String path) {
        String name = ftpFile.getName();
        try {
            if (isByte) {
                name = new String(ftpFile.getName().getBytes(byteEncoding), byteCode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (ftpFile.isDirectory()) {
            if (".".equals(name) || "..".equals(name)) {
                return;
            }
            if (tempPass.equals(name)) {
                return;
            }
            count++;
            String pre = "";
            for (int i = 1; i < count; i++) {
                pre = pre + "  ";
            }
            logger.info(pre + name);
            try {
                ftpClient.changeWorkingDirectory(path + name + "/");
                FTPFile[] ftpFiles = ftpClient.listFiles();
                for (int i = 0; i < ftpFiles.length; i++) {
                    lala(ftpFiles[i], ftpClient, count, path + name + "/");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File out = new File(pathFile + path + name);   //文件名中包含? 无法识别
            if (out.exists()) {
                return;
            }
            //文件不存在,添加记录进数据库
//            logger.info(pathFile + path + name);
            FileOutputStream o = null;
            try {
                o = FileUtils.openOutputStream(out);
            } catch (IOException e) {
                logger.error("输出文件错误", e);
            } finally {
                if (o != null) {
                    try {
                        o.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
//            System.out.println(name);
        }
    }

    public void timeTask(Runnable task) {
        ScheduledThreadPoolExecutor stp = new ScheduledThreadPoolExecutor(1);
        stp.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);
    }
//    public void ceshi1() {
//        FTPClient ftpClient = connectFtpServer();
//        try {
//            ftpClient.changeWorkingDirectory("/");
//            FTPFile[] ftpFiles = ftpClient.listFiles();
//            for (int i = 0; i < ftpFiles.length; i++) {
//                FTPFile ftpFile = ftpFiles[i];
//                lala1(ftpFile, ftpClient);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void lala1(FTPFile ftpFile, FTPClient ftpClient) {
//        if (ftpFile.isDirectory()) {
//            System.out.println(ftpFile.getName());
//            try {
//                ftpClient.changeWorkingDirectory(ftpFile.getName());
//                FTPFile[] ftpFiles = ftpClient.listFiles();
//                for (int i = 0; i < ftpFiles.length; i++) {
//                    FTPFile ftpFile1 = ftpFiles[i];
//                    if (ftpFile1.isDirectory()) {
//                        System.out.println("  " + ftpFile1.getName());
//                        ftpClient.changeWorkingDirectory(ftpFile1.getName());
//                        FTPFile[] ftpFiles = ftpClient.listFiles();
//
//
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
////            System.out.println(ftpFile.getName());
//        }
//    }

}
