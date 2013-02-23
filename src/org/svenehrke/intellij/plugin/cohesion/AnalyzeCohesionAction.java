package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;

public class AnalyzeCohesionAction extends AnAction {
	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getData(PlatformDataKeys.PROJECT);
		AnalysisOutput output = new AnalysisOutput(project, ToolWindowManager.getInstance(project));
		output.init();
		AnalyzeTask task = new AnalyzeTask(project, new AnalyzeTaskOptions(), output);
		ProgressManager.getInstance().run(task);
	}

}