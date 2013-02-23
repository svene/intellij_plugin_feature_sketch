package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class AnalyzeTask extends Task.Backgroundable {

	private final IAnalysisOutput output;
	private final AnalyzeTaskOptions taskOptions;
	private final Project project;

	AnalyzeTask(Project project, AnalyzeTaskOptions taskOptions, IAnalysisOutput output) {
		super(project, "title: cohesion", true, new MyPerformInBackgroundOption());

		this.taskOptions = taskOptions;
		this.output = output;
		this.project = project;
	}

	public void run(@NotNull ProgressIndicator indicator) {
		output.addResult(new AnalysisResult());
	}

	private static class MyPerformInBackgroundOption implements PerformInBackgroundOption {
		public boolean shouldStartInBackground() {
			return false;
		}

		public void processSentToBackground() {
		}
	}
}
