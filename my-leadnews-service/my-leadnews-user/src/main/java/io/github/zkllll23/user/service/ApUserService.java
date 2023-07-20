package io.github.zkllll23.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.user.pojos.ApUser;
import io.github.zkllll23.model.user.dtos.LoginDto;

public interface ApUserService extends IService<ApUser > {

    /**
     * app端登录功能
     * @param dto
     * @return
     */
    public ResponseResult login(LoginDto dto);
}
