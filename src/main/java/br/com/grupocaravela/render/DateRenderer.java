package br.com.grupocaravela.render;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

public class DateRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	private Date dateValue;
	//private SimpleDateFormat sdfNewValue = new SimpleDateFormat("EE MMM dd hh:mm:ss z yyyy");
	private SimpleDateFormat sdfNewValue = new SimpleDateFormat("dd/MM/yyyy");
	private String valueToString = "";

	@Override
	public void setValue(Object value) {
		if ((value != null)) {
			String stringFormat = value.toString();
			try {
				dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(stringFormat);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			valueToString = sdfNewValue.format(dateValue);
			value = valueToString;
		}
		super.setValue(value);
	}
}
