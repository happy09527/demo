package com.example.demo.user.controller;

import com.example.demo.user.client.EmqClient;
import com.example.demo.user.dto.*;
import com.example.demo.user.entity.*;
import com.example.demo.user.enums.QosEnum;
import com.example.demo.user.mapper.UserMapper;
import com.example.demo.user.properties.MqttProperties;
import com.example.demo.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

    @RestController   //表示返回数据
    @RequestMapping("/user")   //统一给接口加前缀  访问地址为@RequestMapping   +  @GetMapping/@PostMapping
public class UserController {
    @Autowired     //注入其他类
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private EmqClient emqClient;

    @Autowired
    private MqttProperties mqttProperties;

    @PostMapping("/insert")    //新增用户账号
    public boolean insert(@RequestBody UserDto user){
        User u  = new User();
        u.setPower("2");
        u.setEmail("0");
        u.setCid(user.getCid());
        u.setPassword(user.getPassword());
        u.setUsername(user.getUsername());
        u.setPhone("0");
        return userMapper.insert(u);
    }


    //获取数据，必须和@RestController对应;
    @PostMapping("/change")   //用于修改用户信息
    public boolean change(@RequestBody User user){  //@RequestBody用于将前端传入的JSON数据转变为User对象
        return userService.change(user);  //向数据库插入,更新数据
    }
    @GetMapping("/find")   //通过账号查询
    public List<User> find(@RequestParam String username){
        return  userMapper.find(username);
    }

    @GetMapping("/cidByUsername")   //通过账号查询账号所属组织编号
    public List<String> cidByUsername(@RequestParam String username){return userMapper.cidByUsername(username);}

    //用于登录的接口
    @PostMapping("/login")
    public UserDto login(@RequestBody UserDto user){   //UserDto类，用于登录的类
        return userService.login(user);
    }
    @PostMapping("/register")       //用于注册的接口
    public UserDto register(@RequestBody UserDto user){
        return userService.register(user);
    }

    @PostMapping("/updatePower")   //修改用户权限
    public boolean update_power(@RequestBody PowerDto powerDto){
        return userService.update_power(powerDto);
    }
    @PostMapping("/updateMessage")    //修改用户信息
    public boolean update_message(@RequestBody MessageDto message){
        return userService.update_message(message);
    }

    @GetMapping("/findByUandP")   //通过用户账号和密码查询用户
    public List<User> findByUandP(@RequestParam String username,@RequestParam String password){
        return  userMapper.findByUandP(username,password);
    }

    @GetMapping("/findAll")     //查询所有用户账号
    public List<User> findAll(){
        return userMapper.findAll();
    }

    @GetMapping("/cidUser")   //查询某一所属组织的所有用户账号
    public List<User> cidUser(@RequestParam String cid){return userMapper.cidUser(cid);}

    @DeleteMapping("/{username}")   //删除数据,带参数的路径，和下面的参数名对应,接口路径：/user/+参数
    public int delete(@PathVariable String username){
        return userMapper.deleteByUsername(username);
    }



    //network表
    @GetMapping("/networkFind")        //查询所有网点信息记录
    public List<Network> networkFind(){
        return userMapper.networkFind();
    }
    @GetMapping("/networkFindById")   //通过网点编号查询网点信息记录
    public  List<Network> networkFindById(@RequestParam String id){
        return userMapper.networkFindById(id);
    }

    @GetMapping("/cidNetwork")   //查询某一所属组织所有的网点信息记录
    public List<Network> cidNetwork(@RequestParam String cid){return userMapper.cidNetwork(cid);}

    @GetMapping("/networkNum")      //查询某一所属组织所有的网点数量
    public int networkNum(@RequestParam String cid){
        int num = userMapper.cidNetwork(cid).size();
        return num;
    }

    @GetMapping("/location")    //查询某一所属组织所有的网点经纬度
    public List<NetworkLocationDto> location(@RequestParam String cid){return userMapper.location(cid);}

    @PostMapping("/networkInsert")   //新增网点信息记录
    public boolean networkInsert(@RequestBody Network network){
        return userService.networkInsert(network);
    }
    @PostMapping("/networkUpdate")   //修改网点信息记录
    public boolean networkUpdate(@RequestBody Network network){
        return userService.networkUpdate(network);
    }
    @GetMapping("/networkDelete")   //删除网点信息记录
    public boolean networkDelete(@RequestParam String id){
        return userMapper.networkDelete(id);
    }

    @PostMapping("/networkSearch")   //搜索网点信息记录
    public List<Network> networkSearch(@RequestBody Network network){
        return userMapper.networkSearch(network.getName(),network.getAddress(),network.getCompany(),network.getPhone(),network.getCid());
    }
    @GetMapping("/networkMulDelete")     //批量删除网点信息记录
    public int networkMulDelete(@RequestParam String network_id){
        String[] ids= network_id.split(",");
        List<String>  id = Arrays.asList(ids);
        return userService.networkMulDelete(id);
    }

