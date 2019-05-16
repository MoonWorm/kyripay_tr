/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author M-ASI
 */
public class TracesLogger
{
  private static Logger logger = LoggerFactory.getLogger("kyripay.traces");


  public static void trace(String msg)
  {
    logger.trace(msg);
  }


  public static void debug(String msg)
  {
    logger.debug(msg);
  }


  public static void info(String msg)
  {
    logger.info(msg);
  }


  public static void info(String format, Object arg)
  {
    logger.info(format, arg);
  }


  public static void info(String format, Object arg1, Object arg2)
  {
    logger.info(format, arg1, arg2);
  }


  public static void info(String format, Object... arguments)
  {
    logger.info(format, arguments);
  }


  public static void info(String msg, Throwable t)
  {
    logger.info(msg, t);
  }


  public static void warn(String msg)
  {
    logger.warn(msg);
  }


  public static void warn(String msg, Throwable t)
  {
    logger.warn(msg, t);
  }


  public static void error(String msg)
  {
    logger.error(msg);
  }


  public static void error(String msg, Throwable t)
  {
    logger.error(msg, t);
  }
}
