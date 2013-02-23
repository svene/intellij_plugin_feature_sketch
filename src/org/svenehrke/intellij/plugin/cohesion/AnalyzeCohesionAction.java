package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;

public class AnalyzeCohesionAction extends AnAction {
	@Override
	public void actionPerformed(AnActionEvent actionEvent) {
		Project project = actionEvent.getData(PlatformDataKeys.PROJECT);
//		showMessage(project, "Hello, " + "!\n I am glad to see you.");
		VirtualFile currentFile = DataKeys.VIRTUAL_FILE.getData(actionEvent.getDataContext());
//		String s = currentFile != null ? currentFile.getName() : "NO FILE";
//		showMessage(project, s);

		AnalysisInput input = new AnalysisInput(currentFile);

		AnalysisOutput output = new AnalysisOutput(project, ToolWindowManager.getInstance(project));
		output.init();

		AnalyzeTask task = new AnalyzeTask(project, new AnalyzeTaskOptions(), input, output);
		ProgressManager.getInstance().run(task);
	}

	private void showMessage(Project inProject, final String inMessage) {
		Messages.showMessageDialog(inProject, inMessage, "Information", Messages.getInformationIcon());
	}

}