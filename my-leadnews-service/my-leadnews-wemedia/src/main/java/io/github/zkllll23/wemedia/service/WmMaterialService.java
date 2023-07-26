package io.github.zkllll23.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.wemedia.dtos.WmMaterialDto;
import io.github.zkllll23.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * 图片上传
     *
     * @param multipartFile
     * @return
     */
    public ResponseResult uploadPicture (MultipartFile multipartFile);

    /**
     * 素材管理列表查询
     *
     * @param wmMaterialDto
     * @return
     */
    public ResponseResult list(WmMaterialDto wmMaterialDto);
}
