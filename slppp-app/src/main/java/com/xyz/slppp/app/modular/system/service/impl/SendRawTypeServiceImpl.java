package com.xyz.slppp.app.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xyz.slppp.app.modular.system.dao.SendRawTypeMapper;
import com.xyz.slppp.app.modular.system.model.SendRawType;
import com.xyz.slppp.app.modular.system.service.SendRawTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SendRawTypeServiceImpl extends ServiceImpl<SendRawTypeMapper, SendRawType> implements SendRawTypeService {

    @Resource
    private SendRawTypeMapper sendRawTypeMapper;

    @Override
    public int insertSendRawType(SendRawType sendRawType) {
        return sendRawTypeMapper.insertSendRawType(sendRawType);
    }

}
