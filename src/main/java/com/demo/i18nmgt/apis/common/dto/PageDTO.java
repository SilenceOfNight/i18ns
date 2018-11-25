package com.demo.i18nmgt.apis.common.dto;

import lombok.Data;

import java.util.List;

/**
 * PageDTO
 *
 * @author Z
 * @date 2018/10/21
 */
@Data
public class PageDTO<T> {
    private Long pageIndex;

    private Long pageSize;

    private Long total;

    private List<? extends T> data;
}
