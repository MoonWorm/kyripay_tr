/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.domain.trace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * @author M-ASI
 */
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Header
{
  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "VALUE", nullable = false)
  private String value;
}
