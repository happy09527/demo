package com.example.demo.user.mapper;    //编写接口

import com.example.demo.user.dto.DealMonthAmount;
import com.example.demo.user.dto.MemberCreateTimeDto;
import com.example.demo.user.dto.NetworkLocationDto;
import com.example.demo.user.dto.RechargeMonthAmount;
import com.example.demo.user.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper  //必需
public interface UserMapper {
    @Select("select * from user")    //查看所有
    List<User> findAll();
    @Select("select * from user where cid=#{cid}")
    List<User> cidUser(@Param("cid") String cid);

    @Select("select cid from user where username=#{username}")
    List<String> cidByUsername(@Param("username") String username);

    @Select("select * from user where username=#{username}")
    List<User> find(@Param("username") String username);

    @Select("select * from user where username=#{username} and password=#{password}")
    List<User> findByUandP(@Param("username") String username,@Param("password") String password);

    @Insert("INSERT INTO user (username, password,power, phone, email,cid) VALUES (#{username}, #{password}, #{power}, #{phone}, #{email},#{cid})")       //插入数据，#{}用于动态数据
    boolean insert(User user);

    //@Update("update user set password=#{password},state=#{state},phone=#{phone},email=#{email} where username = #{username}")
    boolean update(User user);

    @Update("update  user set power=#{power} where username = #{username}")
    boolean update_power(@Param("username") String username,@Param("power") String power);

    @Update("update  user set phone=#{phone} where username = #{username}")
    boolean update_phone(@Param("username") String username,@Param("phone") String phone);

    @Update("update  user set email=#{email} where username = #{username}")
    boolean update_email(@Param("username") String username,@Param("email") String email);

    @Delete("delete from user where username=#{username}")
    int deleteByUsername(@Param("username") String username);   //告诉框架参数名，和上面的语句的参数名对应

    //network表
    @Select("select * from network")//查看
    List<Network> networkFind();

    @Select("select * from network where cid = #{cid}")
    List<Network> cidNetwork(@Param("cid") String cid);

    List<Network> networkSearch(@Param("name")  String name,@Param("address") String address,@Param("company") String company,@Param("phone") String phone,@Param("cid") String cid);

    @Select("select * from network where id=#{id}")
    List<Network> networkFindById(@Param("id") String id);

    @Select("select lng,lat from network where cid=#{cid}")
    List<NetworkLocationDto> location(@Param("cid") String cid);


    @Insert("INSERT INTO network (id, name,address, company, phone,state,lng,lat,cid) VALUES (null, #{name}, #{address}, #{company}, #{phone},#{state},#{lng},#{lat},#{cid})")       //插入数据，#{}用于动态数据
    boolean networkInsert(Network network);

    @Update("update network set name=#{name},address=#{address},company=#{company},phone=#{phone},state=#{state},lng=#{lng},lat=#{lat} where id = #{id}")
    boolean networkUpdate(Network network);


    @Delete("delete from network where id=#{id}")
    boolean networkDelete(@Param("id") String id);   //告诉框架参数名，和上面的语句的参数名对应

    //device表
    @Select("select * from device")//查看
    List<Device> deviceFind();

    @Select("select * from device where cid = #{cid}")
    List<Device> cidDevice(@Param("cid") String cid);

    List<Device> deviceSearch(@Param("kind")  String kind,@Param("name") String name,@Param("network_id") String network_id,@Param("cid") String cid);

    @Select("select * from device where id=#{id}")
    List<Device> deviceFindById(@Param("id") String id);


    @Insert("INSERT INTO device (id,kind, name,network_id,state,cid) VALUES (null,#{kind}, #{name}, #{network_id}, #{state},#{cid})")       //插入数据，#{}用于动态数据
    boolean deviceInsert(Device device);

    @Update("update device set kind=#{kind},name=#{name},network_id=#{network_id},state=#{state} where id = #{id}")
    boolean deviceUpdate(Device device);
    @Update("update device set state=#{state} where id=#{id}")
    boolean deviceStateUpdate(@Param("state") String state,@Param("id") String id);

    @Delete("delete from device where id=#{id}")
    boolean deviceDelete(@Param("id") String id);   //告诉框架参数名，和上面的语句的参数名对应

    @Select("select distinct kind from device where cid=#{cid}")
    List<String> deviceKind(@Param("cid") String cid);

    @Select("select COUNT(*) from device where kind=#{kind} and cid=#{cid}")
    int deviceNum(@Param("kind") String kind,@Param("cid") String cid);
    //member
    @Select("select * from member")//查看
    List<Member> memberFind();

