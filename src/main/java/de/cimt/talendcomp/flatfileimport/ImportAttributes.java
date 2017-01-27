/**
 * Copyright 2015 Jan Lolling jan.lolling@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cimt.talendcomp.flatfileimport;

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
	
	public void setupFrom(Properties properties) throws Exception {
		if (properties.containsKey("DELIMITER")) {
	        delimiter = getDelimiterFromToken(properties.getProperty("DELIMITER"));
		}
		if (properties.containsKey("ENCLOSURE")) {
	        enclosure = properties.getProperty("ENCLOSURE");
		}
		if (properties.containsKey("IGNORE_ENCLOSED_LINE_BREAK")) {
			ignoreLineBreakInEnclosedValues = "true".equals(properties.getProperty("IGNORE_ENCLOSED_LINE_BREAK", "false"));
		}
		if (properties.containsKey("SKIP_FIRST_ROW")) {
	        hasHeaderLine = properties.getProperty("SKIP_FIRST_ROW", "true").trim().equals("true");
		}
		if (properties.containsKey("SKIP_ROWS")) {
	        countSkipRows = Integer.parseInt(properties.getProperty("SKIP_ROWS", "0"));
		}
		if (properties.containsKey("SKIP_EMPTY_LINES")) {
	        skipEmptyLines = "true".equals(properties.put("SKIP_EMPTY_LINES", "false"));
		}
		if (properties.containsKey("IGNORE_NOT_NULL_CONSTRAINTS")) {
	        ignoreNotNullConstraint = "true".equals(properties.getProperty("IGNORE_NOT_NULL_CONSTRAINTS", "false"));
		}
		if (properties.containsKey("ALLOW_ENCLUSURE_IN_TEXT")) {
	        allowEnclosureInText = "true".equals(properties.getProperty("ALLOW_ENCLUSURE_IN_TEXT", "false"));
		}
		if (properties.containsKey("IGNORE_BOM")) {
	        ignoreBOM = "true".equals(properties.getProperty("IGNORE_BOM", "false"));
		}
		if (properties.containsKey("CHARSET")) {
			setCharsetName(properties.getProperty("CHARSET", DEFAULT_CHARSET));
		}
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
