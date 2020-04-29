package com.xyz.slppp.app.modular.system.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xyz.slppp.app.modular.system.model.SendRawType;


public interface SendRawTypeMapper extends BaseMapper<SendRawType> {

    int insertSendRawType(SendRawType sendRawType);

}
