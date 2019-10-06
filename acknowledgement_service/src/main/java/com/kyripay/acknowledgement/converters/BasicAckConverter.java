package com.kyripay.acknowledgement.converters;

import com.kyripay.acknowledgement.dto.*;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;

@Component(BasicAckConverter.ID)
public class BasicAckConverter implements Converter {
    static final String ID = "BASIC_ACK";

    @Override
    public ConvertedAcknowledgement convert(Acknowledgement acknowledgement) {
        byte[] data = Base64.getDecoder().decode(acknowledgement.getData());
        String[] elements = new String(data).split("-");
        return new ConvertedAcknowledgement(UUID.randomUUID().toString(),
                acknowledgement.getId(), acknowledgement.getCustomer(),
                elements[0], AckStatus.fromCode(Integer.parseInt(elements[1])),
                AckLevel.fromCode(Integer.parseInt(elements[2])),
                AckConversionStatus.SUCCESS);
    }
}
