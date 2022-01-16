/**
 * Copyright 2021 json.cn
 */
package com.wh.common.toy.portainer.data;

/**
 * Auto-generated: 2021-12-07 14:21:16
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Auth {

    private String jwt;
    private Long expireTimestamp;

    public Auth() {
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public Long getExpireTimestamp() {
        return expireTimestamp;
    }

    public void setExpireTimestamp(Long expireTimestamp) {
        this.expireTimestamp = expireTimestamp;
    }

    public void setExpireTimestamp() {
        expireTimestamp = System.currentTimeMillis() + 3500 * 3;
    }

    public boolean isOutDate() {
        return System.currentTimeMillis() >= expireTimestamp || jwt == null || jwt.equals("");
    }

    public Auth(String jwt, Long expireTimestamp) {
        this.jwt = jwt;
        this.expireTimestamp = expireTimestamp;
    }

}