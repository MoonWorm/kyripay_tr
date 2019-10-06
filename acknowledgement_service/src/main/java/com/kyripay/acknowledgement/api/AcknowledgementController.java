package com.kyripay.acknowledgement.api;

import com.kyripay.acknowledgement.dto.Acknowledgement;
import com.kyripay.acknowledgement.exceptions.WrongFormatException;
import com.kyripay.acknowledgement.service.AcknowledgementConversionService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Api("Acknowledgement service")
@AllArgsConstructor
public class AcknowledgementController {
    private final AcknowledgementConversionService acknowledgementConversionService;

    @PostMapping("acks")
    @ApiOperation(value = "Post acknowledgement for conversion",
                  notes = "Accepts the acknowledgement object and convert it using converter defined by format")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Acknowledgement is accepted for processing"),
            @ApiResponse(code = 400, message = "Acknowledgement can't be processed for given data")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    void convertAcknowledgement(@Valid @RequestBody Acknowledgement acknowledgement){
        try {
            acknowledgementConversionService.convert(acknowledgement);
        } catch (WrongFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
