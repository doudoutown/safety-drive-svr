package com.safety.controller;

import com.nimbusds.jose.JOSEException;
import com.safety.controller.vo.LoginVo;
import com.safety.controller.vo.UserDriveVo;
import com.safety.dao.UserDao;
import com.safety.dao.UserSensorDao;
import com.safety.framework.BaseController;
import com.safety.framework.Response;
import com.safety.po.UserPo;
import com.safety.po.UserPoExample;
import com.safety.po.UserSensorPo;
import com.safety.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by fanwenbin on 2017/7/2.
 */
@RestController
@RequestMapping("/web_api/v1/user")
public class UserController extends BaseController{
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserSensorDao userSensorDao;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    Response<LoginVo> login(@RequestParam("username") String username, @RequestParam("password") String password) throws JOSEException {
        UserPoExample example = new UserPoExample();
        example.or().andUsernameEqualTo(username).andPasswordEqualTo(password);
        List<UserPo> userPos = userDao.selectByExample(example);
        Assert.notEmpty(userPos, "用户名密码错误");
        UserPo userPo = userPos.get(0);
        return Response.SUCCESS(LoginVo.builder().token(TokenUtil.createToken(userPo.getUsername(), userPo.getId())).username(username).password(userPo.getPassword()).build());
    }

    @RequestMapping(value = "/drive/info", method = RequestMethod.POST)
    @ResponseBody
    Response<UserDriveVo> drive(com.safety.framework.Context context,@RequestParam("x") String x, @RequestParam("y") String y,@RequestParam("z")String z) throws JOSEException {
        UserSensorPo po = new UserSensorPo();
        po.setUserId(context.getAccountId());
        po.setX(Integer.valueOf(x));
        po.setY(Integer.valueOf(y));
        po.setZ(Integer.valueOf(z));
        userSensorDao.insertSelective(po);

        return Response.SUCCESS(UserDriveVo.builder().level(Integer.valueOf(x)%2==0?"sdf":"fff").message("汪汪汪").build());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    Response<LoginVo> register(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("confirm_pwd") String confirmPwd,
                               @RequestParam("gender") Integer gender,
                               @RequestParam("age") Integer age,
                               @RequestParam("drive_age") Integer driveAge) throws JOSEException {
        Assert.isTrue(confirmPwd.equals(password), "密码与确认密码不同");
        Assert.isTrue(password.length()>=6, "密码最少6位");
        UserPoExample example = new UserPoExample();
        example.or().andUsernameEqualTo(username);
        Assert.isTrue(userDao.countByExample(example)==0, "用户名已存在");
        UserPo userPo = new UserPo();
        userPo.setAge(age);
        userPo.setDrivingAge(driveAge);
        userPo.setGender(gender);
        userPo.setPassword(password);
        userPo.setUsername(username);
        userDao.insertSelective(userPo);
        return Response.SUCCESS(LoginVo.builder().token(TokenUtil.createToken(userPo.getUsername(), userPo.getId())).username(username).password(userPo.getPassword()).build());
    }
}