    //device表
    @GetMapping("/deviceFind")   //查询所有设备信息记录
    public List<Device> deviceFind(){
        return userMapper.deviceFind();
    }
    @GetMapping("/deviceFindById")  //通过设备编号查询设备信息
    public  List<Device> deviceFindById(@RequestParam String id){
        return userMapper.deviceFindById(id);
    }

    @GetMapping("/cidDevice")    //根据所属编号查询设备
    public List<Device> cidDevice(@RequestParam String cid){return userMapper.cidDevice(cid);}

    @GetMapping("/deviceNum")  //获取某一所属公司设备数
    public int deviceNum(@RequestParam String cid){
        int num = userMapper.cidDevice(cid).size();
        return num;
    }
    @PostMapping("/deviceInsert")     //新增设备信息
    public boolean deviceInsert(@RequestBody Device device){
        return userService.deviceInsert(device);
    }
    @PostMapping("/deviceUpdate")    //修改设备信息
    public boolean deviceUpdate(@RequestBody Device device){
        return userService.deviceUpdate(device);
    }
    @GetMapping("/deviceDelete")      //删除设备信息
    public boolean deviceDelete(@RequestParam String id){
        return userMapper.deviceDelete(id);
    }
    @GetMapping("/deviceStateUpdate")   //修改设备状态
    public boolean deviceStateUpdate(@RequestParam String state,@RequestParam String id){
        return userMapper.deviceStateUpdate(state,id);
    }

    @PostMapping("/deviceSearch")    //搜索设备
    public List<Device> deviceSearch(@RequestBody Device device){
        return userMapper.deviceSearch(device.getKind(),device.getName(),device.getNetwork_id(),device.getCid());
    }
    @GetMapping("/deviceMulDelete")   //批量删除设备
    public int deviceMulDelete(@RequestParam String device_id){
        String[] ids= device_id.split(",");
        List<String>  id = Arrays.asList(ids);
        return userService.deviceMulDelete(id);
    }

    @GetMapping("/deviceKind")   //获取设备种类数据
    public List<DeviceKindDto> deviceKind(@RequestParam String cid){
        List<DeviceKindDto> deviceKindDtos = new ArrayList<>();
        List<String> kind = userMapper.deviceKind(cid);
        for(String k : kind){
            DeviceKindDto deviceKindDto = new DeviceKindDto();
            deviceKindDto.setName(k);
            deviceKindDto.setValue(userMapper.deviceNum(k,cid));
            deviceKindDtos.add(deviceKindDto);
        }
        return deviceKindDtos;
    }

    //member
    @GetMapping("/memberFind")    //获取所有会员信息
    public List<Member> memberFind(){
        return userMapper.memberFind();
    }
    @GetMapping("/memberFindById")   //通过会员编号查询会员信息
    public  List<Member> memberFindById(@RequestParam String id){
        return userMapper.memberFindById(id);
    }

    @GetMapping("/cidMember")    //查询某一所属组织的所有会员信息
    public  List<Member> cidMember(@RequestParam String cid){return userMapper.cidMember(cid);}

    @GetMapping("/memberNum")   //获取某一所属组织的会员数
    public int memberNum(@RequestParam String cid){
        int num = userMapper.cidMember(cid).size();
        return num;
    }

    @PostMapping("/memberSearch")    //搜索会员信息
    public List<Member> memberSearch(@RequestBody Member member){
        return userMapper.memberSearch(member.getName(),member.getPhone(),member.getCid());
    }

    @PostMapping("/memberUpdate")     //修改会员信息
    public boolean memberUpdate(@RequestBody Member member){
        return userService.memberUpdate(member);
    }

    @GetMapping("/memberMonthNum")   //获取每月注册会员数据
    public Map<String, Object> memberMonthNum(@RequestParam String cid){
        Map<String, Object> map = new HashMap<>();
        List<MemberCreateTimeDto> memberCreateTimeDtos = userMapper.memberMonthNum(cid);
        List<String> month = new ArrayList<>();
        List<Integer> num = new ArrayList<>();
        for(MemberCreateTimeDto mct : memberCreateTimeDtos){
            month.add(mct.getMonths());
            num.add(mct.getCount());
        }
        map.put("x",month);
        map.put("y",num);
        return map;
    }
    //deal
    @GetMapping("/dealFind")     //查询所有消费信息记录
    public List<Deal> dealFind(){
        return userMapper.dealFind();
    }
    @GetMapping("/dealFindById")    //通过编号查询消费信息记录
    public  List<Deal> dealFindById(@RequestParam String id){
        return userMapper.dealFindById(id);
    }

