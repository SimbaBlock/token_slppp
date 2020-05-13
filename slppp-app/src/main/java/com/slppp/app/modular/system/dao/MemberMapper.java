package com.slppp.app.modular.system.dao;

import com.slppp.app.modular.system.model.Member;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper {

    int insert(Member member);

    Member findByUserName(String name);

    Member findByUserNameAndPassword(@Param("username") String username, @Param("password") String password);

}
