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
	private final AnalysisInput input;

	AnalyzeTask(Project project, AnalyzeTaskOptions taskOptions, AnalysisInput input, IAnalysisOutput output) {
		super(project, "title: cohesion", true, new MyPerformInBackgroundOption());

		this.taskOptions = taskOptions;
		this.input = input;
		this.output = output;
		this.project = project;
	}

	public void run(@NotNull ProgressIndicator indicator) {
//		output.addResult(new AnalysisResult("some module"));

		final CohesionAnalyzer cohesionAnalyzer = new CohesionAnalyzer(project, taskOptions, input, output);
		cohesionAnalyzer.run();
	}

	private static class MyPerformInBackgroundOption implements PerformInBackgroundOption {
		public boolean shouldStartInBackground() {
			return false;
		}

		public void processSentToBackground() {
		}
	}
}
