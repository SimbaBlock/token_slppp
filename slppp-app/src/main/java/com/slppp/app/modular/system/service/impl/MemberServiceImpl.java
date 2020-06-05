package com.slppp.app.modular.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.slppp.app.config.shiro.ShiroKit;
import com.slppp.app.config.shiro.security.JwtProperties;
import com.slppp.app.config.shiro.security.JwtToken;
import com.slppp.app.config.shiro.security.JwtUtil;
import com.slppp.app.core.common.exception.BizExceptionEnum;
import com.slppp.app.core.constant.SecurityConsts;
import com.slppp.app.core.util.JedisUtils;
import com.slppp.app.core.util.JsonResult;
import com.slppp.app.modular.system.dao.MemberMapper;
import com.slppp.app.modular.system.model.KycAddress;
import com.slppp.app.modular.system.model.Member;
import com.slppp.app.modular.system.service.KycAddressService;
import com.slppp.app.modular.system.service.MemberService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Autowired
    private JedisUtils jedisUtils;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private KycAddressService kycAddressService;

    @Override
    public int insert(Member member) {
        return memberMapper.insert(member);
    }

    @Override
    public Member findByUserName(String name) {
        return memberMapper.findByUserName(name);
    }

    @Override
    public Member findByUserNameAndPassword(String username, String password) {
        return memberMapper.findByUserNameAndPassword(username, password);
    }

    @Override
    public JsonResult login(String username, String password, HttpServletResponse response) {

        String encodePassword = ShiroKit.md5(password, SecurityConsts.LOGIN_SALT);

        Member member = memberMapper.findByUserName(username);

        if (member == null)
            return new JsonResult(BizExceptionEnum.USER_NOT_ERROR.getCode(), BizExceptionEnum.USER_NOT_ERROR.getMessage());

        if (!encodePassword.equals(member.getPassword()))
            return new JsonResult(BizExceptionEnum.USER_PASSWORD_ERROR.getCode(), BizExceptionEnum.USER_PASSWORD_ERROR.getMessage());

        String strToken = this.loginSuccess(member.getUsername(), response);

        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new JwtToken(strToken);
        subject.login(token);


        KycAddress kyc = kycAddressService.findByMemberId( member.getId());

        return new JsonResult().addData("private_key", member.getPrivateKey()).addData("status", member.getStatus()).addData("name", kyc.getName());
    }

    @Override
    public int updateStatus(Member member) {
        return memberMapper.updateStatus(member);
    }

    /**
     * 登录后更新缓存，生成token，设置响应头部信息
     *
     * @param account
     * @param response
     */
    private String loginSuccess(String account, HttpServletResponse response) {

        String currentTimeMillis = String.valueOf(System.currentTimeMillis());

        //生成token
        JSONObject json = new JSONObject();
        String token = JwtUtil.sign(account, currentTimeMillis);
        json.put("token", token);

        //更新RefreshToken缓存的时间戳
        String refreshTokenKey= SecurityConsts.PREFIX_SHIRO_REFRESH_TOKEN + account;
        jedisUtils.saveString(refreshTokenKey, currentTimeMillis, jwtProperties.getTokenExpireTime()*60);

/*        //记录登录日志
        LoginLog loginLog= new LoginLog();
        loginLog.setAccount(account);
        loginLog.setLoginTime(Date.from(Instant.now()));
        loginLog.setContent("登录成功");
        loginLog.setYnFlag(YNFlagStatusEnum.VALID.getCode());
        loginLog.setCreator(account);
        loginLog.setEditor(account);
        loginLog.setCreatedTime(loginLog.getLoginTime());
        loginLogService.save(loginLog);*/

        //写入header
        response.setHeader(SecurityConsts.REQUEST_AUTH_HEADER, token);
        response.setHeader("Access-Control-Expose-Headers", SecurityConsts.REQUEST_AUTH_HEADER);

        return token;
    }

}
