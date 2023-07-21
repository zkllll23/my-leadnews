package io.github.zkllll23.article.test;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.github.zkllll23.article.ArticleApplication;
import io.github.zkllll23.article.mapper.ApArticleContentMapper;
import io.github.zkllll23.article.service.ApArticleService;
import io.github.zkllll23.file.service.FileStorageService;
import io.github.zkllll23.model.article.pojos.ApArticle;
import io.github.zkllll23.model.article.pojos.ApArticleContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApArticleService apArticleService;

    @Test
    public void createStaticUrlTest() throws Exception {
        // 1.获取文章内容
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, "1383827976310018049L"));
        if (apArticleContent != null || StringUtils.isNotBlank(apArticleContent.getContent())) {
            // 2.文章内容通过freemarker生成html文件
            Template template = configuration.getTemplate("article.ftl");
            Map<String, Object> map = new HashMap<>();
            map.put("content", JSONArray.parseArray(apArticleContent.getContent()));
            StringWriter out = new StringWriter();
            template.process(map, out);

            // 3.上传html文件到MinIO
            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", in);
            // 4.保存url到ap_article
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId, apArticleContent.getArticleId()).set(ApArticle::getStaticUrl, path));
        }

    }
}
