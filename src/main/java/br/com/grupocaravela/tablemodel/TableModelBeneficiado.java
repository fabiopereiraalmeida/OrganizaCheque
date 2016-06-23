package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Beneficiado;

public class TableModelBeneficiado extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Beneficiado> listaBeneficiado;
    //Titulo das colunas
    private String[] colunas = {"Beneficiado", "Juros (%)", "Lucro"};
    
    //Construtor
    public TableModelBeneficiado(){
        this.listaBeneficiado = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addBeneficiado(Beneficiado c){
        this.listaBeneficiado.add(c);
        fireTableDataChanged();
    }
    
    public void removeBeneficiado(int rowIndex){
        this.listaBeneficiado.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Beneficiado getBeneficiado(int rowIndex){
        return this.listaBeneficiado.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaBeneficiado.size();
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
                return this.listaBeneficiado.get(rowIndex).getDestinatario().getNome();
            
            case 1:
                return this.listaBeneficiado.get(rowIndex).getJuros();
                
            case 2:
                return this.listaBeneficiado.get(rowIndex).getLucro();
                
            default:
                return this.listaBeneficiado.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
