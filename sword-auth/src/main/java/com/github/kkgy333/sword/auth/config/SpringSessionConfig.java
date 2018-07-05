package com.github.kkgy333.sword.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Author: kkgy333
 * Date: 2018/7/5
 **/
@ConditionalOnProperty(prefix = "sword", name = "spring-session-open", havingValue = "true")
public class SpringSessionConfig {

}
