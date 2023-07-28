package io.github.zkllll23.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.zkllll23.common.constants.WeMediaConstants;
import io.github.zkllll23.common.exception.CustomException;
import io.github.zkllll23.model.common.dtos.PageResponseResult;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.common.enums.AppHttpCodeEnum;
import io.github.zkllll23.model.wemedia.dtos.WmNewsDto;
import io.github.zkllll23.model.wemedia.dtos.WmNewsPageReqDto;
import io.github.zkllll23.model.wemedia.pojos.WmMaterial;
import io.github.zkllll23.model.wemedia.pojos.WmNews;
import io.github.zkllll23.model.wemedia.pojos.WmNewsMaterial;
import io.github.zkllll23.utils.thread.WmThreadLocalUtil;
import io.github.zkllll23.wemedia.mapper.WmMaterialMapper;
import io.github.zkllll23.wemedia.mapper.WmNewsMapper;
import io.github.zkllll23.wemedia.mapper.WmNewsMaterialMapper;
import io.github.zkllll23.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    /**
     * 查询文章列表
     *
     * @param wmNewsPageReqDto
     * @return
     */
    @Override
    public ResponseResult findList(WmNewsPageReqDto wmNewsPageReqDto) {
        // 检查参数
        wmNewsPageReqDto.checkParam();

        // 分页查询
        IPage page = new Page(wmNewsPageReqDto.getPage(), wmNewsPageReqDto.getSize());
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (wmNewsPageReqDto.getStatus() != null) {
            lambdaQueryWrapper.eq(WmNews::getStatus, wmNewsPageReqDto.getStatus());
        }

        if (wmNewsPageReqDto.getChannelId() != null) {
            lambdaQueryWrapper.eq(WmNews::getChannelId, wmNewsPageReqDto.getChannelId());
        }

        if (wmNewsPageReqDto.getBeginPubDate() != null && wmNewsPageReqDto.getEndPubDate() != null) {
            lambdaQueryWrapper.between(WmNews::getPublishTime, wmNewsPageReqDto.getBeginPubDate(), wmNewsPageReqDto.getEndPubDate());
        }

        if (StringUtils.isNotBlank(wmNewsPageReqDto.getKeyword())) {
            lambdaQueryWrapper.like(WmNews::getTitle, wmNewsPageReqDto.getKeyword());
        }

        lambdaQueryWrapper.eq(WmNews::getUserId, WmThreadLocalUtil.getUser().getId());
        lambdaQueryWrapper.orderByDesc(WmNews::getPublishTime);

        page(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(wmNewsPageReqDto.getPage(), wmNewsPageReqDto.getSize(), (int) page.getTotal());
        // 结果返回
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**
     * 查看详情
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult getContent(Integer id) {
        if (id == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.okResult(wmNews);
    }

    /**
     * 发布文章
     *
     * @param wmNewsDto
     * @return
     */
    @Override
    public ResponseResult submitNews(WmNewsDto wmNewsDto) {
        // 检查参数
        if (wmNewsDto == null || wmNewsDto.getContent() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 保存或修改文章
        WmNews wmNews = new WmNews();
        try {
            BeanUtils.copyProperties(wmNews,wmNewsDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (wmNewsDto.getImages() != null && wmNewsDto.getImages().size() > 0) {
            // StringUtils用不了,选择用StringBuilder
            StringBuilder sb = new StringBuilder();
            for (String str : wmNewsDto.getImages()) {
                if (StringUtils.isNotBlank(str)) {
                    sb.append(str).append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            String imageStr = sb.toString();
            wmNews.setImages(imageStr);
        }
        // 如果封面类型为自动,先设置为空
        if (wmNewsDto.getType().equals(WeMediaConstants.WM_NEWS_TYPE_AUTO)) {
            wmNews.setType(null);
        }
        saveOrUpdateWmNews(wmNews);

        // 如果存入草稿 直接返回
        if (wmNewsDto.getStatus().equals(WmNews.Status.NORMAL.getCode())) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        // 保存文章内容图片与素材关系
        List<String> urls = extractUrls(wmNewsDto.getContent());
        saveRelativeInfoForContent(urls, wmNews.getId());
        // 保存文章封面图片与素材关系
        saveRelativeInfoForCover(urls, wmNews, wmNewsDto);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 保存图片与素材关系
     *
     * @param urls
     * @param id
     */
    private void saveRelativeInfo(List<String> urls, Integer id, Short type) {
        if (urls != null && !urls.isEmpty()) {
            List<WmMaterial> wmMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery().in(WmMaterial::getUrl, urls));
            if (wmMaterials == null || wmMaterials.isEmpty()) {
                throw new CustomException(AppHttpCodeEnum.MATERIAL_REFERENCE_INVALID);
            }
            if (urls.size() != wmMaterials.size()) {
                throw new CustomException(AppHttpCodeEnum.MATERIAL_REFERENCE_INVALID);
            }
            List<Integer> idList = wmMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());
            wmNewsMaterialMapper.saveRelations(idList, id, type);
        }
    }

    /**
     * @param urls
     * @param wmNews
     * @param wmNewsDto
     */
    private void saveRelativeInfoForCover(List<String> urls, WmNews wmNews, WmNewsDto wmNewsDto) {
        List<String> images = wmNewsDto.getImages();

        if (wmNewsDto.getType().equals(WeMediaConstants.WM_NEWS_TYPE_AUTO)) {
            if (urls.size() >= 3) {
                wmNews.setType(WeMediaConstants.WM_NEWS_MANY_IMAGE);
                images = urls.stream().limit(3).collect(Collectors.toList());
            } else if (urls.size() >= 1) {
                wmNews.setType(WeMediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = urls.stream().limit(1).collect(Collectors.toList());
            } else {
                wmNews.setType(WeMediaConstants.WM_NEWS_NONE_IMAGE);
            }
            if (images != null && images.size() > 0){
                StringBuilder sb = new StringBuilder();
                for (String str : images) {
                    if (StringUtils.isNotBlank(str)) {
                        sb.append(str).append(",");
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
                String imageStr = sb.toString();
                wmNews.setImages(imageStr);
            }
            updateById(wmNews);
        }

        if (images != null && !images.isEmpty()) {
            saveRelativeInfo(images, wmNews.getId(), WeMediaConstants.WM_COVER_REFERENCE);
        }
    }


    /**
     * 保存文章内容图片与素材关系
     *
     * @param urls
     * @param id
     */
    private void saveRelativeInfoForContent(List<String> urls, Integer id) {
        saveRelativeInfo(urls, id, WeMediaConstants.WM_CONTENT_REFERENCE);
    }

    /**
     * 提取内容中的images的url
     *
     * @param content
     * @return
     */
    private List<String> extractUrls(String content) {
        List<String> urls = new ArrayList<>();

        List<Map> maps = JSON.parseArray(content, Map.class);
        for (Map map : maps) {
            Object typeValue = map.get("type");
            if (typeValue != null && typeValue.equals("image")) {
                urls.add((String) map.get("value"));
            }
        }
        return urls;
    }

    /**
     * 保存或修改文章
     *
     * @param wmNews
     */
    private void saveOrUpdateWmNews(WmNews wmNews) {
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        // 默认上架
        wmNews.setEnable(WeMediaConstants.WM_NEWS_ENABLE_TRUE);
        // 判断是保存还是修改
        if (wmNews.getId() == null) {
            // 保存
            save(wmNews);
        } else {
            //修改 先删除图片和素材的关系再保存
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId, wmNews.getId()));
            updateById(wmNews);
        }
    }
}
