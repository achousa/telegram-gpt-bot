package es.achousa.utils;

import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Json2Pojo {

    public static void main(String[] args) throws Exception {
        convertJsonToJavaClass(new File("/home/tony/pojos/response.json").toURI().toURL(),new File("/home/tony/pojos"),"es.achousa.model.response", "ChatResponse");
        convertJsonToJavaClass(new File("/home/tony/pojos/request.json").toURI().toURL(),new File("/home/tony/pojos"),"es.achousa.model.request", "ChatRequest");
    }

    public static void convertJsonToJavaClass(URL inputJsonUrl, File outputJavaClassDirectory, String packageName, String javaClassName)
            throws IOException {
        JCodeModel jcodeModel = new JCodeModel();

        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() {
                return true;
            }

            @Override
            public SourceType getSourceType() {
                return SourceType.JSON;
            }
        };

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(jcodeModel, javaClassName, packageName, inputJsonUrl);

        jcodeModel.build(outputJavaClassDirectory);
    }


}
