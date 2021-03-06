package com.fjroa.ghost;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.fjroa.ghost.controllers.GhostController;
import com.fjroa.ghost.dict.IDictionary;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GhostApplicationTests {

	/** The mvc. */
	@Autowired
	private MockMvc mvc;

	/** The dict. */
	@Autowired
	@Qualifier("englishDict")
	private IDictionary dict;
	
	/** The json mapper. */
	private ObjectMapper jsonMapper = new ObjectMapper();

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(GhostApplication.class, args);
	}

	/**
	 * Gets the home page.
	 *
	 * @return the home page
	 * @throws Exception the exception
	 */
	@Test
	public void getHomePage() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Ghost Game")));
	}

	/**
	 * Test next prefix.
	 * En el contexto de test tendremos el siguiente diccionario: winner, loser
	 * Al envio de w espera que devuelva wi
	 * @throws Exception the exception
	 */
	@Test
	public void testNextPrefix() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/ghost").accept(MediaType.APPLICATION_JSON).content("\"w\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getMsg(), GhostController.PLAY);
		Assert.assertEquals(response.getResult(), "wi");
		Assert.assertEquals(response.getLetter(), new Character('i'));
	}
	
	/**
	 * Test next prefix when the next is word but exist another path.
	 * En el contexto de test tendremos el siguiente diccionario: winner, loser, wind
	 * Al envio de win espera que devuelva winn en lugar de wind
	 * @throws Exception the exception
	 */
	@Test
	public void testNextPrefix2() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/ghost").accept(MediaType.APPLICATION_JSON).content("\"win\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getMsg(), GhostController.PLAY);
		Assert.assertEquals(response.getResult(), "winn");
		Assert.assertEquals(response.getLetter(), new Character('n'));
	}
	
	/**
	 * Test dict.
	 * En el contexto de test tendremos el siguiente diccionario:  winner, loser, wind
	 * Al envio de w espera que devuelva wind
	 * Al envio de l espera que devuelva loser
	 * Al envio de g espera que devuelva null
	 * @throws Exception the exception
	 */
	@Test
	public void testDict() throws Exception {
		Assert.assertEquals(dict.getFirstWordStartingWith("w"), "wind");
		Assert.assertEquals(dict.getFirstWordStartingWith("l"), "loser");
		Assert.assertEquals(dict.getFirstWordStartingWith("g"), null);
	}
	
	/**
	 * Test invalid prefix.
	 * En el contexto de test tendremos el siguiente diccionario:  winner, loser, wind
	 * Al envio de wm espera que devuelva fin de la partida
	 * @throws Exception the exception
	 */
	@Test
	public void testInvalidPrefix() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/ghost").accept(MediaType.APPLICATION_JSON).content("\"wm\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getMsg(), GhostController.LOSE);
		Assert.assertEquals(response.getResult(), "wm");
		Assert.assertEquals(response.getLetter(), null);
	}
	
	/**
	 * Test word valid.
	 * Test invalid prefix.
	 * En el contexto de test tendremos el siguiente diccionario: winner, loser, wind
	 * Al envio de loser espera que devuelva fin de la partida
	 * @throws Exception the exception
	 */
	@Test
	public void testWordValid() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/ghost").accept(MediaType.APPLICATION_JSON).content("\"loser\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getMsg(), GhostController.LOSE);
		Assert.assertEquals(response.getResult(), "loser");
		Assert.assertEquals(response.getLetter(), null);
	}
	
	/**
	 * Test player wins.
	 * En el contexto de test tendremos el siguiente diccionario:  winner, loser, wind
	 * Al envio de winne espera que devuelva fin de la partida
	 * @throws Exception the exception
	 */
	@Test
	public void testPlayerWins() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/ghost").accept(MediaType.APPLICATION_JSON).content("\"winne\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getMsg(), GhostController.WIN);
		Assert.assertEquals(response.getResult(), "winner");
		Assert.assertEquals(response.getLetter(), null);
	}
	
	/**
	 * Test challenge.
	 * En el contexto de test tendremos el siguiente diccionario:  winner, loser, wind
	 * Al envio de lo espera que devuelva loser y fin de la partida
	 * @throws Exception the exception
	 */
	@Test
	public void testGiveup() throws Exception {
		final MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/api/giveup").accept(MediaType.APPLICATION_JSON).content("\"lo\""))
				.andExpect(status().isOk()).andReturn();
		AjaxResponseBody response = jsonMapper.readValue(result.getResponse().getContentAsString(),
				AjaxResponseBody.class);
		Assert.assertEquals(response.getMsg(), GhostController.LOSE);
		Assert.assertEquals(response.getResult(), "loser");
		Assert.assertEquals(response.getLetter(), null);
	}
}
