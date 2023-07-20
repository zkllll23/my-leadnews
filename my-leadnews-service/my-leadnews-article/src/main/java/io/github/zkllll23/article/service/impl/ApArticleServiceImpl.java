package io.github.zkllll23.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.zkllll23.article.mapper.ApArticleMapper;
import io.github.zkllll23.article.service.ApArticleService;
import io.github.zkllll23.common.constants.ArticleConstants;
import io.github.zkllll23.model.article.dtos.ArticleHomeDto;
import io.github.zkllll23.model.article.pojos.ApArticle;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    /**
     * 加载文章列表
     *
     * @param dto
     * @param type 1-加载更多,2-加载最新
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        // 参数校验
        Integer size = dto.getSize();
        if (size == null || size == 0) {
            size = 10;
        }
        size = Math.min(size, 50);
        dto.setSize(size);

        if (!ArticleConstants.LOADTYPE_LOAD_MORE.equals(type) || !ArticleConstants.LOADTYPE_LOAD_NEW.equals(type)) {
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        if (StringUtils.isBlank(dto.getTag())) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        if (dto.getMaxBehotTime() == null) {
            dto.setMaxBehotTime(new Date());
        }
        if (dto.getMinBehotTime() == null) {
            dto.setMinBehotTime(new Date());
        }

        // 查询
        List<ApArticle> list = apArticleMapper.loadArticleList(dto, type);
        return ResponseResult.okResult(list);
    }
}
