package org.svenehrke.intellij.plugin.cohesion;

import com.intellij.openapi.vfs.VirtualFile;

public class AnalysisInput {

	private final VirtualFile currentFile;

	public AnalysisInput(VirtualFile inCurrentFile) {
		currentFile = inCurrentFile;
	}


	public VirtualFile getCurrentFile() {
		return currentFile;
	}
}
