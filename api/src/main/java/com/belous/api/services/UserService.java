package com.belous.api.services;

import com.belous.api.models.UserEntity;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;


import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by Vincenzo Racca
 */
public interface UserService {

    UserEntity save(UserEntity user);
    UserEntity addRoleToUser(String username, String roleName);
    UserEntity findByUsername(String username);
    List<UserEntity> findAll();
    Map<String,String> refreshToken(String authorizationHeader, String issuer) throws BadJOSEException, ParseException, JOSEException;
}