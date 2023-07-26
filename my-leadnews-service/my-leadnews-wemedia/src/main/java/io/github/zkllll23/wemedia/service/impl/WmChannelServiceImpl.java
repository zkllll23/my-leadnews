package io.github.zkllll23.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.wemedia.pojos.WmChannel;
import io.github.zkllll23.wemedia.mapper.WmChannelMapper;
import io.github.zkllll23.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {
    /**
     * 查询所有频道
     *
     */
    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }
}
