package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class AnalysisOutput implements IAnalysisOutput {
	private final Project project;
	private final ToolWindowManager toolWindowManager;
	private UsageTreeNode root;

	public AnalysisOutput(Project inProject, ToolWindowManager inInstance) {
		this.project = inProject;
		this.toolWindowManager = inInstance;
	}

	public void init() {
		ToolWindow toolWindow = toolWindowManager.getToolWindow(ToolWindowId.DEPENDENCIES);
		if (toolWindow == null) {
			toolWindow = toolWindowManager.registerToolWindow(ToolWindowId.DEPENDENCIES, true, ToolWindowAnchor.BOTTOM, project, true);
		}
		toolWindow.setToHideOnEmptyContent(true);
		ContentManager contentManager = toolWindow.getContentManager();
		root = new UsageTreeNode(Strings.COHESION_RESULTS, null);
		JTree myTree = new JTree(root);
		Content content = ContentFactory.SERVICE.getInstance().createContent(myTree, Strings.ANALYSIS, false);
		contentManager.addContent(content);
	}

	public void addResult(AnalysisResult inAnalysisResult) {
		UsageTreeNode moduleNode = new UsageTreeNode("some module", root);
		root.add(moduleNode);
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
	}}
