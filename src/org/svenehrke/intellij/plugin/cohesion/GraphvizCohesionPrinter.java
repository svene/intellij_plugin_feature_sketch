package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.openapi.util.text.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class GraphvizCohesionPrinter implements ICohesionPrinter {
	private final ICohesionOutputWriter writer;
	private final AnalysisOutput analysisOutput;
	private final List<RelationShip> relationShips = new ArrayList<RelationShip>();

	public GraphvizCohesionPrinter(AnalysisOutput inAnalysisOutput, ICohesionOutputWriter inWriter) {
		analysisOutput = inAnalysisOutput;
		writer = inWriter;
	}

	public void printCohesionGraph() {
		writer.writeLine("digraph FeatureSketchDiagram {");

		// Write cluster for main class:
		writer.writeLine("subgraph cluster_" + analysisOutput.getMainNode().getName() + " {");
		writer.writeLine("node [style=filled];");
		writer.writeLine(String.format("label = \"%s\"", analysisOutput.getMainNode().getName()));
		for (CohesionNode node : analysisOutput.getMainNode().getChildren().values()) {
			_printCohesionGraph(node, 0);
		}
		writer.writeLine("}");

		// Write clusters for external classes:
		for (CohesionNode cohesionNode : analysisOutput.getExternalClassNodes()) {
			writer.writeLine("subgraph cluster_" + cohesionNode.getName() + "class {");
			writer.writeLine("node [style=filled];");
			writer.writeLine(String.format("label = \"%s\"", cohesionNode.getName()));
			writer.writeLine("  }");
		}

		// write relationships between clusters:
		for (RelationShip relationShip : relationShips) {
			writer.writeLine("  " + getCohesionNodeTitle(relationShip.getFrom()) + " -> " + getCohesionNodeTitle(relationShip.getTo()));
		}

		// end of diagram:
		writer.writeLine("}");
	}

	public void _printCohesionGraph(CohesionNode inCohesionNode, int inLevel) {
		int idx = 0;
		final String indent = filledString(inLevel + 2, " ");
		writer.writeLine(indent + getCohesionNodeTitle(inCohesionNode));
		for (CohesionNode node : inCohesionNode.getChildren().values()) {
			if (node.isClass()) { //external class
				relationShips.add(new RelationShip(node, inCohesionNode));
			}
			else {
				writer.writeLine(indent + getCohesionNodeTitle(node) + " -> " + getCohesionNodeTitle(inCohesionNode));
			}
		}
	}

	private static class RelationShip {
		private final CohesionNode from;
		private final CohesionNode to;

		private RelationShip(CohesionNode inFrom, CohesionNode inTo) {
			from = inFrom;
			to = inTo;
		}

		public CohesionNode getFrom() {
			return from;
		}

		public CohesionNode getTo() {
			return to;
		}
	}

	private String getCohesionNodeTitle(CohesionNode inCohesionNode) {
		return '"' + classPrefix(inCohesionNode) + inCohesionNode.getName() + methodPostfix(inCohesionNode) + '"';
	}

	private String methodPostfix(CohesionNode inCohesionNode) {
		return inCohesionNode.isMethod() ? "()" : "";
	}

	private String classPrefix(CohesionNode inCohesionNode) {
		return "";
//		return inCohesionNode.isClass() ? "X " : "";
	}

	public static String filledString(int inHowMany, final String character) {
		return StringUtil.repeat(character, inHowMany);
	}
}
