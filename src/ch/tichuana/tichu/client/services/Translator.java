package ch.tichuana.tichu.client.services;

import java.util.Locale;
import java.util.ResourceBundle;

public class Translator {

	private Locale locale;
	private ResourceBundle resourceBundle;

	/**
	 * Translator class loads properties from a resource bundle. Based on the JavaFX-App-Template.
	 * @author Christian
	 * @param localeString to identify the language according to a String object
	 */
	public Translator(String localeString) {
		ServiceLocator serviceLocator= ServiceLocator.getServiceLocator();
		String path = serviceLocator.getConfiguration().getProperty("resourceBundle.path");
		this.locale = new Locale(localeString);
		this.resourceBundle = ResourceBundle.getBundle(path, this.locale);
	}

	/**
	 * Alternate Constructor, locale as parameter
	 * @author Christian
	 * @param locale to identify the language according to a Locale object
	 */
	public Translator(Locale locale) {
		ServiceLocator serviceLocator= ServiceLocator.getServiceLocator();
		String path = serviceLocator.getConfiguration().getProperty("resourceBundle.path");
		this.locale = locale;
		this.resourceBundle = ResourceBundle.getBundle(path, this.locale);
	}


	/**
	 * Returns the value belonging to its key.
	 * @author Christian
	 * @param key identify the property by key
	 * @return string in a given language
	 */
	public String getString(String key) {
		return resourceBundle.getString(key);
	}





}