package org.svenehrke.intellij.plugin.cohesion;

import java.util.List;

public interface IAnalysisOutput {
	void addExternalClassNode(CohesionNode inClassNode);

	void printCohesionGraph();

	void setMainNode(CohesionNode inMainNode);

	CohesionNode getMainNode();

	Iterable<CohesionNode> getExternalClassNodes();
}
