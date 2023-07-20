package io.github.zkllll23.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.zkllll23.model.article.dtos.ArticleHomeDto;
import io.github.zkllll23.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    public List<ApArticle> loadArticleList(@Param("dto") ArticleHomeDto dto, @Param("type") Short type);

}