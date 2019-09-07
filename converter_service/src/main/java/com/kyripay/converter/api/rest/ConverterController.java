package com.kyripay.converter.api.rest;

import com.kyripay.converter.api.dto.ConversionRequest;
import com.kyripay.converter.api.dto.ConverterResponse;
import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.DocumentStatus;
import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.exceptions.DocumentNotFoundException;
import com.kyripay.converter.exceptions.WrongFormatException;
import com.kyripay.converter.service.ConversionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
@Api("Converter Service")
@AllArgsConstructor
public class ConverterController
{
  private final ConversionService conversionService;


  @PostMapping(value = "converters/conversion-requests")
  @ApiOperation(value = "Push document for conversion",
      notes = "Accepts the request object and delegate it to the specific converter defined by formatId.")
  @ApiResponses({
      @ApiResponse(code = 202, message = "Payment is accepted for conversion", response = Document.class),
      @ApiResponse(code = 400, message = "Payment cannot be converted to the given format") }
  )
  @ResponseStatus(HttpStatus.ACCEPTED)
  ConverterResponse convert(@RequestBody ConversionRequest request)
  {
    try {
      String documentId = conversionService.convert(request.getPayment(), request.getFormat());
      return new ConverterResponse(documentId, DocumentStatus.PROCESSING);
    }
    catch (WrongFormatException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

  }


  @GetMapping("converters")
  @ApiOperation("Returns list of available formats")
  Map<String, FormatDetails> getFormats()
  {
    return conversionService.getFormats();
  }


  @GetMapping("documents/{id}")
  @ApiOperation("Returns converted document by id")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Document is found", response = Document.class),
      @ApiResponse(code = 404, message = "Document not found") }
  )
  Document getDocument(@NotBlank(message = "Document id must be provided") @PathVariable String id)
  {
    try {
      return conversionService.getDocument(id);
    }
    catch (DocumentNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }
}
