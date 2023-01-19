package fr.ensiie.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WSConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/service/*");
    }

    @Bean(name = "trainWsdl")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema trainSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("TrainPort");
        wsdl11Definition.setLocationUri("/service/train");
        wsdl11Definition.setTargetNamespace("http://www.web-service-train-ensiie.com/schema/train");
        wsdl11Definition.setSchema(trainSchema);
        return wsdl11Definition;
    }

    @Bean(name = "clientWsdl")
    public DefaultWsdl11Definition defaultWsdl11DefinitionClient(XsdSchema clientSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ClientPort");
        wsdl11Definition.setLocationUri("/service/client");
        wsdl11Definition.setTargetNamespace("http://www.web-service-train-ensiie.com/schema/client");
        wsdl11Definition.setSchema(clientSchema);
        return wsdl11Definition;
    }

    @Bean(name = "tokenWsdl")
    public DefaultWsdl11Definition defaultWsdl11DefinitionToken(XsdSchema tokenSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("TokenPort");
        wsdl11Definition.setLocationUri("/service/token");
        wsdl11Definition.setTargetNamespace("http://www.web-service-train-ensiie.com/schema/token");
        wsdl11Definition.setSchema(tokenSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema trainSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/train.xsd"));
    }

    @Bean
    public XsdSchema clientSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/client.xsd"));
    }

    @Bean
    public XsdSchema tokenSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/token.xsd"));
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
