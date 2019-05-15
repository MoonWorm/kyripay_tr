package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel(value = "Document", description = "Converted payment document")
public class Document {
    private String id;
    private Format format;
    private byte[] data;


    public Document(String id, Format format, byte[] data)
    {
        this.id = id;
        this.format = format;
        this.data = data;
    }


    public Document(byte[] data)
    {
        this.data = data;
    }
}
