package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.openapi.util.text.StringUtil;

public class GraphvizCohesionPrinter implements ICohesionPrinter {
	private final CohesionNode cohesionNode;
	private final ICohesionOutputWriter writer;

	public GraphvizCohesionPrinter(CohesionNode inCohesionNode, ICohesionOutputWriter inWriter) {
		cohesionNode = inCohesionNode;
		writer = inWriter;
	}

	public void printCohesionGraph() {
		writer.writeLine("digraph R {");
		for (CohesionNode node : cohesionNode.getChildren().values()) {
			_printCohesionGraph(node, 0);
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
		return '"' +  inCohesionNode.getName() + (inCohesionNode.isMethod() ? "()" : "") + '"';
	}

	public static String filledString(int inHowMany, final String character) {
		return StringUtil.repeat(character, inHowMany);
	}
}
