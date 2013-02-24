package org.svenehrke.intellij.plugin.cohesion;

import java.util.HashMap;
import java.util.Map;

public class CohesionNode {

	private final String name;
	private final Map<String, CohesionNode> children = new HashMap<String, CohesionNode>();

	public CohesionNode(String inName) {
		name = inName;
	}


	public void addChild(CohesionNode child) {
		children.put(child.getName(), child);
	}

	public String getName() {
		return name;
	}

	public Map<String, CohesionNode> getChildren() {
		return children;
	}

	public CohesionNode childByName(String inName) {
		return children.get(inName);
	}
}
