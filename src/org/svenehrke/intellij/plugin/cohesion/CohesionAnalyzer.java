package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;

import java.util.Collection;

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

				for (PsiClass psiClass : javaFile.getClasses()) {
					output.addResult(new AnalysisResult("- " + psiClass.getName()));

					for (PsiField psiField : psiClass.getFields()) {
						final Collection<PsiReference> usages = ReferencesSearch.search(psiField).findAll();
						output.addResult(new AnalysisResult("-- " + psiField.getName() + " (" + usages.size() + ")"));

						for (PsiReference usage : usages) {
							final String s = usage.toString() + ", " + usage.getClass().getSimpleName();
							output.addResult(new AnalysisResult("--- " + s));
							PsiReferenceExpression rx = (PsiReferenceExpression) usage;
							PsiElement parent = rx.getParent();
							while (!(parent instanceof PsiFile) && (parent != null)) {
//								output.addResult(new AnalysisResult("---- parent: " + parent.toString()));
								if (parent instanceof PsiMethod) {
									PsiMethod method = (PsiMethod) parent;
									output.addResult(new AnalysisResult("---- used in: " + method.getName()));
								}
								parent = parent.getParent();
							}
						}
					}


/*
					for (PsiMethod psiMethod : psiClass.getMethods()) {
						output.addResult(new AnalysisResult("- " + psiMethod.getName()));
						final PsiCodeBlock body = psiMethod.getBody();

						for (PsiStatement psiStatement : body.getStatements()) {
							if (psiClass instanceof PsiDeclarationStatement) {
								final PsiDeclarationStatement psiDeclarationStatement = (PsiDeclarationStatement) psiClass;
								final String s = psiDeclarationStatement.getFirstChild().getClass().getSimpleName();
								output.addResult(new AnalysisResult("-- " + s));

							}
						}
					}
*/

				}

			}
		}
	}
}
