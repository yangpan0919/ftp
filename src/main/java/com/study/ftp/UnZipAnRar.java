package com.study.ftp;

//import com.github.junrar.Archive;
//import com.github.junrar.exception.RarException;
//import com.github.junrar.rarfile.FileHeader;
//import org.apache.tools.zip.ZipEntry;
//import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

public class UnZipAnRar {

    public static void unZip(File zipFile, String outDir) throws IOException {

        File outFileDir = new File(outDir);
        if (!outFileDir.exists()) {
            boolean isMakDir = outFileDir.mkdirs();
            if (isMakDir) {
                System.out.println("创建压缩目录成功");
            }
        }
//
//        ZipFile zip = new ZipFile(zipFile);
//        for (Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements(); ) {
//            ZipEntry entry = (ZipEntry) enumeration.nextElement();
//            String zipEntryName = entry.getName();
//            InputStream in = zip.getInputStream(entry);
//
//            if (entry.isDirectory()) {      //处理压缩文件包含文件夹的情况
//                File fileDir = new File(outDir + zipEntryName);
//                fileDir.mkdir();
//                continue;
//            }
//
//            File file = new File(outDir, zipEntryName);
//            file.createNewFile();
//            OutputStream out = new FileOutputStream(file);
//            byte[] buff = new byte[1024];
//            int len;
//            while ((len = in.read(buff)) > 0) {
//                out.write(buff, 0, len);
//            }
//            in.close();
//            out.close();
//        }
    }

    public static void unRar(File rarFile, String outDir) {
        File outFileDir = new File(outDir);
        if (!outFileDir.exists()) {
            boolean isMakDir = outFileDir.mkdirs();
            if (isMakDir) {
                System.out.println("创建压缩目录成功");
            }
        }
        FileInputStream fis = null;
        FileOutputStream os = null;
//        Archive archive = null;
//        try {
//            fis = new FileInputStream(rarFile);
//            archive = new Archive(fis);
//            FileHeader fileHeader = archive.nextFileHeader();
//            while (fileHeader != null) {
//                if (fileHeader.isDirectory()) {
//                    fileHeader = archive.nextFileHeader();
//                    continue;
//                }
//                File out = new File(outDir + fileHeader.getFileNameString());
//                if (!out.exists()) {
//                    if (!out.getParentFile().exists()) {
//                        out.getParentFile().mkdirs();
//                    }
//                    out.createNewFile();
//                }
//                os = new FileOutputStream(out);
//                archive.extractFile(fileHeader, os);
//                fileHeader = archive.nextFileHeader();
//            }
//        } catch (RarException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if(fis!=null){
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(archive!=null){
//                try {
//                    archive.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(os!=null){
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//

//        }
    }
}
