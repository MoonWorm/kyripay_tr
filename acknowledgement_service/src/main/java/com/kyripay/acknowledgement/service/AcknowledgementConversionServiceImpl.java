package com.kyripay.acknowledgement.service;

import com.kyripay.acknowledgement.converters.Converter;
import com.kyripay.acknowledgement.dto.AckConversionStatus;
import com.kyripay.acknowledgement.dto.Acknowledgement;
import com.kyripay.acknowledgement.dto.ConvertedAcknowledgement;
import com.kyripay.acknowledgement.exceptions.WrongFormatException;
import com.kyripay.acknowledgement.repository.AcknowledgementRepository;
import com.kyripay.acknowledgement.repository.ConvertedAcknowledgementRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AcknowledgementConversionServiceImpl implements AcknowledgementConversionService, ConversionRequestListener {

    private final Map<String, Converter> availableConverters;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ConvertedAcknowledgementRepository convertedAckRepository;
    private final AcknowledgementRepository ackRepository;

    @Override
    public void convert(Acknowledgement acknowledgement) {
        if (!availableConverters.containsKey(acknowledgement.getFormat()))
            throw new WrongFormatException(String.format("Acknowledgment converter for format %s doesn't supported", acknowledgement.getFormat()));
        acknowledgement.setId(UUID.randomUUID().toString());
        applicationEventPublisher.publishEvent(new ConversionRequestEvent(this, acknowledgement));
        ackRepository.save(acknowledgement);
    }

    @Override
    @Async
    @EventListener
    public void callConverter(ConversionRequestEvent event) {
        ConvertedAcknowledgement convertedAcknowledgement = null;
        try{
            Converter converter = availableConverters.get(event.getAcknowledgement().getFormat());
            convertedAcknowledgement = converter.convert(event.getAcknowledgement());
            convertedAcknowledgement.setAckConversionStatus(AckConversionStatus.SUCCESS);
        } catch (RuntimeException e){
            if (convertedAcknowledgement == null)
                convertedAcknowledgement = new ConvertedAcknowledgement(UUID.randomUUID().toString(),

                    event.getAcknowledgement().getId(), event.getAcknowledgement().getCustomer(),
                        null, null, null,
                    AckConversionStatus.FAILED);
        }
        finally {
            convertedAckRepository.save(convertedAcknowledgement);
        }
    }

}
