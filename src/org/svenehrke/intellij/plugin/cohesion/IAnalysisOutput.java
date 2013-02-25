package org.svenehrke.intellij.plugin.cohesion;

public interface IAnalysisOutput {
	void addResult(AnalysisResult inAnalysisResult);

	void addCohesionNode(CohesionNode inClassNode);

	void printCohesionGraph();
}
