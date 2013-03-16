package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FSOptionsDialog extends DialogWrapper {

	private static final String SHOW_EXTERNAL_CALLS_KEY = "featureSketch.showExternalCalls";
	private static final String MERGE_EXTERNAL_CALLS_KEY = "featureSketch.mergeExternalCalls";
	private final JCheckBox showExternalDependenciesCheckBox;
	private final JCheckBox mergeExternalDependenciesCheckBox;
	private final Project project;

	FSOptionsDialog(Project inProject, AnalyzeTaskOptions inTaskOptions) {
		super(inProject, false);
		project = inProject;

		mergeExternalDependenciesCheckBox = new JCheckBox(Strings.MERGE_EXTERNAL_CALLS);
		showExternalDependenciesCheckBox = new JCheckBox(Strings.SHOW_EXTERNAL_CALLS);

		setTitle(Strings.FEATURE_SKETCH_OPTIONS_DIALOG_TITLE);
		init();
	}

	@Override
	protected JComponent createCenterPanel() {
		JPanel panel = new JPanel(new MigLayout("wrap 3"));
		panel.add(showExternalDependenciesCheckBox);
		panel.add(mergeExternalDependenciesCheckBox);



		return panel;
	}

	@Override
	protected final void init() {
		super.init();

		showExternalDependenciesCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mergeExternalDependenciesCheckBox.setEnabled(showExternalDependenciesCheckBox.isSelected());
			}
		});


		PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
		boolean showExternalCalls = propertiesComponent.getBoolean(SHOW_EXTERNAL_CALLS_KEY, false);
		showExternalDependenciesCheckBox.setSelected(showExternalCalls);

		boolean mergeExternalCalls = propertiesComponent.getBoolean(MERGE_EXTERNAL_CALLS_KEY, false);
		mergeExternalDependenciesCheckBox.setSelected(mergeExternalCalls);

		mergeExternalDependenciesCheckBox.setEnabled(showExternalDependenciesCheckBox.isSelected());
	}

	@Override
	protected Action[] createActions() {
		return new Action[]{getOKAction(), getCancelAction()};
	}

	@Override
	protected void doOKAction() {
		PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
		propertiesComponent.setValue(SHOW_EXTERNAL_CALLS_KEY, Boolean.toString(showExternalDependenciesCheckBox.isSelected()));
		propertiesComponent.setValue(MERGE_EXTERNAL_CALLS_KEY, Boolean.toString(mergeExternalDependenciesCheckBox.isSelected()));
		super.doOKAction();
	}

	AnalyzeTaskOptions getTaskOptions() {
		final AnalyzeTaskOptions result = new AnalyzeTaskOptions();
		result.showExternalDependencies = showExternalDependenciesCheckBox.isSelected();
		result.mergeExternalDependencies = mergeExternalDependenciesCheckBox.isSelected();
		return result;
	}
}
