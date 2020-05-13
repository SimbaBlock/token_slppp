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
package com.slppp.app.core.common.exception;

import cn.stylefeng.roses.kernel.model.exception.AbstractBaseExceptionEnum;

/**
 * @author fengshuonan
 * @Description 所有业务异常的枚举
 * @date 2016年11月12日 下午5:04:51
 */
public enum BizExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 字典
     */
    DICT_EXISTED(400, "字典已经存在"),
    ERROR_CREATE_DICT(500, "创建字典失败"),
    ERROR_WRAPPER_FIELD(500, "包装字典属性失败"),
    ERROR_CODE_EMPTY(500, "字典类型不能为空"),

    /**
     * 文件上传
     */
    FILE_READING_ERROR(400, "FILE_READING_ERROR!"),
    FILE_NOT_FOUND(400, "FILE_NOT_FOUND!"),
    UPLOAD_ERROR(500, "上传图片出错"),

    /**
     * 权限和数据问题
     */
    DB_RESOURCE_NULL(400, "数据库中没有该资源"),
    NO_PERMITION(405, "权限异常"),
    REQUEST_INVALIDATE(400, "请求数据格式不正确"),
    INVALID_KAPTCHA(400, "验证码不正确"),
    VALIDECODE_TIMEOUT(400,"验证码已过期"),
    VALIDECODE_IP_TO_MUCH(400,"不可频繁发送验证码"),
    REPEAT_SUBMIT(400,"重复提交"),
    CANT_DELETE_ADMIN(600, "不能删除超级管理员"),
    CANT_FREEZE_ADMIN(600, "不能冻结超级管理员"),
    CANT_CHANGE_ADMIN(600, "不能修改超级管理员角色"),


    /**
     * 账户问题
     */
    USER_ALREADY_REG(401, "该用户已经注册"),
    NO_THIS_USER(400, "没有此用户"),
    USER_NOT_EXISTED(400, "没有此用户"),
    ACCOUNT_FREEZED(401, "账号被冻结"),
    OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
    TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),
    VISITOR_GENREATE_DUPLICATE(400,"生成用户名重复"),
    LOGIN_TOO_OFTEN(400,"登录频繁，请稍后再试"),
    LOGIN_INVALID(400,"用户不存在或密码错误"),
    USER_DISABLE(400,"账户不存在或不可用"),
    SUPER_NOT_EXISTED(400,"上级不存在"),
    SUPER_ROLE_MISMATCH(400,"上级类型不匹配"),
    INVITE_CODE_NOT_EXISTED(400,"邀请码不存在"),
    /**
     * 错误的请求
     */
    MENU_PCODE_COINCIDENCE(400, "菜单编号和副编号不能一致"),
    EXISTED_THE_MENU(400, "菜单编号重复，不能添加"),
    DICT_MUST_BE_NUMBER(400, "字典的值必须为数字"),
    REQUEST_NULL(400, "请求有错误"),
    SESSION_TIMEOUT(400, "会话超时"),
    SERVER_ERROR(500, "服务器异常"),
    PARAMETER_CANT_BE_EMPTY(400,"缺失参数"),
    /**
     * token异常
     */
    TOKEN_EXPIRED(700, "token过期"),
    TOKEN_ERROR(700, "token验证失败"),

    /**
     * 签名异常
     */
    SIGN_ERROR(700, "签名验证失败"),

    /**
     * 其他
     */
    AUTH_REQUEST_ERROR(400, "账号密码错误"),


    /**
     *  signature
     */


    USER_NOT_LOGIN_ERROR(400401, "该用户token失效，请重新登录"),

    USER_INFO_ERROR(200045, "该用户已经填写资料"),

    USER_ADDRESS_ERROR(200046,"该地址已经存在"),

    USER_REGISTER_REPEAT_ERROR(10001, "用户名已存在！"),

    USER_REGISTER_CODE_ERROR(20001,"验证码错误"),

    USER_REGISTER_OVERDUE_ERROR(20002,"验证码过期"),

    USER_NAME_PASSWORD_ERROR(10005, "用户名或密码错误"),





    // token

    USER_PASSWORD_ERROR(10008, "密码错误"),

    USER_NOT_ERROR(10009, "用户不存在"),

    TOKEN_ISNULL_ERROR(4000001, "这个token不存在"),

    MONEY_IS_NOT_ENOUGH_ERROR(4000002, "当前地址钱不够"),

    NOT_TOKEN_ERROR(4000003, "当前token不存在"),

    TOKEN_NO_AUTHORITY_ERROR(4000004, "无增发权限"),

//    ADDRESS_NOT_KYC_ERROR(4000005, "地址没有认证"),

    ADDRESS_REPEAT_ERROR(4000006, "认证地址重复"),

    LONG_ERROR(4000007, "数值非法符号或太大"),

    TOKEN_RECHARGE_ERROR(4000008, "用户充值失败"),

    MINT_ADD_ERROR(4000009, "重复增发"),

    SYSTEM_ADDRESS_UTXO_ERROR(4000010, "系统地址utxo不足"),

    CREATE_RAW_TRANSACTION_ERROR(4000011, "创建交易错误"),

    SIGN_RAW_TRANSACTION_ERROR(4000012, "签名错误"),

    SEND_RAW_TRANSACTION_ERROR(4000013, "上链失败"),

    INVALID_BITCOIN_ADDRESS_ERROR(4000014, "地址无效"),

    NOT_INT_ERROR(4000015, "参数必须是数值"),

    TYPE_ERROR(4000016, "type参数不识别"),

    PARAM_LENGTH_ERROR(4000017, "长度过长："),

    MINT_ADD_TOKEN_LOSS_ERROR(4000018, "增发token遗失"),

    GENESIS_TOKEN_TYPE_ERROR(4000019, "token_type格式错误"),

    GENESIS_TOKEN_TICKER_ERROR(4000020, "token_ticker格式错误"),

    GENESIS_TOKEN_NAME_ERROR(4000021, "token_name格式错误"),

    GENESIS_TOKEN_URL_ERROR(4000022, "token_url格式错误"),

    GENESIS_TOKEN_PRECITION_ERROR(4000023, "precition格式错误"),

    GENESIS_TOKEN_VOUT_ERROR(4000024, "vout格式错误"),

    GENESIS_TOKEN_QUANTITY_ERROR(4000025, "quantity格式错误"),

    ADDRESS_HASH_ERROR(4000026, "当前还无hash记录"),

    MISSING_INPUTS_ERROR(4000027, "missing_inputs")

    ;


    BizExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
