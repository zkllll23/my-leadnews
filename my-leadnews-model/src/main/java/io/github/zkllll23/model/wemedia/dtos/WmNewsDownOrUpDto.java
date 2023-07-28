package io.github.zkllll23.model.wemedia.dtos;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class WmNewsDownOrUpDto {
    /**
     * 文章id
     */
    private Integer id;

    /**
     * 上下架 0-下架 1-上架
     */
    private Short enable;
}
