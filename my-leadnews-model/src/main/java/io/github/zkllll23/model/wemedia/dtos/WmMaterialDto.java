package io.github.zkllll23.model.wemedia.dtos;

import io.github.zkllll23.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmMaterialDto extends PageRequestDto {

    /**
     * 是否收藏 0-未收藏 1-收藏
     */
    private Short isCollection;
}
