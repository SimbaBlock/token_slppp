package com.slppp.app.modular.system.service.impl;

import com.slppp.app.modular.system.dao.ContentTextMapper;
import com.slppp.app.modular.system.model.ContentText;
import com.slppp.app.modular.system.service.ContentTextService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ContentTextServiceImpl implements ContentTextService {

    @Resource
    private ContentTextMapper contentTextMapper;

    @Override
    public ContentText findByType(Integer type) {
        return contentTextMapper.findByType(type);
    }

}
