package com.fjroa.ghost.controllers;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fjroa.ghost.game.Dictionary;

/**
 * The Class GhostController.
 */
@RestController
public class GhostController {

	/** The dict. */
	@Autowired
	Dictionary englishDict;

	@Autowired
	Dictionary spanishDict;

	private Dictionary dict;
	
	
	/**
	 * Gets the next move via ajax.
	 *
	 * @param prefix the prefix
	 * @param errors the errors
	 * @return the next move via ajax
	 */
	@PostMapping("/api/ghost")
	public ResponseEntity<?> getNextMoveViaAjax(@Valid @RequestBody String prefix, Errors errors) {

		AjaxResponseBody result = new AjaxResponseBody();
		prefix = prefix.replace("\"", "");
		if (errors.hasErrors()) {
			result.setMsg(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		if (dict.isWord(prefix)) {
			result.setMsg("You lose. " + prefix + " is a valid word.");
			result.setResult(prefix);
			result.setActive(false);
		} else {
			String nextPrefix = dict.getNextPrefix(prefix);
			if (nextPrefix == null) {
				result.setMsg("You lose. There is no valid word with this prefix.");
				result.setResult(prefix);
				result.setActive(false);
			} else {
				if (dict.isWord(nextPrefix)) {
					result.setMsg("You win. I only can find a valid word : " + nextPrefix);
					result.setResult(prefix);
					result.setActive(false);
				} else {
					result.setMsg("The game is active...");
					result.setResult(nextPrefix);
					result.setActive(true);
				}
			}
		}
		return ResponseEntity.ok(result);

	}
	
	/**
	 * Gets the valid word for finish the game.
	 *
	 * @param prefix the prefix
	 * @param errors the errors
	 * @return the valid word
	 */
	@PostMapping("/api/challenge")
	public ResponseEntity<?> getValidWord(@Valid @RequestBody String prefix, Errors errors) {

		AjaxResponseBody result = new AjaxResponseBody();
		prefix = prefix.replace("\"", "");
		if (errors.hasErrors()) {
			result.setMsg(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		String validWord = dict.getWordStartingWith(prefix);
		if (validWord != null && !validWord.isEmpty()) {
			result.setMsg("You lose. " + validWord + " is a valid word.");
			result.setResult(validWord);
			result.setActive(false);
		} else {
			result.setMsg("You win. I can't find a valid word with this prefix.");
			result.setResult(prefix);
			result.setActive(false);
		}
		
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Starts a new game
	 *
	 * @param lang the language of the dictionary
	 * @param errors the errors
	 * @return the status
	 */
	@PostMapping("/api/start")
	public ResponseEntity<?> startGame(@Valid @RequestBody String lang, Errors errors) {

		AjaxResponseBody result = new AjaxResponseBody();
		lang = lang.replace("\"", "");
		if (errors.hasErrors()) {
			result.setMsg(
					errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
			return ResponseEntity.badRequest().body(result);
		}

		if (lang.equals("es")) {
			dict = spanishDict;
		} else {
			dict = englishDict;
		}
		
		return ResponseEntity.ok(result);
	}
	
	@PostConstruct
	public void cargarDictDefecto() {
		dict = englishDict;
	}
}
