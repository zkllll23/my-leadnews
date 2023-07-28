package io.github.zkllll23.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.wemedia.dtos.WmNewsDto;
import io.github.zkllll23.model.wemedia.dtos.WmNewsPageReqDto;
import io.github.zkllll23.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {

    /**
     * 查询文章列表
     *
     * @param wmNewsPageReqDto
     * @return
     */
    public ResponseResult findList(WmNewsPageReqDto wmNewsPageReqDto);

    /**
     * 发布文章
     *
     * @param wmNewsDto
     * @return
     */
    public ResponseResult submitNews(WmNewsDto wmNewsDto);

    /**
     * 查看详情
     *
     * @param id
     * @return
     */
    public ResponseResult getContent(Integer id);

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    ResponseResult deleteNews(Integer id);
}
