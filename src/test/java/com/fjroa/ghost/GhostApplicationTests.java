package com.fjroa.ghost;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjroa.ghost.controllers.AjaxResponseBody;
import com.fjroa.ghost.game.Dictionary;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GhostApplicationTests {

	//En el contexto de test tendremos el siguiente diccionario: winner, loser
	@Autowired
	private MockMvc mvc;

	@Autowired
	private Dictionary dict;
	
	private ObjectMapper jsonMapper = new ObjectMapper();

	public static void main(String[] args) {
		SpringApplication.run(GhostApplication.class, args);
	}

	@Test
	public void getHomePage() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Ghost Game")));
	}

	@Test
	public void testNextPrefix() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/ghost").accept(MediaType.APPLICATION_JSON).content("\"w\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getResult(), "wi");
		Assert.assertEquals(response.getActive(), true);
	}
	
	@Test
	public void testDict() throws Exception {
		Assert.assertEquals(dict.getAnyWordStartingWith("w"), "winner");
		Assert.assertEquals(dict.getAnyWordStartingWith("l"), "loser");
		Assert.assertEquals(dict.getAnyWordStartingWith("g"), null);
	}
	
	@Test
	public void testInvalidPrefix() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/ghost").accept(MediaType.APPLICATION_JSON).content("\"wm\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getResult(), "wm");
		Assert.assertEquals(response.getActive(), false);
	}
	
	@Test
	public void testWordValid() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/ghost").accept(MediaType.APPLICATION_JSON).content("\"loser\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getResult(), "loser");
		Assert.assertEquals(response.getActive(), false);
	}
	
	@Test
	public void testPlayerWins() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/ghost").accept(MediaType.APPLICATION_JSON).content("\"winne\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getResult(), "winne");
		Assert.assertEquals(response.getActive(), false);
	}
	
	@Test
	public void testChallenge() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/challenge").accept(MediaType.APPLICATION_JSON).content("\"lo\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getResult(), "loser");
		Assert.assertEquals(response.getActive(), false);
	}
}
