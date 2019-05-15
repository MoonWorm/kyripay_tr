package com.kyripay.converter.api;

import com.google.common.collect.Lists;
import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.Format;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@Api("Converter Service")
public class ConverterController
{
    @PostMapping(value = "/v1/converters/{formatId}/documents", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "Push document for conversion",
        notes = "Accepts the document object and delegate it to the specific converter defined by formatId.")
    ConverterResponse convert(@RequestBody byte[] data, @PathVariable String formatId){
        return new ConverterResponse(UUID.randomUUID().toString());
    }

    @GetMapping("/v1/converters")
    @ApiOperation("Returns list of available formats")
    List<Format> getFormats(){
        return Lists.newArrayList(Format.FORMAT_1, Format.FORMAT_2);
    }

    @GetMapping("/v1/documents/{id}")
    @ApiOperation("Returns converted document by id")
    Document getDocument(@PathVariable String id){
        return new Document(id, Format.FORMAT_1, "converted data".getBytes());
    }


    @Value
    @ApiModel(value = "Converter response", description = "Contains id of the document to be requested via '/v1/documents/{id}' endpoint")
    private class ConverterResponse {
        private final String documentId;
    }
}
