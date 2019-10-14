/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.domain.trace;

import com.kyripay.traces.dto.representation.EventRepresentation;
import com.kyripay.traces.dto.request.EventCreationRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * @author M-ASI
 */
@Data
@Embeddable
@NoArgsConstructor
public class Event
{
  private String name;

  private String source;

  @Enumerated(EnumType.STRING)
  private EventType type;

  private String comment;

  private LocalDateTime created;


  public static Event fromCreationRequest(EventCreationRequest creationRequest)
  {
    Event event = new Event();
    event.setName(creationRequest.getName());
    event.setSource(creationRequest.getSource());
    event.setType(creationRequest.getType());
    event.setComment(creationRequest.getComment());
    event.setCreated(LocalDateTime.now());

    return event;
  }


  public EventRepresentation toRepresentation()
  {
    return EventRepresentation.of(name, source, type, comment, created);
  }

}
