package com.study.ftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class Controller {


    @Autowired
    FtpUtil ftpUtil;


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        ftpUtil.fileList();
        return "sucess";
    }


    @RequestMapping(value = "/lala/{id}", method = RequestMethod.GET)
    public String lala(@PathVariable("id") String id) {

        FtpUtil.path = id;
        return "修改成功";
//        confQuAnswerService.test();
    }

    @RequestMapping(value = "/lala2/{id}", method = RequestMethod.GET)
    public String lala2(@PathVariable("id") String id) {

        if ("1".equals(id)) {
            FtpUtil.parentPath = "";
        } else {
            FtpUtil.parentPath = id;
        }

        return "修改成功:" + FtpUtil.parentPath;
//        confQuAnswerService.test();
    }

    @RequestMapping(value = "/lala3/{id}", method = RequestMethod.GET)
    public String lala3(@PathVariable("id") String id) {

        FtpUtil.tempPass = id;
        return "修改成功:" + FtpUtil.tempPass;
//        confQuAnswerService.test();
    }

    @RequestMapping(value = "/encode/{id}", method = RequestMethod.GET)
    public String encode(@PathVariable("id") String id) {
        FtpUtil.ControlEncoding = id;
        return "修改成功:" + FtpUtil.ControlEncoding;
    }

    @RequestMapping(value = "/byteCode/{id}", method = RequestMethod.GET)
    public String byteCode(@PathVariable("id") String id) {
        FtpUtil.byteCode = id;
        return "修改成功:" + FtpUtil.byteCode;
    }

    @RequestMapping(value = "/byteEnco/{id}", method = RequestMethod.GET)
    public String byteEncoding(@PathVariable("id") String id) {
        FtpUtil.byteEncoding = id;
        return "修改成功:" + FtpUtil.byteEncoding;
    }

    @RequestMapping(value = "/isByte/{id}", method = RequestMethod.GET)
    public String isByte(@PathVariable("id") String id) {
        if ("1".equals(id)) {
            FtpUtil.isByte = true;
        } else {
            FtpUtil.isByte = false;
        }

        return "修改成功:" + FtpUtil.isByte;
    }


    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public String find(@PathVariable("id") String id) {

        String file = ftpUtil.findFile(id);
        return file;
//        confQuAnswerService.test();
    }

    @RequestMapping(value = "/ceshi", method = RequestMethod.GET)
    public String ceshi() {
        long l = Instant.now().toEpochMilli();
        ftpUtil.ceshi();
        long l1 = Instant.now().toEpochMilli();
        return "测试目录结构,耗时：" + (l1 - l);
//        confQuAnswerService.test();
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public String task() {
        ftpUtil.timeTask(() -> {
            System.out.println("定时任务");
        });

        return "定时任务开始";
//        confQuAnswerService.test();
    }

}
