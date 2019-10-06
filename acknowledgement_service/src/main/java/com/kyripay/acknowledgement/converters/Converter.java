package com.kyripay.acknowledgement.converters;

import com.kyripay.acknowledgement.dto.Acknowledgement;
import com.kyripay.acknowledgement.dto.ConvertedAcknowledgement;

public interface Converter {

    ConvertedAcknowledgement convert(Acknowledgement acknowledgement);
}
