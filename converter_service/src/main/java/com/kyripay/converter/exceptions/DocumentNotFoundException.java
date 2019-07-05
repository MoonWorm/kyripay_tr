package com.kyripay.converter.exceptions;


import lombok.Getter;


public class DocumentNotFoundException extends RuntimeException
{
  @Getter
  private final String documentId;

  public DocumentNotFoundException(String documentId)
  {
    super(String.format("Document %s not found", documentId));
    this.documentId = documentId;
  }
}
