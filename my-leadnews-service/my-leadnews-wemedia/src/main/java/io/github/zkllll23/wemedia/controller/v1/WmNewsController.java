package io.github.zkllll23.wemedia.controller.v1;

import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.wemedia.dtos.WmNewsDto;
import io.github.zkllll23.model.wemedia.dtos.WmNewsPageReqDto;
import io.github.zkllll23.wemedia.service.WmNewsService;
import io.swagger.models.auth.In;
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

    /**
     * 查看详情
     *
     * @return
     */
    @GetMapping("/one/{id}")
    public ResponseResult getContent(@PathVariable Integer id) {
        return wmNewsService.getContent(id);
    }

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @GetMapping("/del_news/{id}")
    public ResponseResult deleteNews(@PathVariable Integer id) {
        return wmNewsService.deleteNews(id);
    }
}
