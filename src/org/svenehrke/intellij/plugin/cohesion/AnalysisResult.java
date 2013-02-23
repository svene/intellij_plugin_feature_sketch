package org.svenehrke.intellij.plugin.cohesion;

public class AnalysisResult {
	private final String value;

	public AnalysisResult(String inValue) {
		value = inValue;
	}

	public String getValue() {
		return value;
	}
}
