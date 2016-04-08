package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Usuario;

public class TableModelUsuario extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Usuario> listaUsuario;
    //Titulo das colunas
    private String[] colunas = {"Id", "Nome", "Usuario", "Administrador"};
    
    //Construtor
    public TableModelUsuario(){
        this.listaUsuario = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addUsuario(Usuario d){
        this.listaUsuario.add(d);
        fireTableDataChanged();
    }
    
    public void removeUsuario(int rowIndex){
        this.listaUsuario.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Usuario getUsuario(int rowIndex){
        return this.listaUsuario.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaUsuario.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return this.listaUsuario.get(rowIndex).getId();
            
            case 1:
                return this.listaUsuario.get(rowIndex).getNome();
                
            case 2:
                return this.listaUsuario.get(rowIndex).getUsuario();
                
            case 3:
            	
            	if (this.listaUsuario.get(rowIndex).getAdiministrador()) {
            		return "sim";
				}else{
					return "Não";
				}
                            
            default:
                return this.listaUsuario.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
