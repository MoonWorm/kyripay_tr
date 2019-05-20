package com.kyripay.converter.api;

import com.google.common.collect.Lists;
import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.Format;
import com.kyripay.converter.dto.Payment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
@Api("Converter Service")
public class ConverterController
{
    @PostMapping(value = "converters/{formatId}/conversion-requests")
    @ApiOperation(value = "Push document for conversion",
        notes = "Accepts the payment object and delegate it to the specific converter defined by formatId.")
    ConverterResponse convert(@RequestBody Payment data, @PathVariable String formatId){
        return new ConverterResponse(UUID.randomUUID().toString());
    }

    @GetMapping("converters")
    @ApiOperation("Returns list of available formats")
    List<Format> getFormats(){
        return Lists.newArrayList(Format.FORMAT_1, Format.FORMAT_2);
    }

    @GetMapping("documents/{id}")
    @ApiOperation("Returns converted document by id")
    Document getDocument(@PathVariable String id){
        return new Document(id, Format.FORMAT_1, "converted data".getBytes());
    }


    @Value
    @ApiModel(value = "Converter response", description = "Contains id of the document to be requested via '/api/v1/documents/{id}' endpoint")
    private class ConverterResponse {
        private final String documentId;
    }
}
