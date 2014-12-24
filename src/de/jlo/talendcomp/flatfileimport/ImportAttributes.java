package de.jlo.talendcomp.flatfileimport;

import java.util.Properties;

public class ImportAttributes {
	
	private boolean hasHeaderLine;
	private int countSkipRows;
	private String delimiter;
	private String enclosure;
	private boolean skipEmptyLines = false;
	private boolean ignoreLineBreakInEnclosedValues = false;
	private boolean ignoreNotNullConstraint = false;
	private boolean allowEnclosureInText = false;
	private int columnCount;
	private String charsetName;
	private boolean ignoreBOM = false;
	public static final String DEFAULT_CHARSET = System.getProperty("file.encoding");
	
	public void storeInto(Properties properties) {
		properties.put("SKIP_ROWS", String.valueOf(countSkipRows));
		properties.put("DELIMITER", getTokenFromDelimiter(delimiter));
		properties.put("SKIP_EMPTY_LINES", skipEmptyLines);
        if (enclosure != null) {
        	properties.put("ENCLOSURE", enclosure);
        	properties.put("IGNORE_ENCLOSED_LINE_BREAK", ignoreLineBreakInEnclosedValues);
        } else {
        	properties.remove("ENCLOSURE");
        }
        properties.put("COLUMN_COUNT", String.valueOf(columnCount));
        properties.put("IGNORE_NOT_NULL_CONSTRAINTS", ignoreNotNullConstraint);
        properties.put("ALLOW_ENCLUSURE_IN_TEXT", allowEnclosureInText);
        properties.put("IGNORE_BOM", ignoreBOM);
		if (charsetName != null) {
			properties.put("CHARSET", charsetName);
		} else {
			properties.put("CHARSET", DEFAULT_CHARSET);
		}
	}

	private static String getDelimiterFromToken(String token) {
        if (token == null) {
            return null;
        }
        if (token.equals("{TAB}")) {
            return "\t";
        } else if (token.equals("{SPACE}")) {
            return " ";
        } else {
            return token;
        }
    }
	
	private static String getTokenFromDelimiter(String delimiter) {
		if (delimiter == null) {
			return null;
		} else if ("\t".equals(delimiter)) {
			return "{TAB}";
		} else if (" ".equals(delimiter)) {
			return "{SPACE}";
		} else {
			return delimiter;
		}
	}
	
	public String getDelimiterToken() {
		return getTokenFromDelimiter(delimiter);
	}
	
	public void clear() {
		Properties p = new Properties();
		try {
			setupFrom(p);
		} catch(Exception e) {
			System.err.println(e);
		}
	}

	public void setupFrom(Properties properties) throws Exception {
        delimiter = getDelimiterFromToken(properties.getProperty("DELIMITER"));
        enclosure = properties.getProperty("ENCLOSURE");
        ignoreLineBreakInEnclosedValues = "true".equals(properties.getProperty("IGNORE_ENCLOSED_LINE_BREAK", "false"));
        hasHeaderLine = properties.getProperty("SKIP_FIRST_ROW", "true").trim().equals("true");
        countSkipRows = Integer.parseInt(properties.getProperty("SKIP_ROWS", "0"));
        skipEmptyLines = "true".equals(properties.put("SKIP_EMPTY_LINES", "false"));
        ignoreNotNullConstraint = "true".equals(properties.getProperty("IGNORE_NOT_NULL_CONSTRAINTS", "false"));
        allowEnclosureInText = "true".equals(properties.getProperty("ALLOW_ENCLUSURE_IN_TEXT", "false"));
        ignoreBOM = "true".equals(properties.getProperty("IGNORE_BOM", "false"));
		setCharsetName(properties.getProperty("CHARSET", DEFAULT_CHARSET));
	}
	
	public String getCharsetName() {
		return charsetName != null ? charsetName : DEFAULT_CHARSET;
	}
	public void setCharsetName(String charsetName) {
		if (charsetName != null && charsetName.trim().length() > 0) {
			this.charsetName = charsetName.trim();
		} else {
			this.charsetName = null;
		}
	}

	public boolean hasHeaderLine() {
		return hasHeaderLine;
	}

	public void setHeaderLine(boolean skipFirstRow) {
		this.hasHeaderLine = skipFirstRow;
	}
	
	public int getCountSkipRows() {
		return countSkipRows;
	}
	
	public void setCountSkipRows(int countSkipRows) {
		this.countSkipRows = countSkipRows;
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public String getEnclosure() {
		return enclosure;
	}
	
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}
	
    public int getColumnCount() {
		return columnCount;
	}
	
    public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public boolean isIgnoreLineBreakInEnclosedValues() {
		return ignoreLineBreakInEnclosedValues;
	}

	public void setIgnoreLineBreakInEnclosedValues(
			boolean ignoreLineBreakInEnclosedValues) {
		this.ignoreLineBreakInEnclosedValues = ignoreLineBreakInEnclosedValues;
	}

	public boolean isSkipEmptyLines() {
		return skipEmptyLines;
	}

	public void setSkipEmptyLines(boolean skipEmptyLines) {
		this.skipEmptyLines = skipEmptyLines;
	}

	public boolean isIgnoreNotNullConstraint() {
		return ignoreNotNullConstraint;
	}

	public void setIgnoreNotNullConstraint(boolean ignoreNotNullConstraint) {
		this.ignoreNotNullConstraint = ignoreNotNullConstraint;
	}
	
	public boolean isAllowEnclosureInText() {
		return allowEnclosureInText;
	}

	public void setAllowEnclosureInText(boolean allowEnclosureInText) {
		this.allowEnclosureInText = allowEnclosureInText;
	}

	public boolean isIgnoreBOM() {
		return ignoreBOM;
	}

	public void setIgnoreBOM(boolean ignoreBOM) {
		this.ignoreBOM = ignoreBOM;
	}
	
}
