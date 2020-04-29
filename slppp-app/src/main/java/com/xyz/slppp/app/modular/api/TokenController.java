package com.xyz.slppp.app.modular.api;

import com.xyz.slppp.app.core.util.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class TokenController {

    @ResponseBody
    @RequestMapping(value="/token", method = RequestMethod.POST)
    public JsonResult getUtxo(String type, String hex, String sourceAddress, String receivingAddress, String token) throws Exception {



        return new JsonResult();

    }

}
