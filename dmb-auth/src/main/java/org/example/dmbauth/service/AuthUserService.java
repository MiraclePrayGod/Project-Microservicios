package org.example.dmbauth.service;

import org.example.dmbauth.dto.AuthUserDto;
import org.example.dmbauth.entity.AuthUser;
import org.example.dmbauth.entity.TokenDto;

public interface AuthUserService {
    public AuthUser save(AuthUserDto authUserDto);

    public TokenDto login(AuthUserDto authUserDto);

    public TokenDto validate(String token);

}
