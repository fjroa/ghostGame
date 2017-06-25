package com.fjroa.ghost;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.fjroa.ghost.game.TrieDictionary;
import com.fjroa.ghost.game.Dictionary;

@SpringBootApplication
public class GhostApplication {

	
	public static void main(String[] args) {
		 SpringApplication.run(GhostApplication.class, args);
	}
	
	@Bean
	public Dictionary dict(@Value("${app.dictFile}") File dictFile) throws FileNotFoundException, IOException {
		return new TrieDictionary(new FileInputStream(dictFile));
	}
}
