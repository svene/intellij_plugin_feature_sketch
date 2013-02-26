package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
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
//				final String packageName = javaFile.getPackageName();

				for (PsiClass psiClass : javaFile.getClasses()) {
					final CohesionNode classNode = new CohesionNode(psiClass.getName(), false);
					output.addCohesionNode(classNode);

					for (PsiMember psiMember : psiClass.getFields()) {
						handlePsiMember(psiClass, classNode, psiMember, false);
					}
					for (PsiMember psiMember : psiClass.getMethods()) {
						PsiMethod psiMethod = (PsiMethod) psiMember;
						handlePsiMember(psiClass, classNode, psiMember, true);
					}



				}

			}
		}

		private void handlePsiMember(PsiClass inPsiClass, CohesionNode inClassNode, PsiMember psiMember, boolean isMethod) {
			final CohesionNode cohesionNode = new CohesionNode(getNodeLabel(psiMember, isMethod), isMethod);
			inClassNode.addChild(cohesionNode);

			final Collection<PsiReference> usages = ReferencesSearch.search(psiMember).findAll();

			for (PsiReference usage : usages) {
				final String s = usage.toString() + ", " + usage.getClass().getSimpleName();
				PsiElement parent;
				if (usage instanceof PsiReferenceExpression) {
					PsiReferenceExpression rx = (PsiReferenceExpression) usage;
					parent = rx.getParent();
				}
				else if (usage instanceof CompositePsiElement) {
					parent = ((CompositePsiElement)usage).getParent();
				}
				else {
					parent = null;
				}
				if (parent == null) {
					cohesionNode.addChild(new CohesionNode("type of parent not supported yet: " + usage.getClass().getSimpleName(), false));
				}
				else {
					PsiMethod psiMethod = findOwningPsiMethod(parent);
					if (psiMethod != null) {
						final PsiClass owningPsiClass = findOwningPsiClass(psiMethod);
						if (inPsiClass.getName().equals(owningPsiClass.getName())) {
							final CohesionNode methodNode = new CohesionNode(getNodeLabel(psiMethod, true), true);
							cohesionNode.addChild(methodNode);
						}
/*
						else {
							final CohesionNode methodNode = new CohesionNode("* " + getNodeLabel(psiMethod, true), true);
							cohesionNode.addChild(methodNode);
						}
*/
					}
				}
			}
		}
	}

	private PsiMethod findOwningPsiMethod(PsiElement node) {
		PsiElement parent = node.getParent();
		while (!(parent instanceof PsiFile) && (parent != null)) {
			if (parent instanceof PsiMethod) {
				return (PsiMethod) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}
	private PsiClass findOwningPsiClass(PsiElement node) {
		PsiElement parent = node.getParent();
		while (!(parent instanceof PsiFile) && (parent != null)) {
			if (parent instanceof PsiClass) {
				return (PsiClass) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	private String getNodeLabel(PsiMember psiMember, boolean isMethod) {
		String name = psiMember.getName();
/*
		if (isMethod) {
			PsiMethod psiMethod = (PsiMethod) psiMember;
			final PsiElement parent = psiMethod.getParent();
			if (parent instanceof PsiClass) {
				PsiClass psiClass = (PsiClass) parent;
				name = psiClass.getName() + "." + name;
			}
			else {
				name = "CLASS" + "." + name;
			}
		}
*/
		return name;
	}
}
