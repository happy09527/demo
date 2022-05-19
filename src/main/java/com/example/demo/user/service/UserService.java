package com.example.demo.user.service;

import com.example.demo.user.dto.MessageDto;
import com.example.demo.user.dto.PowerDto;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.entity.Device;
import com.example.demo.user.entity.Member;
import com.example.demo.user.entity.Network;
import com.example.demo.user.entity.User;
import com.example.demo.user.mapper.UserMapper;
import com.example.demo.user.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public boolean change(User user){     //修改用户信息
        if(userMapper.find(user.getUsername()).isEmpty()){
            return userMapper.insert(user);
        }else {
            return userMapper.update(user);
        }
    }

    public UserDto login(UserDto user) {    //账号登录
        String username = user.getUsername();
        String password = user.getPassword();
        if(username.isEmpty() ||username==null || password==null || password.isEmpty()){
            user.setToken(null);
            return user;
        }
        if(userMapper.findByUandP(username,password).isEmpty()){
            user.setToken(null);
            return user;
        }else{
            String token = TokenUtils.genToken(user.getUsername(),user.getPassword());
            user.setToken(token);
            return user;
        }
    }

    public  UserDto register(UserDto user){     //账号注册
        String username = user.getUsername();
        String password = user.getPassword();
        String cid = user.getCid();
        if(username.isEmpty() || password.isEmpty()){
            user.setToken(null);
            return user;
        }
        if(userMapper.find(username).isEmpty()){
            User u = new User();
            u.setUsername(username);
            u.setPassword(password);
            u.setPower("2");
            u.setPhone("无");
            u.setEmail("无");
            u.setCid(cid);
            if(userMapper.insert(u)){
                String token = TokenUtils.genToken(user.getUsername(),user.getPassword());
                user.setToken(token);
                return user;
            }
        }
        user.setToken(null);
        return user;
    }

    public boolean update_power(PowerDto powerDto){      //修改用户权限
        if(powerDto.getPower().isEmpty()!=true && powerDto.getPower().length()==1){
            return userMapper.update_power(powerDto.getUsername(),powerDto.getPower());
        }else
            return false;
    }

    public boolean update_message(MessageDto message){      //修改用户邮箱和电话
        boolean p=false,e=false;
            if(message.getPhone()!=null && message.getPhone().isEmpty()!=true){
                p = userMapper.update_phone(message.getUsername(),message.getPhone());
            }
            if(message.getEmail()!=null && message.getEmail().isEmpty()!=true){
                e = userMapper.update_email(message.getUsername(),message.getEmail());
            }
            if(p || e){
                return true;
            }else
                return false;
    }
        //network表
    public boolean networkInsert(Network network) {      //新增网点信息
        network.setState("正常运营");
        return userMapper.networkInsert(network);
    }
    public boolean networkUpdate(Network network){    //修改网点信息
        if(network.getId()!=null && network.getId().isEmpty()!=true){
            List<Network> n = userMapper.networkFindById(network.getId());
            if(n.isEmpty())
                return false;
            if(network.getName().isEmpty()){
                network.setName(n.get(0).getName());
            }
            if(network.getAddress().isEmpty()){
                network.setAddress(n.get(0).getAddress());
            }
            if(network.getCompany().isEmpty()){
                network.setCompany(n.get(0).getAddress());
            }
            if(network.getPhone().isEmpty()){
                network.setPhone(n.get(0).getPhone());
            }
            if(network.getLng()==0){
                network.setLng(n.get(0).getLng());
            }
            if(network.getLat()==0){
                network.setLat(n.get(0).getLat());
            }
            network.setState(n.get(0).getState());
            return userMapper.networkUpdate(network);
        }else
            return false;
    }

    public int networkMulDelete(List<String> ids) {     //批量删除网点信息
        int total=0;
        for (String id : ids) {
            if(userMapper.networkDelete(id)){
                total++;
            }
        }
        return total;
    }
    //device
    public boolean deviceInsert(Device device) {         //新增设备信息
        device.setState("关闭");
        return userMapper.deviceInsert(device);
    }

    public boolean deviceUpdate(Device device){     //修改设备信息
        if(device.getId()!=null && device.getId().isEmpty()!=true){
            List<Device> d = userMapper.deviceFindById(device.getId());
            if(d.isEmpty())
                return false;
            if(device.getName().isEmpty()){
                device.setName(d.get(0).getName());
            }
            if(device.getKind().isEmpty()){
                device.setKind(d.get(0).getKind());
            }
            if(device.getNetwork_id().isEmpty()){
                device.setNetwork_id(d.get(0).getNetwork_id());
            }
            device.setState(d.get(0).getState());
            return userMapper.deviceUpdate(device);
        }else
            return false;
    }
    public int deviceMulDelete(List<String> ids) {      //批量删除设备信息
        int total=0;
        for (String id : ids) {
            if(userMapper.deviceDelete(id)){
                total++;
            }
        }
        return total;
    }
    //member
    public boolean memberUpdate(Member member){      //修改会员信息
        if(member.getId()!=null && member.getId().isEmpty()!=true){
            List<Member> d = userMapper.memberFindById(member.getId());
            if(d.isEmpty())
                return false;
            if(member.getName().isEmpty()){
                member.setName(d.get(0).getName());
            }
            if(member.getMoney()==0){
                member.setMoney(d.get(0).getMoney());
            }
            if(member.getPhone().isEmpty()){
                member.setPhone(d.get(0).getPhone());
            }
            return userMapper.memberUpdate(member);
        }else
            return false;
    }
    public boolean memberInsert(Member member) {      //新增会员信息
        if(userMapper.memberFindById(member.getId()).isEmpty()==false)
            return false;
        if(member.getId()!=null && member.getId().isEmpty()!=true){
            return userMapper.memberInsert(member);
        }else
            return false;
    }


}
