package org.svenehrke.intellij.plugin.cohesion;

public interface IAnalysisOutput {
	void addResult(AnalysisResult inAnalysisResult);

	void addCohesionNode(CohesionNode inClassNode);

	void writeLine(String inString);

	void printCohesionGraph();
}
