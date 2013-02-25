package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.openapi.util.text.StringUtil;

public class DevelopmentCohesionPrinter implements ICohesionPrinter {
	private final CohesionNode cohesionNode;
	private final ICohesionOutputWriter writer;

	public DevelopmentCohesionPrinter(CohesionNode inCohesionNode, ICohesionOutputWriter inWriter) {
		cohesionNode = inCohesionNode;
		writer = inWriter;
	}

	public void printCohesionGraph() {
		for (CohesionNode node : cohesionNode.getChildren().values()) {
			_printCohesionGraph(node, 0);
		}
		writer.writeLine("DONE");
	}

	public void _printCohesionGraph(CohesionNode inCohesionNode, int inLevel) {
		int idx = 0;
		final String indent = filledString(inLevel + 2, " ");
		if (inCohesionNode.getChildren().isEmpty()) {
			writer.writeLine(inCohesionNode.getName() + ": -");
		}
		else {
			StringBuilder sb = new StringBuilder(indent);
			writer.writeLine(inCohesionNode.getName() + " used by:");
			for (CohesionNode node : inCohesionNode.getChildren().values()) {
				if (idx > 0) sb.append(", ");
				sb.append(node.getName());
				idx++;
			}
			writer.writeLine(sb.toString());
		}
	}

	public static String filledString(int inHowMany, final String character) {
		return StringUtil.repeat(character, inHowMany);
	}
}
