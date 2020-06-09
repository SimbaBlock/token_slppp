package com.slppp.app.modular.api;

import com.slppp.app.core.util.JsonResult;
import com.slppp.app.modular.system.model.ContentText;
import com.slppp.app.modular.system.service.ContentTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest/content")
public class ContentController {

    @Autowired
    private ContentTextService contentTextService;

    @ResponseBody
    @RequestMapping("/info")
    public JsonResult register(Integer type) {

        ContentText contentText = contentTextService.findByType(type);

        return new JsonResult().addData("content", contentText);

    }



}
