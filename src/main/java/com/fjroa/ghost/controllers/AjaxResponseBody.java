package com.fjroa.ghost.controllers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class AjaxResponseBody.
 */
public class AjaxResponseBody {

	/** The msg. */
	private final String msg;

	/** The result. */
	private final String result;

	/** The msg. */
	private final Character letter;

	private Set<String> availablePrefixes;

	public AjaxResponseBody(Errors errors) {
		this.msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(","));
		this.result = null;
		this.letter = null;
	}

	@JsonCreator
	public AjaxResponseBody(@JsonProperty("msg") String msg, @JsonProperty("result") String result,
			@JsonProperty("letter") Character letter, @JsonProperty("availablePrefixes") Set<String> availablePrefixes) {
		super();
		this.msg = msg;
		this.result = result;
		this.letter = letter;
		this.availablePrefixes = availablePrefixes;
	}

	/**
	 * Gets the msg.
	 *
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	public Character getLetter() {
		return letter;
	}
	
	public Set<String> getAvailablePrefixes() {
		return availablePrefixes;
	}
}
