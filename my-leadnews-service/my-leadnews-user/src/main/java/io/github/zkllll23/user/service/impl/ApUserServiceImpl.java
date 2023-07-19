package io.github.zkllll23.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.common.enums.AppHttpCodeEnum;
import io.github.zkllll23.model.user.pojos.ApUser;
import io.github.zkllll23.user.dtos.LoginDto;
import io.github.zkllll23.user.mapper.ApUserMapper;
import io.github.zkllll23.user.service.ApUserService;
import io.github.zkllll23.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.crypto.Digest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    /**
     * app端登录功能
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {
        // 正常登录
        if (StringUtils.isNotBlank(dto.getPhone()) && StringUtils.isNotBlank(dto.getPassword())) {
            ApUser dbUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone()));
            if (dbUser == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户信息不存在");
            }

            String salt = dbUser.getSalt();
            String password = dto.getPassword();
            String pswd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            if (!pswd.equals(dbUser.getPassword())) {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }

            String token = AppJwtUtil.getToken(dbUser.getId().longValue());
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            dbUser.setSalt("");
            dbUser.setPassword("");
            map.put("user", dbUser);

            return ResponseResult.okResult(map);
        } else {
            // 游客登录
            Map<String, Object> map = new HashMap<>();
            String token = AppJwtUtil.getToken(0L);
            map.put("token", token);
            return ResponseResult.okResult(map);
        }
    }
}
