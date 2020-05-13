package com.xyz.slppp.app.modular.system.service;

import com.xyz.slppp.app.core.util.JsonResult;
import com.xyz.slppp.app.modular.system.model.Member;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {

    int insert(Member member);

    Member findByUserName(String name);

    Member findByUserNameAndPassword(String username, String password);

    JsonResult login(String username, String password, HttpServletResponse response);

}
