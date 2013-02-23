package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;

public class CohesionAnalyzer {

	private final Project project;
	private final AnalyzeTaskOptions taskOptions;
	private final AnalysisInput input;
	private final PsiManager psiManager;
	private final IAnalysisOutput output;

	public CohesionAnalyzer(Project inProject, AnalyzeTaskOptions inTaskOptions, AnalysisInput inInput, IAnalysisOutput inOutput) {
		project = inProject;
		taskOptions = inTaskOptions;
		input = inInput;
		output = inOutput;

		psiManager = PsiManager.getInstance(project);
	}

	public void run() {
		ApplicationManager.getApplication().runReadAction(new AnalyzerRunnable());
//		final TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
//		final ConsoleView console = builder.getConsole();
//		console.print("hallo", ConsoleViewContentType.NORMAL_OUTPUT);
	}

	private class AnalyzerRunnable implements Runnable {

		public void run() {
			PsiFile psiFile = psiManager.findFile(input.getCurrentFile());
			if (psiFile != null && JavaLanguage.INSTANCE.equals(psiFile.getLanguage())) {
				PsiJavaFile javaFile = (PsiJavaFile) psiFile;
				final String packageName = javaFile.getPackageName();
				output.addResult(new AnalysisResult(packageName));
			}
		}
	}
}
