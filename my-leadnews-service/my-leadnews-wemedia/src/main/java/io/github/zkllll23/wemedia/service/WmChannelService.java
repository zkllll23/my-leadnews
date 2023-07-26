package io.github.zkllll23.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {

    /**
     * 查询所有频道
     *
     */
    public ResponseResult findAll();
}
