package com.slppp.app.modular.system.dao;

import com.slppp.app.modular.system.model.ContentText;

public interface ContentTextMapper {

    ContentText findByType(Integer type);

}
