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

	private void init() {
		toolkit = Toolkit.getDefaultToolkit();
	}

	public JFloatField() {
		super();
		init();
	}

	public JFloatField(int columns) {
		super(columns);
		init();
	}

	public float getValue() {
		if (getText().equals("")) {
			return 0;
		}
		return Float.valueOf(getText()).floatValue();
	}

	public void setValue(float value) {
		setText(value +"");
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
				if (Character.isDigit(c))
					result[j++] = c;
				else {
					toolkit.beep();
				}
			}
			super.insertString(offs, new String(result, 0, j), a);
		}

		public void remove(int offs, int len) throws BadLocationException {
			String removedText = getText(offs, len);
			super.remove(offs, len);
		}
	}
}