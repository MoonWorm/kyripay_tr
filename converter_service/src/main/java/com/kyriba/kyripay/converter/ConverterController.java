package com.kyriba.kyripay.converter;

import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
public class ConverterController
{

    @PostMapping("/v1/converters/{formatId}/documents")
    ConvertedDocument convert(@RequestBody Document document, @PathVariable String formatId){
        return new ConvertedDocument(UUID.randomUUID().toString());
    }

    @GetMapping("/v1/converters")
    List<String> getFormats(){
        return new ArrayList<>();
    }

    @GetMapping("/v1/documents/{id}")
    ConvertedDocument getDocument(@PathVariable String id){
        return new ConvertedDocument(id);
    }


    @Value
    private class ConvertedDocument {
        private final String id;
    }
}
