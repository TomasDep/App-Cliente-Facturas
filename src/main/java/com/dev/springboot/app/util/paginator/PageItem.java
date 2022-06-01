package com.dev.springboot.app.util.paginator;

public class PageItem {
	private int numero;
	private boolean actual;
	
	public PageItem(int numero, boolean actual) {
		this.numero = numero;
		this.actual = actual;
	}

	public int getNumero() {
		return this.numero;
	}

	public boolean isActual() {
		return this.actual;
	}
}
