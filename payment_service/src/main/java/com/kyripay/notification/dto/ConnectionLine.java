/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
public class ConnectionLine
{

  @ApiModelProperty(value = "Connection protocol", example = "http")
  private String protocol;
  @ApiModelProperty(value = "Connection host", example = "payment-service.com")
  private String host;
  @ApiModelProperty(value = "Connection port", example = "53")
  private int port;
  @ApiModelProperty(value = "Host relative path/folder", example = "/pay")
  private String path;
  @ApiModelProperty(value = "Username/login name that is might be required for secured connections", example = "john.doe")
  private String login;
  @ApiModelProperty(value = "Password that is might be required for secured connections", example = "abc$def!")
  private String password;


  public static ConnectionLineBuilder connectionLineBuilder()
  {
    return new ConnectionLineBuilder();
  }


  public static class ConnectionLineBuilder
  {

    private ConnectionLine instance = new ConnectionLine();


    public ConnectionLineBuilder protocol(String protocol)
    {
      instance.setProtocol(protocol);
      return this;
    }


    public ConnectionLineBuilder host(String host)
    {
      instance.setHost(host);
      return this;
    }


    public ConnectionLineBuilder port(int port)
    {
      instance.setPort(port);
      return this;
    }


    public ConnectionLineBuilder path(String path)
    {
      instance.setPath(path);
      return this;
    }


    public ConnectionLineBuilder login(String login)
    {
      instance.setLogin(login);
      return this;
    }


    public ConnectionLineBuilder password(String password)
    {
      instance.setPassword(password);
      return this;
    }


    public ConnectionLine build()
    {
      return instance;
    }

  }

}