package com.ud.iot.spring.config;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.exceptions.DistributionException;
import de.flapdoodle.embed.process.extract.UserTempNaming;

/**
 * Configuración para base de datos mongo embebida.
 * Activa sólo para entornos de desarrollo donde no hay bdd
 * 
 * Se intenta construir un nuevo proceso mongod.
 * Si existe el fichero ejecutable, se intenta eliminar
 * Si no se puede eliminar, se da por hecho que el proceso está arrancado y no es necesario reiniciarlo
 * Si se ha eliminado, se realiza otro intento de construcción de fichero y posteriomente se inicia 
 * 
 * @author Lorenzo
 *
 */
@Configuration
@Profile("dev")
@PropertySource({"classpath:mongo-dev.properties"})
//Importante excluir la configuración automática para evitar el inicio de un segundo proceso
@EnableAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
public class MongoEmbeddedConfig extends AbstractMongoClientConfiguration{
	
	@Value("${mongo.dev.port}") int port;
	@Value("${mongo.dev.host}") String host;
	@Value("${mongo.dev.dbname}") String dbName;

	@Override
	protected String getDatabaseName() {
		return this.dbName;
	}
	
	@PostConstruct
	public void init() {
		IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
	            .defaults(Command.MongoD)
	            .artifactStore(new ExtractedArtifactStoreBuilder()
	                    .defaults(Command.MongoD)
	                    .download(new DownloadConfigBuilder()
	                            .defaultsForCommand(Command.MongoD).build())
	                    .executableNaming(new UserTempNaming()))
	            .build();

	    try {
	    	Net net = new Net(host, port, false);
	    	IMongodConfig mongodConfig = new MongodConfigBuilder()
	    			.version(Version.Main.PRODUCTION)
	    			.net(net)
	    			.replication(new Storage(System.getProperty("user.home") + "/.test-0-storage", null, 0))
	    			.build();
	    	
	    	MongodExecutable mongodExe = null;
	    	try {
	    		mongodExe = MongodStarter.getInstance(runtimeConfig).prepare(mongodConfig);
	    	} catch(DistributionException de) {
	    		if(de.getCause() != null &&
	    				de.getCause() instanceof FileAlreadyExistsException) {
	    			FileAlreadyExistsException faee = (FileAlreadyExistsException)de.getCause();
	    			boolean deleted = new File(faee.getFile()).delete();
	    			if(deleted) {
	    				mongodExe = MongodStarter.getInstance(runtimeConfig).prepare(mongodConfig);
	    			}
	    		}
	    	}
	    	
	    	if(mongodExe != null) {
	    		mongodExe.start();
	    	}
	    	
	    } catch(Exception e) {
	    	System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
	    }
	}
}