/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.domain.trace;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author M-ASI
 */
@Data
@NoArgsConstructor
public class TraceCreationRequest
{
  private long paymentId;
}