    @GetMapping("/cidDeal")    //查询某一组织所有消费信息记录
    public List<Deal> cidDeal(@RequestParam String cid){return userMapper.cidDeal(cid);}

    @GetMapping("/dealAmount")    //查询某一组织所有消费金额数据
    public int dealAmount(@RequestParam String cid){
        return userMapper.dealAmount(cid);
    }

    @GetMapping("/dealMonthAmount")   //查询某一组织每月消费金额数据
    public Map<String,Object> dealMonthAmount(@RequestParam String cid){
        Map<String,Object> map = new HashMap<>();
        List<DealMonthAmount> dealMonthAmounts = userMapper.dealMonthAmount(cid);
        List<String> month = new ArrayList<>();
        List<Integer> amount = new ArrayList<>();
        for(DealMonthAmount dma : dealMonthAmounts){
            month.add(dma.getMonths());
            amount.add(dma.getMonthAmount());
        }
        map.put("x",month);
        map.put("y",amount);
        return map;
    }

    @PostMapping("/dealSearch")   //搜索消费信息记录
    public List<Deal> dealSearch(@RequestBody Deal deal){
        return userMapper.dealSearch(deal.getDevice_id(),deal.getMember_id(),deal.getCid());
    }
    //recharge
    @GetMapping("/rechargeFind")   //查询所有充值信息记录
    public List<Recharge> rechargeFind(){
        return userMapper.rechargeFind();
    }
    @GetMapping("/rechargeFindById")   //通过编号查询充值信息记录
    public  List<Recharge> rechargeFindById(@RequestParam String id){
        return userMapper.rechargeFindById(id);
    }

    @GetMapping("/cidRecharge")    //查询某一所属组织所有充值信息记录
    public List<Recharge> cidRecharge(@RequestParam String cid){return userMapper.cidRecharge(cid);}

    @GetMapping("/rechargeAmount")    //获取某一所属组织所有充值金额数据
    public int rechargeAmount(@RequestParam String cid){
        return userMapper.rechargeAmount(cid);
    }

    @GetMapping("/rechargeMonthAmount")    //获取某一所属组织每月充值金额数据
    public Map<String,Object> rechargeMonthAmount(@RequestParam String cid){
        Map<String,Object> map = new HashMap<>();
        List<RechargeMonthAmount> rechargeMonthAmounts = userMapper.rechargeMonthAmount(cid);
        List<String> month = new ArrayList<>();
        List<Integer> amount = new ArrayList<>();
        for(RechargeMonthAmount rma : rechargeMonthAmounts){
            month.add(rma.getMonths());
            amount.add(rma.getMonthAmount());
        }
        map.put("x",month);
        map.put("y",amount);
        return map;
    }
    @PostMapping("/rechargeSearch")    //搜索充值信息记录
    public List<Recharge> rechargeSearch(@RequestBody Recharge recharge){
        return userMapper.rechargeSearch(recharge.getMember_id(),recharge.getCid());
    }
    //Verification
    @GetMapping("/Ver")        //随机生成四位验证码
    public List<Character> Verification(){
        String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz0123456789";
        List<Character> ver = new ArrayList<>();
        for (int i=0;i<4;i++)
            ver.add(chars.charAt((int) Math.floor(Math.random() * chars.length())));
        return ver;
    }
    //log表
    @GetMapping("/log")          //查询所有设备通信记录
    List<Log> allLog(){return userMapper.allLog();}

    @GetMapping("/cidLog")    //查询某一所属组织所有的设备通信记录
    List<Log> cidLog(@RequestParam String cid){return userMapper.cidLog(cid);}
    @GetMapping("/open")     //打开设备
    boolean open(@RequestParam String message,@RequestParam String cid){
        emqClient.publish("open/123",message, QosEnum.Qos2,false);
        return userMapper.logInsert("operation",message,cid);
    }
    @GetMapping("/close")    //关闭设备
    boolean close(@RequestParam String message,@RequestParam String cid){
        emqClient.publish("close/123",message, QosEnum.Qos2,false);
        return userMapper.logInsert("operation",message,cid);
    }

    @GetMapping("/logSearch")    //搜索设备通信记录
    List<Log> logSearch(@RequestParam String type,@RequestParam String cid){return userMapper.logSearch(type,cid);}

    //Company
    @GetMapping("/company")      //根据所属组织编号查询所属组织名称
    List<Company> company(@RequestParam String id){return userMapper.company(id);}
    //Invite
    @GetMapping("/invite")       // 根据邀请码查询所属组织
    List<Invite> invite(@RequestParam String code){
        return userMapper.invite(code);
    }
    @GetMapping("/findInvite")
    boolean findInvite(@RequestParam String code){return userMapper.findInvite(code);}
}
