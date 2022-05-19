package com.example.demo.user.mapper;

import com.example.demo.user.entity.Files;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("insert into file (id,name, type, size, url ,md5,cid) values (null,#{name},#{type},#{size},#{url},#{md5},#{cid})")
    boolean fileInsert(Files file);

    @Select("select * from file where md5 = #{md5}")
    List<Files> fileMd5(@Param("md5") String md5);


    @Select("select * from  file where id=#{id}")
    List<Files> fileSearchById(@Param("id") String id);

    @Select("select * from file")
    List<Files> allFile();

    @Select("select  * from file where cid=#{cid}")
    List<Files> fileSearchByCid(@Param("cid") String cid);

    @Select("select  * from file where type='mp4' and cid = #{cid}")
    List<Files> videoFile(@Param("cid") String cid);

    List<Files> fileSearch(@Param("name") String name,@Param("cid") String cid);

    @Delete("delete from file where id=#{id}")
    boolean fileDelete(@Param("id") String id);
}
