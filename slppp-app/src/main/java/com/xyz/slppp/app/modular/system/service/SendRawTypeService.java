package com.xyz.slppp.app.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xyz.slppp.app.modular.system.model.SendRawType;


public interface SendRawTypeService extends IService<SendRawType> {

    int insertSendRawType(SendRawType sendRawType);

}
