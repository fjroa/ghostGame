package com.fjroa.ghost.controllers;

import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fjroa.ghost.game.IDictionary;

/**
 * The Class GhostController.
 */
@RestController
public class GhostController {

	/** The dict. */
	@Autowired
	IDictionary englishDict;

	@Autowired
	IDictionary spanishDict;

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
		
		IDictionary dict = getDict();
		
		if (dict.isWord(prefix)) {
			result.setMsg("You lose. " + prefix + " is a valid word.");
			result.setResult(prefix);
			result.setLetter(null);
		} else {
			String nextPrefix = dict.getNextPrefix(prefix);
			if (nextPrefix == null) {
				result.setMsg("You lose. There is no valid word with this prefix.");
				result.setResult(prefix);
				result.setLetter(null);
			} else {
				if (dict.isWord(nextPrefix)) {
					result.setMsg("You win. I only can find a valid word : " + nextPrefix);
					result.setResult(prefix);
					result.setLetter(null);
				} else {
					result.setMsg("The game is active...");
					result.setResult(nextPrefix);
					result.setLetter(nextPrefix.substring(nextPrefix.length()-1));
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
		IDictionary dict = getDict();
		String validWord = dict.getWordStartingWith(prefix);
		if (validWord != null && !validWord.isEmpty()) {
			result.setMsg("You lose. " + validWord + " is a valid word.");
			result.setResult(validWord);
			result.setLetter(null);
		} else {
			result.setMsg("You win. I can't find a valid word with this prefix.");
			result.setResult(prefix);
			result.setLetter(null);
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
	public IDictionary getDict() {
		Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			return spanishDict;
		} else {
			return englishDict;
		}
	}
}
