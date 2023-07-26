package io.github.zkllll23.wemedia.controller.v1;

import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channel")
public class WmChannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @RequestMapping("/channels")
    public ResponseResult findAll() {
        return wmChannelService.findAll();
    }
}
