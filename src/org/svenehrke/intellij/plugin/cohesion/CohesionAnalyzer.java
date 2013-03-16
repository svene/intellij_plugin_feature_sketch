package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.lang.java.JavaLanguage;
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
		PsiFile psiFile = psiManager.findFile(input.getCurrentFile());
		if (psiFile != null && JavaLanguage.INSTANCE.equals(psiFile.getLanguage())) {
			PsiJavaFile javaFile = (PsiJavaFile) psiFile;
			PsiClass[] classes = javaFile.getClasses();
			for (int i = 0; i < classes.length; i++) {
				if (i > 0) {
					continue; // current version of plugin only handles first class of file
				}

				PsiClass psiClass = classes[i];
				final CohesionNode classNode = new CohesionNode(psiClass.getName(), FKNodeType.CLASS);
				output.setMainNode(classNode);

				handleFields(psiClass, output);
				handleMethods(psiClass, output);


			}

		}
	}

		private void handleFields(PsiClass psiClass, IAnalysisOutput inOutput) {
			for (PsiMember psiMember : psiClass.getFields()) {
				handlePsiMember(psiClass, inOutput, psiMember, FKNodeType.FIELD);
			}
		}

		private void handleMethods(PsiClass psiClass, IAnalysisOutput inOutput) {
			for (PsiMember psiMember : psiClass.getMethods()) {
				PsiMethod psiMethod = (PsiMethod) psiMember;

				// Skip constructors since they usually touch most features of the class by nature
				// and thus would only bring noise to the output:
				if (psiMethod.isConstructor()) {
					continue;
				}
				handlePsiMember(psiClass, inOutput, psiMember, FKNodeType.METHOD);
			}
		}

		private void handlePsiMember(PsiClass inPsiClass, IAnalysisOutput inOutput, PsiMember psiMember, FKNodeType inNodeType) {
			final CohesionNode cohesionNode = new CohesionNode(getNodeLabel(psiMember), inNodeType);
			inOutput.getMainNode().addChild(cohesionNode);

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
					cohesionNode.addChild(new CohesionNode("type of parent not supported yet: " + usage.getClass().getSimpleName(), inNodeType));
				}
				else {
					PsiMethod psiMethod = findOwningPsiMethod(parent);
					if (psiMethod != null) {
						final PsiClass owningPsiClass = findOwningPsiClass(psiMethod);
						if (inPsiClass.getName().equals(owningPsiClass.getName())) {
							final CohesionNode methodNode = new CohesionNode(getNodeLabel(psiMethod), FKNodeType.METHOD);
							cohesionNode.addChild(methodNode);
						}
						else {
							final CohesionNode externalClassNode = new CohesionNode(getNodeLabel(owningPsiClass), FKNodeType.CLASS);
							cohesionNode.addChild(externalClassNode);
							output.addExternalClassNode(externalClassNode);
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

	private String getNodeLabel(PsiMember psiMember) {
		return psiMember.getName();
	}
}
