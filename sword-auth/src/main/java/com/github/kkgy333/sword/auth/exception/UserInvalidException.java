package com.github.kkgy333.sword.auth.exception;

import com.github.kkgy333.sword.auth.constants.CommonConstants;

/**
 * Author: kkgy333
 * Date: 2018/7/5
 **/
public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, CommonConstants.EX_USER_PASS_INVALID_CODE);
    }
}