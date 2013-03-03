package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.openapi.util.text.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class GraphvizCohesionPrinter implements ICohesionPrinter {
	private final ICohesionOutputWriter writer;
	private List<CohesionNode> cohesionNodes = new ArrayList<CohesionNode>();

	public GraphvizCohesionPrinter(List<CohesionNode> inCohesionNodes, ICohesionOutputWriter inWriter) {
		cohesionNodes = inCohesionNodes;
		writer = inWriter;
	}

	public void printCohesionGraph() {
		writer.writeLine("digraph FeatureSketchDiagram {");
		for (CohesionNode cohesionNode : cohesionNodes) {
			writer.writeLine("subgraph cluster_" + cohesionNode.getName() + "class {");
			writer.writeLine("node [style=filled];");
			writer.writeLine(String.format("label = \"%s\"", cohesionNode.getName()));
			for (CohesionNode node : cohesionNode.getChildren().values()) {
				_printCohesionGraph(node, 0);
			}
			writer.writeLine("  }");
		}
		writer.writeLine("}");
	}

	public void _printCohesionGraph(CohesionNode inCohesionNode, int inLevel) {
		int idx = 0;
		final String indent = filledString(inLevel + 2, " ");
		writer.writeLine(indent + getCohesionNodeTitle(inCohesionNode));
		for (CohesionNode node : inCohesionNode.getChildren().values()) {
			writer.writeLine(indent + getCohesionNodeTitle(node) + " -> " + getCohesionNodeTitle(inCohesionNode));
		}
	}

	private String getCohesionNodeTitle(CohesionNode inCohesionNode) {
		return '"' + classPrefix(inCohesionNode) + inCohesionNode.getName() + methodPostfix(inCohesionNode) + '"';
	}

	private String methodPostfix(CohesionNode inCohesionNode) {
		return inCohesionNode.isMethod() ? "()" : "";
	}

	private String classPrefix(CohesionNode inCohesionNode) {
		return inCohesionNode.isClass() ? "X " : "";
	}

	public static String filledString(int inHowMany, final String character) {
		return StringUtil.repeat(character, inHowMany);
	}
}
