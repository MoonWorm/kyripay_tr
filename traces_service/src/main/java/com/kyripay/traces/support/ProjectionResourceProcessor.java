/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.support;

import org.springframework.data.projection.TargetAware;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;


/**
 * Workaround for this <a href="https://jira.spring.io/browse/DATAREST-713">issue</a>.
 * Solution found <a href="https://stackoverflow.com/questions/47048099/spring-data-rest-resourceprocessor-not-applied-on-projections">here</a>.
 *
 * Projections need their own resource processors in spring-data-rest.
 * To avoid code duplication the ProjectionResourceProcessor delegates the link creation to
 * the resource processor of the underlying entity.
 * @param <E> entity type the projection is associated with
 * @param <T> the resource type that this ResourceProcessor is for
 */
public class ProjectionResourceProcessor<E extends Identifiable, T> implements ResourceProcessor<Resource<T>>
{

  private final ResourceProcessor<Resource<E>> entityResourceProcessor;

  public ProjectionResourceProcessor(ResourceProcessor<Resource<E>> entityResourceProcessor) {
    this.entityResourceProcessor = entityResourceProcessor;

  }

  @SuppressWarnings("unchecked")
  @Override
  public Resource<T> process(Resource<T> resource) {
    if (resource.getContent() instanceof TargetAware) {
      TargetAware targetAware = (TargetAware) resource.getContent();
      if (targetAware != null
          && targetAware.getTarget() != null
          && targetAware.getTarget() instanceof Identifiable) {
        E target = (E) targetAware.getTarget();
        resource.add(entityResourceProcessor.process(new Resource<>(target)).getLinks());
      }
    }
    return resource;
  }

}