    @Select("select * from member where cid = #{cid}")
    List<Member> cidMember(@Param("cid") String cid);

    List<Member> memberSearch(@Param("name")  String name,@Param("phone") String phone,@Param("cid") String cid);

    @Select("select * from member where id=#{id}")
    List<Member> memberFindById(@Param("id") String id);

    @Select("SELECT DATE_FORMAT(created_time,'%m') months,COUNT(id) COUNT FROM member where cid=#{cid} GROUP BY months")
    List<MemberCreateTimeDto> memberMonthNum(@Param("cid") String cid);

    @Insert("INSERT INTO member (id,name,money,phone,cid) VALUES (null, #{name}, #{money}, #{phone},#{cid})")       //插入数据，#{}用于动态数据
    boolean memberInsert(Member member);

    @Update("update member set name=#{name},money=#{money},phone=#{phone} where id = #{id}")
    boolean memberUpdate(Member member);


    @Delete("delete from member where id=#{id}")
    boolean memberDelete(@Param("id") String id);   //告诉框架参数名，和上面的语句的参数名对应


    //deal
    @Select("select * from deal")//查看
    List<Deal> dealFind();

    @Select("select * from deal where cid = #{cid}")
    List<Deal> cidDeal(@Param("cid") String cid);

    List<Deal> dealSearch(@Param("device_id")  String device_id,@Param("member_id") String member_id,@Param("cid") String cid);

    @Select("select * from deal where id=#{id}")
    List<Deal> dealFindById(@Param("id") String id);

    @Select("SELECT DATE_FORMAT(date,'%m') months,sum(amount) monthAmount  FROM deal where cid = #{cid} GROUP BY months")
    List<DealMonthAmount> dealMonthAmount(@Param("cid") String cid);

    @Select("select sum(amount) allAmount from deal where cid=#{cid}")
    int dealAmount(@Param("cid") String cid);

    @Insert("INSERT INTO deal (id,device_id,member_id,amount,cid) VALUES (null, #{device_id}, #{member_id}, #{amount},#{cid})")       //插入数据，#{}用于动态数据
    boolean dealInsert(Deal deal);

    @Update("update deal set device_id=#{device_id},member_id=#{member_id},amount=#{amount} where id = #{id}")
    boolean dealUpdate(Deal deal);


    @Delete("delete from deal where id=#{id}")
    boolean dealDelete(@Param("id") String id);   //告诉框架参数名，和上面的语句的参数名对应
    //recharge
    @Select("select * from recharge")//查看
    List<Recharge> rechargeFind();

    @Select("select * from recharge where cid = #{cid}")
    List<Recharge> cidRecharge(@Param("cid") String cid);

    List<Recharge> rechargeSearch(@Param("member_id")  String member_id,@Param("cid") String cid);

    @Select("select * from recharge where id=#{id}")
    List<Recharge> rechargeFindById(@Param("id") String id);

    @Select("SELECT DATE_FORMAT(date,'%m') months,sum(amount) monthAmount  FROM recharge where cid=#{cid} GROUP BY months")
    List<RechargeMonthAmount> rechargeMonthAmount(@Param("cid") String cid);

    @Select("select sum(amount) allAmount from recharge where cid=#{cid}")
    int rechargeAmount(@Param("cid") String cid);

    @Insert("INSERT INTO recharge (id,member_id,amount,cid) VALUES (null, #{device_id}, #{member_id}, #{amount},#{cid})")       //插入数据，#{}用于动态数据
    boolean rechargeInsert(Recharge recharge);

    @Update("update recharge set member_id=#{member_id},amount=#{amount} where id = #{id}")
    boolean rechargeUpdate(Recharge recharge);


    @Delete("delete from recharge where id=#{id}")
    boolean rechargeDelete(@Param("id") String id);   //告诉框架参数名，和上面的语句的参数名对应

    //Log表
    @Select("select * from log")
    List<Log> allLog();

    @Select("select * from log where cid=#{cid}")
    List<Log> cidLog(@Param("cid") String cid);

    List<Log> logSearch(@Param("type") String type,@Param("cid") String cid);

    @Insert("insert into log (type, message,cid) values (#{type},#{message},#{cid})")
    boolean logInsert(@Param("type") String type,@Param("message") String message,@Param("cid") String cid);
    //Company表
    @Select("select * from company where id = #{id}")
    List<Company> company(@Param("id") String id);
    //Invite表
    @Select("select * from invite where code=#{code}")
    List<Invite> invite(@Param("code") String code);

    @Select("select * from invite where code=#{code}")
    boolean findInvite(@Param("code") String code);
}
