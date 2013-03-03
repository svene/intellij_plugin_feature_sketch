package org.svenehrke.intellij.plugin.cohesion;

public interface IAnalysisOutput {
	void addCohesionNode(CohesionNode inClassNode);

	void printCohesionGraph();
}
