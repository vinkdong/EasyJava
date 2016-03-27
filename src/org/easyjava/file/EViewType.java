package org.easyjava.file;

import org.w3c.dom.Node;

public class EViewType {
	
	public Dict getDict() {
		return dict;
	}
	public void setDict(Dict dict) {
		this.dict = dict;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	private Dict dict = new Dict();
	private Node node = null;

}
