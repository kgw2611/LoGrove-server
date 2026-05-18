package com.hansung.logrove.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConvertedImage {

    private byte[] bytes;
    private Integer width;
    private Integer height;
}
