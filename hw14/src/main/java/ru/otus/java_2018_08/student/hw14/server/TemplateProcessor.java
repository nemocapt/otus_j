package ru.otus.java_2018_08.student.hw14.server;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@Component
public class TemplateProcessor {
    private static final String HTML_DIR = "/template";

    private final Configuration configuration;
    private String dir;

    public TemplateProcessor() throws IOException {
        this(HTML_DIR);
    }

    public TemplateProcessor(String dir) throws IOException {
        this.dir = dir;

        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassForTemplateLoading(getClass(), HTML_DIR);
        configuration.setDefaultEncoding("UTF-8");
    }

    public String getPage(String filename, Map<String, Object> data) throws IOException {
        try (Writer stream = new StringWriter()) {
            Template template = configuration.getTemplate(filename);
            template.process(data, stream);
            return stream.toString();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
