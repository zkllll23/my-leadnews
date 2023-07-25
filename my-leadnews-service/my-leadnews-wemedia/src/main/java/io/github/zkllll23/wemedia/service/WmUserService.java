package io.github.zkllll23.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.wemedia.dtos.WmLoginDto;
import io.github.zkllll23.model.wemedia.pojos.WmUser;

public interface WmUserService extends IService<WmUser> {

    /**
     * 自媒体端登录
     * @param dto
     * @return
     */
    public ResponseResult login(WmLoginDto dto);

}