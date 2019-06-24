package com.kyripay.notification.service;

import java.util.Map;

public interface TemplateResolver {

    String resolveText(String templateId, Map<String, Object> params);

}
