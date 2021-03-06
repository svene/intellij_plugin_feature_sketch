package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashSet;
import java.util.Set;

public class AnalysisOutput implements IAnalysisOutput {
	private final Project project;
	private final ToolWindowManager toolWindowManager;
	private UsageTreeNode root;

	private CohesionNode mainNode;
	private Set<CohesionNode> externalClassNodes = new HashSet<CohesionNode>();
	private ConsoleView console;

	public AnalysisOutput(Project inProject, ToolWindowManager inInstance) {
		this.project = inProject;
		this.toolWindowManager = inInstance;
	}

	public void setMainNode(CohesionNode inMainNode) {
		mainNode = inMainNode;
	}

	public CohesionNode getMainNode() {
		return mainNode;
	}

	public Iterable<CohesionNode> getExternalClassNodes() {
		return externalClassNodes;
	}

	public void init() {
		ToolWindow toolWindow = toolWindowManager.getToolWindow(ToolWindowId.DEPENDENCIES);
		if (toolWindow == null) {
			toolWindow = toolWindowManager.registerToolWindow(ToolWindowId.DEPENDENCIES, true, ToolWindowAnchor.BOTTOM, project, true);
		}
		toolWindow.setToHideOnEmptyContent(true);
		ContentManager contentManager = toolWindow.getContentManager();
		root = new UsageTreeNode(Strings.COHESION_RESULTS, null);
//		JTree myTree = new JTree(root);
//		Content content = ContentFactory.SERVICE.getInstance().createContent(myTree, Strings.ANALYSIS, false);
//		contentManager.addContent(content);

		final TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
		console = builder.getConsole();

		Content content2 = ContentFactory.SERVICE.getInstance().createContent(console.getComponent(), Strings.ANALYSIS_CONSOLE, false);
		contentManager.addContent(content2);
	}

	public void printCohesionGraph() {
//		final ICohesionPrinter cohesionPrinter = new DevelopmentCohesionPrinter(cohesionNode, new DefaultCohesionOutputWriter());
		final ICohesionPrinter cohesionPrinter = new GraphvizCohesionPrinter(this, new DefaultCohesionOutputWriter());
		cohesionPrinter.printCohesionGraph();
	}

	public void addExternalClassNode(CohesionNode inExternalClassNode) {
		externalClassNodes.add(inExternalClassNode);
	}

	static class UsageTreeNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = -3278551194929568326L;
		private final String name;

		UsageTreeNode(String name, UsageTreeNode parent) {
			super(parent);
			this.name = name;
		}

		@Override
		public String toString() {
			return getName();
		}

		String getName() {
			return name;
		}


		public UsageTreeNode clone() {
			return (UsageTreeNode) super.clone();
		}
	}

	private class DefaultCohesionOutputWriter implements ICohesionOutputWriter {
		public void writeLine(String inString) {
			console.print(inString + "\n", ConsoleViewContentType.NORMAL_OUTPUT);
		}
	}
}
