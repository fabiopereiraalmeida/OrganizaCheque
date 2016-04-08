package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Destinatario;

public class TableModelDestinatario extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Destinatario> listaDestinatario;
    //Titulo das colunas
    private String[] colunas = {"Id", "Nome", "Telefone", "Cidade"};
    
    //Construtor
    public TableModelDestinatario(){
        this.listaDestinatario = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addDestinatario(Destinatario d){
        this.listaDestinatario.add(d);
        fireTableDataChanged();
    }
    
    public void removeDestinatario(int rowIndex){
        this.listaDestinatario.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Destinatario getDestinatario(int rowIndex){
        return this.listaDestinatario.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaDestinatario.size();
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
                return this.listaDestinatario.get(rowIndex).getId();
            
            case 1:
                return this.listaDestinatario.get(rowIndex).getNome();
                
            case 2:
                return this.listaDestinatario.get(rowIndex).getTelefone();
                
            case 3:
                return this.listaDestinatario.get(rowIndex).getCidade();
                
            default:
                return this.listaDestinatario.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
