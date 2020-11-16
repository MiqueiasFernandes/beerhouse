package com.beerhouse.config;

import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.h2.H2ConfigurationHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
        if (!env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_TEST))) {
            initH2Console(servletContext);
        }
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
                    String db_path = dbURL.split(":")[3].split(";")[0];
                    String path = db_path.substring(0, db_path.lastIndexOf("/"));
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
                        myWriter.write("0=JHipster H2 (Disk)|org.h2.Driver|jdbc\\:h2\\:file\\:" + db_path + "|sa\n");
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
            //Se o cliente setou a propriedade para popular o banco a partir de arquivo
            String basic_data = env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.import_files");
            basic_data = basic_data == null ? env.getProperty("spring.datasource.data") : basic_data;
            if (basic_data != null) {
                // Criar um arquivo basico de modelo, caso o cliente não tenhha
                File basic_data_file = new File(basic_data.replace("file:./", "./"));
                if (!basic_data_file.exists()) {
                    log.debug("Criando arquivo de inicialização do banco de dados basico.");
                    ClassPathResource classPathResource = new ClassPathResource("basic_data.sql");
                    InputStream inputStream = classPathResource.getInputStream();
                    Files.copy(inputStream, basic_data_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    IOUtils.closeQuietly(inputStream);
                    log.info("Arquivo SQL salvo em " + basic_data_file);
                }
            }
        } catch (Exception e) {
            log.error("Falhou ao atualizar o arquivo de propriedades do H2.", e);
        }
    }

}
