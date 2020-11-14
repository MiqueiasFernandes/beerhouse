package com.beerhouse.config;

import io.github.jhipster.config.h2.H2ConfigurationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.io.File;
import java.io.FileWriter;

/**
 * Configuration of H2 embebed database.
 */
@Configuration
public class DatabasConfiguration implements ServletContextInitializer {

    private final Logger log = LoggerFactory.getLogger(DatabasConfiguration.class);
    private final Environment env;

    DatabasConfiguration(Environment env) {
        this.env = env;
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        initH2Console(servletContext);
    }

    /**
     * Initializes H2 console.
     */
    private void initH2Console(ServletContext servletContext) {
        log.info("Inicializando H2 database.");
        try {
            String dbURL = env.getProperty("spring.datasource.url", "");
            if (new File("src/main/resources/").exists()) {
                H2ConfigurationHelper.initH2Console(servletContext);
            } else {
                if (dbURL.startsWith("jdbc:h2:file:")) {
                    String path = dbURL.split(":")[3].split(";")[0];
                    File dir = new File(path);
                    if (!dir.isDirectory()) {
                        if (dir.isFile()) {
                            throw new Exception("Diretorio já existe como arquivo");
                        }
                        if (!dir.mkdirs()) {
                            throw new Exception("Falhou ao criar o diretorio: " + dir.getAbsolutePath());
                        }
                    }
                    String fileDBProperties = path + "/.h2.server.properties";

                    if (!new File(fileDBProperties).isFile()) {
                        FileWriter myWriter = new FileWriter(fileDBProperties);
                        myWriter.write("0=JHipster H2 (Disk)|org.h2.Driver|jdbc\\:h2\\:file\\:" + path + "|sa\n");
                        myWriter.write("webAllowOthers=true\n");
                        myWriter.write("webPort=8082\n");
                        myWriter.write("webSSL=false\n");
                        myWriter.close();
                        log.debug("Arquivo de propriedades do H2 atualizado com sucesso.");
                    }
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    Class<?> servletClass = Class.forName("org.h2.server.web.WebServlet", true, loader);
                    Servlet servlet = (Servlet) servletClass.getDeclaredConstructor().newInstance();

                    ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console", servlet);
                    h2ConsoleServlet.addMapping("/h2-console/*");
                    h2ConsoleServlet.setInitParameter("-properties", path);
                    h2ConsoleServlet.setLoadOnStartup(1);
                    log.info("H2 configurado com sucesso em: " + path);
                } else {
                    throw new Exception("URL do banco de dados H2 invalido: " + dbURL);
                }
            }
        } catch (Exception e) {
            log.error("Falhou ao atualizar o arquivo de propriedades do H2.", e);
        }
    }
}
