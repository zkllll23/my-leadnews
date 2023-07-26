package io.github.zkllll23.wemedia.controller.v1;

import io.github.zkllll23.model.common.dtos.ResponseResult;
import io.github.zkllll23.model.wemedia.dtos.WmMaterialDto;
import io.github.zkllll23.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * 上传图片素材
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        return wmMaterialService.uploadPicture(multipartFile);
    }

    /**
     * 素材管理列表查询
     *
     * @param wmMaterialDto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmMaterialDto wmMaterialDto) {
        return wmMaterialService.list(wmMaterialDto);
    }
}