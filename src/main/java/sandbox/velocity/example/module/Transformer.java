package sandbox.velocity.example.module;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import sandbox.velocity.example.vo.RowData;

import java.io.StringWriter;

public class Transformer {
    private VelocityEngine engine;
    private void engineInit(){
        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();
    }

    public String generate(RowData rowData) {
        engineInit();

        VelocityContext context = new VelocityContext();
        context.put("packageName", rowData.getPackageName());
        context.put("className", rowData.getClassName());
        context.put("properties", rowData.getProperties());

        StringWriter writer = new StringWriter();
        Template template = engine.getTemplate("templates/fact_class.vm");
        template.merge(context, writer);
        return writer.toString();
    }
}
