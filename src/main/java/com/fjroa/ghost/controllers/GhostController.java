package com.fjroa.ghost.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fjroa.ghost.dict.IDictionary;

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

	public static String WIN = "WIN";
	public static String LOSE = "LOSE";
	public static String LOSE2 = "LOSE2";
	public static String GIVEUP = "GIVEUP";
	public static String PLAY = "PLAY";

	/**
	 * Gets the next move.
	 *
	 * @param prefix
	 *            the prefix
	 * @param errors
	 *            the errors
	 * @return the next move via ajax
	 */
	@PostMapping("/api/ghost")
	public ResponseEntity<?> getNextMove(@Valid @RequestBody String prefix, Errors errors) {
		// 0.- Check if there are errors in the ajax request
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(new AjaxResponseBody(errors));
		}
		prefix = prefix.replaceAll("\"", "");
		IDictionary dict = getDict();
		// 1.- Check if the input is a valid word in the dictionary
		if (dict.isWord(prefix)) {
			// 1.1 - If the current prefix is a valid word, player loses
			return ResponseEntity.ok(new AjaxResponseBody(LOSE, prefix, null, null));
		}
		// 2.- Find a letter to continue the game
		Set<Character> nextLetters = dict.getNextLetters(prefix);
		if (nextLetters == null) {
			// 2.1 - If the prefix is not part of a valid word, player loses
			return ResponseEntity.ok(new AjaxResponseBody(LOSE2, prefix, null, null));
		}
		Character nextLetter = getRandomNextLetter(prefix, nextLetters, dict);
		String nextPrefix = prefix + nextLetter;
		// 3.- Check if the new prefix is a valid word in the dictionary
		if (dict.isWord(nextPrefix)) {
			// 3.1 - If the next prefix is a valid word, player wins
			return ResponseEntity.ok(new AjaxResponseBody(WIN, nextPrefix, null, getNextValidWords(nextPrefix, dict)));
		}

		// 4.- The prefix and the nextPrefix are valid, the game have not finished.
		return ResponseEntity.ok(new AjaxResponseBody(PLAY, nextPrefix, nextLetter, getNextValidWords(nextPrefix, dict)));

	}

	/**
	 * Gets the valid word for finish the game.
	 *
	 * @param prefix
	 *            the prefix
	 * @param errors
	 *            the errors
	 * @return the valid word
	 */
	@PostMapping("/api/giveup")
	public ResponseEntity<?> getValidWord(@Valid @RequestBody String prefix, Errors errors) {

		// 0.- Check if there are errors in the ajax request
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(new AjaxResponseBody(errors));
		}
		prefix = prefix.replaceAll("\"", "");
		IDictionary dict = getDict();
		String validWord = dict.getFirstWordStartingWith(prefix);
		if (validWord != null && !validWord.isEmpty()) {
			return ResponseEntity.ok(new AjaxResponseBody(GIVEUP, validWord, null, null));
		}

		return null;
	}

	private Character getRandomNextLetter(String prefix, Set<Character> nextLetters, IDictionary dict) {
		List<Character> randomList = new ArrayList<Character>(nextLetters);
		Collections.shuffle(randomList);
		for (Character c : randomList) {
			if (!dict.isWord(prefix + c)) {
				return c;
			}
		}
		return randomList.get(0);
	}
	
	private Set<String> getNextValidWords(String nextPrefix, IDictionary dict) {
		Set<Character> nextLetters = dict.getNextLetters(nextPrefix);
		if (nextLetters == null) {
			return null;
		}
		Set<String> returnSet = new HashSet<String>();
		for (Character c : nextLetters) {
			returnSet.add(nextPrefix + c); 
		}
		return returnSet;
	}
	
	public IDictionary getDict() {
		Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			return spanishDict;
		} else {
			return englishDict;
		}
	}
}
