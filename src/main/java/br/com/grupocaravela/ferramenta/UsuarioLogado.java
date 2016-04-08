package br.com.grupocaravela.ferramenta;

import br.com.grupocaravela.objeto.Usuario;

public class UsuarioLogado {

	public static Usuario usuario;

	public static Usuario getUsuario() {
		return usuario;
	}

	public static void setUsuario(Usuario usuario) {
		UsuarioLogado.usuario = usuario;
	}
	
}

