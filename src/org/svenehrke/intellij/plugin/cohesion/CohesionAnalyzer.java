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
		output.printCohesionGraph();
	}

	private class AnalyzerRunnable implements Runnable {

		public void run() {
			PsiFile psiFile = psiManager.findFile(input.getCurrentFile());
			if (psiFile != null && JavaLanguage.INSTANCE.equals(psiFile.getLanguage())) {
				PsiJavaFile javaFile = (PsiJavaFile) psiFile;
				final String packageName = javaFile.getPackageName();

				for (PsiClass psiClass : javaFile.getClasses()) {
					final CohesionNode classNode = new CohesionNode(psiClass.getName());
					output.addCohesionNode(classNode);

					for (PsiField psiField : psiClass.getFields()) {
						final CohesionNode fieldNode = new CohesionNode(psiField.getName());
						classNode.addChild(fieldNode);

						final Collection<PsiReference> usages = ReferencesSearch.search(psiField).findAll();

						for (PsiReference usage : usages) {
							final String s = usage.toString() + ", " + usage.getClass().getSimpleName();
							PsiReferenceExpression rx = (PsiReferenceExpression) usage;
							PsiElement parent = rx.getParent();
							while (!(parent instanceof PsiFile) && (parent != null)) {
								if (parent instanceof PsiMethod) {
									PsiMethod method = (PsiMethod) parent;
									final CohesionNode methodNode = new CohesionNode(method.getName());
									fieldNode.addChild(methodNode);
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
