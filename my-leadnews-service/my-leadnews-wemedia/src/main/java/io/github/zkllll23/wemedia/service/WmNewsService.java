package io.github.zkllll23.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.wemedia.dtos.WmNewsPageReqDto;
import io.github.zkllll23.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {

    public ResponseResult findList(WmNewsPageReqDto wmNewsPageReqDto);
}
