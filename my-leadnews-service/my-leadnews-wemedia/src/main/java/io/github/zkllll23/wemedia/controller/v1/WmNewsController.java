package io.github.zkllll23.wemedia.controller.v1;

import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.wemedia.dtos.WmNewsDto;
import io.github.zkllll23.model.wemedia.dtos.WmNewsPageReqDto;
import io.github.zkllll23.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    /**
     * 文章列表查询
     *
     * @param wmNewsPageReqDto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmNewsPageReqDto wmNewsPageReqDto) {
        return wmNewsService.findList(wmNewsPageReqDto);
    }

    /**
     * 发布文章
     *
     * @param wmNewsDto
     * @return
     */
    @PostMapping("/submit")
    public ResponseResult submitNews (@RequestBody WmNewsDto wmNewsDto) {
        return wmNewsService.submitNews(wmNewsDto);
    }
}
