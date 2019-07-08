package com.kyripay.notification.service;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

@Component
public class TwigTemplateResolver implements TemplateResolver {

    private static final String TEMPLATES_FOLDER = "templates";
    private static final String TEMPLATE_EXTENSION = ".twig";

    @Override
    public String resolveText(String templateId, Map<String, Object> params) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(TEMPLATES_FOLDER
                + File.separator + templateId + TEMPLATE_EXTENSION);
        JtwigModel model = JtwigModel.newModel();
        params.entrySet().forEach(e -> model.with(e.getKey(), e.getValue()));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        template.render(model, os);
        return new String(os.toByteArray(), Charset.defaultCharset());
    }

}