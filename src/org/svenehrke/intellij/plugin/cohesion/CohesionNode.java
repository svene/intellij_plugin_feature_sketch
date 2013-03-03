package org.svenehrke.intellij.plugin.cohesion;

import java.util.HashMap;
import java.util.Map;

public class CohesionNode {

	private final String name;
	private final FKNodeType nodeType;
	private final Map<String, CohesionNode> children = new HashMap<String, CohesionNode>();

	public CohesionNode(String inName, FKNodeType inNodeType) {
		name = inName;
		nodeType = inNodeType;
	}

	public void addChild(CohesionNode child) {
		children.put(child.getName(), child);
	}

	public String getName() {
		return name;
	}

	public boolean isMethod() {
		return nodeType == FKNodeType.METHOD;
	}
	public boolean isClass() {
		return nodeType == FKNodeType.CLASS;
	}

	public boolean isField() {
		return nodeType == FKNodeType.FIELD;
	}

	public Map<String, CohesionNode> getChildren() {
		return children;
	}

	public CohesionNode childByName(String inName) {
		return children.get(inName);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CohesionNode that = (CohesionNode) o;

		if (!name.equals(that.name)) return false;
		if (nodeType != that.nodeType) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + nodeType.hashCode();
		return result;
	}
}
