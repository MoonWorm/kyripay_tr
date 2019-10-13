package com.kyripay.payment.domain;

import com.kyripay.payment.domain.vo.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCriterias {

    private Status status;

}
