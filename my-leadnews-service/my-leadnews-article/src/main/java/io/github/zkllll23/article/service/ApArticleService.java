package io.github.zkllll23.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.zkllll23.model.article.dtos.ArticleHomeDto;
import io.github.zkllll23.model.article.pojos.ApArticle;
import io.github.zkllll23.model.common.dtos.ResponseResult;

public interface ApArticleService extends IService<ApArticle> {

    /**
     * 加载文章列表
     * @param dto
     * @param type 1-加载更多,2-加载最新
     * @return
     */
    public ResponseResult load(ArticleHomeDto dto, Short type);
}
