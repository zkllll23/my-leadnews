package io.github.zkllll23.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.zkllll23.common.constants.WeMediaConstants;
import io.github.zkllll23.common.exception.CustomException;
import io.github.zkllll23.file.service.FileStorageService;
import io.github.zkllll23.model.common.dtos.PageResponseResult;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.common.enums.AppHttpCodeEnum;
import io.github.zkllll23.model.wemedia.dtos.WmMaterialDto;
import io.github.zkllll23.model.wemedia.pojos.WmMaterial;
import io.github.zkllll23.utils.thread.WmThreadLocalUtil;
import io.github.zkllll23.wemedia.mapper.WmMaterialMapper;
import io.github.zkllll23.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 图片上传
     *
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 检查参数
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 上传图片到MinIO
        String filename = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = multipartFile.getOriginalFilename().substring(originalFilename.lastIndexOf("."));
        String url = null;
        try {
            url = fileStorageService.uploadImgFile("", filename + suffix, multipartFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 保存url到数据库
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setType((short) 0);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setUrl(url);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);

        // 返回结果
        return ResponseResult.okResult(wmMaterial);
    }

    /**
     * 素材管理列表查询
     *
     * @param wmMaterialDto
     * @return
     */
    @Override
    public ResponseResult list(WmMaterialDto wmMaterialDto) {
        // 参数校验
        wmMaterialDto.checkParam();

        // 分页查询
        IPage page = new Page(wmMaterialDto.getPage(), wmMaterialDto.getSize());
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (wmMaterialDto.getIsCollection() != null && wmMaterialDto.getIsCollection() == 1) {
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection, wmMaterialDto.getIsCollection());
        }
        lambdaQueryWrapper.eq(WmMaterial::getUserId, WmThreadLocalUtil.getUser().getId());
        lambdaQueryWrapper.orderByDesc(WmMaterial::getCreatedTime);
        page(page, lambdaQueryWrapper);

        ResponseResult responseResult = new PageResponseResult(wmMaterialDto.getPage(), wmMaterialDto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        // 结果返回
        return responseResult;
    }

    /**
     * 删除图片
     *
     * @return
     */
    @Override
    public ResponseResult deletePicture(Integer id) {
        if (id == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        boolean res = removeById(id);
        if (!res) {
            throw new CustomException(AppHttpCodeEnum.MATERIAL_DELETE_FAIL);
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 收藏图片
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult collectPicture(Integer id) {
        if (id == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmMaterial wmMaterial = getById(id);
        if (wmMaterial == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        wmMaterial.setIsCollection(WeMediaConstants.COLLECT_MATERIAL);
        updateById(wmMaterial);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 取消收藏图片
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult cancleCollectPicture(Integer id) {
        WmMaterial wmMaterial = getById(id);
        if (wmMaterial == null) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        wmMaterial.setIsCollection(WeMediaConstants.CANCEL_COLLECT_MATERIAL);
        updateById(wmMaterial);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
