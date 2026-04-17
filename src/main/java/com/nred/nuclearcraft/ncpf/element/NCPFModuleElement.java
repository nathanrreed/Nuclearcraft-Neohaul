package com.nred.nuclearcraft.ncpf.element;

public class NCPFModuleElement extends NCPFElement {
	public String name;
	
	public NCPFModuleElement() {
		super("module");
	}
	
	public NCPFModuleElement(String name) {
		this();
		this.name = name;
	}
}