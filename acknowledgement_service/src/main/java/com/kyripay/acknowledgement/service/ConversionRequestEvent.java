package com.kyripay.acknowledgement.service;

import com.kyripay.acknowledgement.dto.Acknowledgement;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConversionRequestEvent extends ApplicationEvent {

    private final Acknowledgement acknowledgement;

    ConversionRequestEvent(Object source, Acknowledgement acknowledgement) {
        super(source);
        this.acknowledgement = acknowledgement;
    }
}
