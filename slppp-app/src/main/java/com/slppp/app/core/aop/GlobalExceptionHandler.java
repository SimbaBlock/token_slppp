/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slppp.app.core.aop;

import com.slppp.app.core.common.exception.BizExceptionEnum;
import com.slppp.app.core.exception.ParamException;
import com.slppp.app.core.exception.XsvException;

import com.slppp.app.core.util.JsonResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午3:19:56
 */
@ControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(XsvException.InvalidBitcoinAddressException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JsonResult invalidBitcoinAddress(XsvException.InvalidBitcoinAddressException e) {
        if (e.getCode() == -5)
             return new JsonResult(BizExceptionEnum.INVALID_BITCOIN_ADDRESS_ERROR.getCode(), BizExceptionEnum.INVALID_BITCOIN_ADDRESS_ERROR.getMessage());
        else
            return new JsonResult(BizExceptionEnum.MISSING_INPUTS_ERROR.getCode(), BizExceptionEnum.MISSING_INPUTS_ERROR.getMessage());
    }

    @ExceptionHandler(ParamException.ParamLengthException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JsonResult paramLength(ParamException.ParamLengthException e) {
        log.error("参数长度错误：", e);
        return new JsonResult(BizExceptionEnum.PARAM_LENGTH_ERROR.getCode(), e.getMessage());
    }


}
