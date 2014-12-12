package gui;

import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JFloatField extends JTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Toolkit toolkit;
	private DecimalFormat decimalFormatter;
	private char decimalSeparator;

	private void init() {
		toolkit = Toolkit.getDefaultToolkit();
		setDecimalFormat(new DecimalFormat("#0.00"));
	}

	public JFloatField() {
		super();
		init();
	}

	public JFloatField(int columns) {
		super(columns);
		init();
	}

	public void setDecimalFormat(DecimalFormat formatter) {
		decimalFormatter = formatter;
		decimalSeparator = decimalFormatter.getDecimalFormatSymbols()
				.getDecimalSeparator();
	}

	public float getValue() {
		if (getText().equals("")) {
			return 0;
		}
		try {
			return decimalFormatter.parse(getText()).floatValue();
		} catch (ParseException e) {
			// ne devrait jamais arriver
			toolkit.beep();
			return 0;
		}
	}

	public void setValue(float value) {
		setText(decimalFormatter.format(value));
	}

	public void format() {
		setValue(getValue());
	}

	protected Document createDefaultModel() {
		return new JFloatFieldDocument();
	}

	protected class JFloatFieldDocument extends PlainDocument {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected boolean comma = false;

		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

			char[] source = str.toCharArray();
			char[] result = new char[source.length];
			int j = 0;
			char c;
			for (int i = 0; i < result.length; i++) {
				c = source[i];
				if (c == '.') {
					c = decimalSeparator;
				}
				if (Character.isDigit(c))
					result[j++] = c;
				else if (c == decimalSeparator) {
					if (!comma) {
						comma = true;
						result[j++] = c;
					} else {
						toolkit.beep();
					}
				} else {
					toolkit.beep();
				}
			}
			super.insertString(offs, new String(result, 0, j), a);
		}

		public void remove(int offs, int len) throws BadLocationException {
			String removedText = getText(offs, len);
			if (removedText.indexOf(decimalSeparator) != -1) {
				comma = false;
			}
			super.remove(offs, len);
		}
	}
}