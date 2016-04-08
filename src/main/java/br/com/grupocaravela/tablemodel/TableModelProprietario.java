package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Proprietario;

public class TableModelProprietario extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Proprietario> listaProprietario;
    //Titulo das colunas
    private String[] colunas = {"Id", "Nome", "Telefone", "Cidade", "CPF/CNPJ"};
    
    //Construtor
    public TableModelProprietario(){
        this.listaProprietario = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addProprietario(Proprietario d){
        this.listaProprietario.add(d);
        fireTableDataChanged();
    }
    
    public void removeProprietario(int rowIndex){
        this.listaProprietario.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Proprietario getProprietario(int rowIndex){
        return this.listaProprietario.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaProprietario.size();
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
                return this.listaProprietario.get(rowIndex).getId();
            
            case 1:
                return this.listaProprietario.get(rowIndex).getNome();
                
            case 2:
                return this.listaProprietario.get(rowIndex).getTelefone();
                
            case 3:
                return this.listaProprietario.get(rowIndex).getCidade();
                
            case 4:
                return this.listaProprietario.get(rowIndex).getCpfCnpj();
                
            default:
                return this.listaProprietario.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
