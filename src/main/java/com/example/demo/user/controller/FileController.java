package com.example.demo.user.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.demo.user.entity.Files;
import com.example.demo.user.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Autowired
    private FileMapper fileMapper;
    //上传的接口
    @PostMapping("/upload")
    public  String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename).toLowerCase();
        long size =  file.getSize();
        //先存储到磁盘
        File uploadParentFile = new File(fileUploadPath);
        if(!uploadParentFile.exists()){
            uploadParentFile.mkdir();
        }
        //定义文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID()+ StrUtil.DOT +type;
        File uploadFile = new File(fileUploadPath + uuid);
        String url;
        String md5;
        file.transferTo(uploadFile);
        //文件md5,查询md5是否存在,防止上传重复文件
            md5 = SecureUtil.md5(uploadFile);
            List<Files> files = fileMapper.fileMd5(md5);
            if(!files.isEmpty()){
                uploadFile.delete();
                return "error";
            }else {
                //把获取到的文件存储到磁盘目录中
                url = "http://localhost:8090/file/"+uuid;
            }
        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        saveFile.setCid("1");
        fileMapper.fileInsert(saveFile);
        return url;
    }

    @GetMapping("/{uuid}")  //下载的接口
    public void downLoad(@PathVariable String uuid, HttpServletResponse response) throws IOException {
        File downLoadFile = new File(fileUploadPath + uuid);
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(uuid,"UTF-8"));
        response.setContentType("application/octet-stream");
        os.write(FileUtil.readBytes(downLoadFile));
        os.flush();
        os.close();
    }

    @GetMapping("/allFile")   //查询所有文件
    public List<Files> allFile(@RequestParam String cid){
            return fileMapper.fileSearchByCid(cid);
    }

    @GetMapping("fileById")   //使用文件编号查询文件
    public Files fileById(@RequestParam String id){return fileMapper.fileSearchById(id).get(0);}


    @GetMapping("/videoFile")   //查询所有播放文件
    public List<Files> videoFile(@RequestParam String cid){return fileMapper.videoFile(cid);}
    @GetMapping ("/fileSearch")   //搜索文件
    public List<Files> fileSearch(@RequestParam String name,@RequestParam String cid){return fileMapper.fileSearch(name,cid);}

    @GetMapping("/fileDelete")    //删除文件
    public boolean fileDelete(@RequestParam String id){
        List<Files> files = fileMapper.fileSearchById(id);
        String name = files.get(0).getUrl().substring(27);
        System.out.println(name);
        if(fileMapper.fileDelete(id)==true){
            File file = new File(fileUploadPath+name);
            file.delete();
            return true;
        }else {
            return false;
        }
    }
}
