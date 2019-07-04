package com.study.ftp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FtpApplicationTests {

    @Test
    public void contextLoads() throws IOException {
        FtpUtil ftpUtil = new FtpUtil();
        ftpUtil.fileList();
        UUID uuid = UUID.randomUUID();
        String random = uuid.toString();
        System.out.println(random);
        String remotePath = "/test/A20309.24.rar";

        String localPath = FtpUtil.tempPath + random + remotePath.substring(remotePath.lastIndexOf("/"));

        ftpUtil.downloadFile(localPath, remotePath);

        boolean unrar = ftpUtil.unrar(localPath, FtpUtil.tempPath + random);
        System.out.println(unrar);
//        ftpUtil.readZipFile(localPath);
//        ftpUtil.fileList(ftpClient);
//        ftpUtil.readFile(ftpClient,"/test/A20309.24.rar");

//        System.out.println(ftpClient);


    }

}
