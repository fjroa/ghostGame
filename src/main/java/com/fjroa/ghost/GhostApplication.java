package com.fjroa.ghost;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fjroa.ghost.game.Dictionary;
import com.fjroa.ghost.game.TrieDictionary;

@SpringBootApplication
public class GhostApplication {

	
	public static void main(String[] args) {
		 SpringApplication.run(GhostApplication.class, args);
	}
	
	@Bean
	public Dictionary englishDict(@Value("${app.dictFile}") String dictFile) throws FileNotFoundException, IOException {
		return new TrieDictionary(getClass().getClassLoader().getResourceAsStream(dictFile));
	}
	
	@Bean
	public Dictionary spanishDict(@Value("${app.spanishDictFile}") String spanishDictFile) throws FileNotFoundException, IOException {
		return new TrieDictionary(getClass().getClassLoader().getResourceAsStream(spanishDictFile));
	}
}
