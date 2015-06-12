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
package de.jlo.talendcomp.flatfileimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author Jan Lolling
 */
public final class CSVFileDatasetProvider implements DatasetProvider {

	private transient BufferedReader bufferedReader = null;
	private boolean useEnclosure = false;
	private char enclosure = ' ';
	private long currentRowNum = 0;
	private String currentLine = null;
	private boolean skipEmptyLines = true;
	public static char[] BOMS = {0xFEFF, 0xEF, 0xBB, 0xBF};
	private boolean ignoreBOM = false;
		
	/**
	 * this setups the delimiter and enclosure to support 
	 * @param encloser
	 * @param fieldDelimiter
	 */
	private void setEncloser(String enclosure) {
		if (enclosure != null && enclosure.isEmpty() == false) {
			if (enclosure.length() > 1) {
				throw new IllegalArgumentException("Enclosure can only be one char!");
			}
			this.enclosure = enclosure.charAt(0);
			useEnclosure = true;
		} else {
			useEnclosure = false;
		}
	}

	@Override
	public FieldTokenizer createParser(ImportAttributes properties) {
		CSVFieldTokenizer parser = new CSVFieldTokenizer();
		parser.setDelimiter(properties.getDelimiter());
		parser.setEnclosure(properties.getEnclosure());
		parser.setIgnoreNotNullConstraints(properties.isIgnoreNotNullConstraint());
		parser.setAllowEnclosureInText(properties.isAllowEnclosureInText());
		return parser;
	}

	@Override
	public void setupDatasetProvider(File file, ImportAttributes properties) throws Exception {
		if (properties.isIgnoreLineBreakInEnclosedValues()) {
			setEncloser(properties.getEnclosure());
		}
		skipEmptyLines = properties.isSkipEmptyLines();
		ignoreBOM = properties.isIgnoreBOM();
		bufferedReader = new BufferedReader(
				new InputStreamReader(
				new FileInputStream(
						file), 
						properties.getCharsetName()));
		currentRowNum = 0;
		currentLine = null;
	}

	@Override
	public void closeDatasetProvider() {
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bufferedReader = null;
		}
	}
	
	@Override
	public String getNextDataset() throws Exception {
		checkReader();
		currentLine = readLine();
		// BOM detection only in the first line
		if (ignoreBOM && currentRowNum == 0 && currentLine != null && currentLine.length() > 0) {
			boolean hasBOM = false;
			char firstLineChar = currentLine.charAt(0);
			for (char bom : BOMS) {
				if (firstLineChar == bom) {
					hasBOM = true;
					break;
				}
			}
			if (hasBOM) {
				currentLine = currentLine.substring(1); // skip BOM
			}
		}
		currentRowNum++;
		if (currentLine != null && skipEmptyLines && currentLine.isEmpty()) {
			while (currentLine != null && currentLine.isEmpty()) {
				currentLine = readLine();
				currentRowNum++;
			}
		}
		return currentLine;
	}
	
	private void checkReader() throws Exception {
		if (bufferedReader == null) {
			throw new Exception("Reader is not opened or already closed");
		}
	}
	
	private String readLine() throws Exception {
		if (useEnclosure) {
			return readLineWithEnclosure();
		} else {
			return bufferedReader.readLine();
		}
	}
	
	private String readLineWithEnclosure() throws IOException {
		StringBuilder line = new StringBuilder();
		boolean inEclosuredField = false;
		char last_c = (char) 0;
		while (true) {
			int r = bufferedReader.read();
			if (r == -1) {
				if (line.length() > 0) {
					return line.toString();
				} else {
					return null;
				}
			}
			char c = (char) r;
			if (c == enclosure) {
				if (inEclosuredField || last_c == enclosure) {
					// end of enclosed field found
					inEclosuredField = false;
				} else {
					// start of enclosed field found
					inEclosuredField = true;
				}
				line.append(c); // add the enclosure to the line
			} else if (c == '\n') {
				if (inEclosuredField) {
					// we have to add to line
					line.append(c);
				} else {
					break;
				}
			} else if (c == '\r') {
				// ignore that
				continue;
			} else {
				// all content chars
				line.append(c);
				if (last_c == enclosure) {
					inEclosuredField = true;
				}
			}
			last_c = c;
		}
		return line.toString();
	}

	@Override
	public long getCurrentRowNum() {
		return currentRowNum;
	}

	@Override
	public boolean isSkipEmptyLines() {
		return skipEmptyLines;
	}

	@Override
	public void skipEmptyLines(boolean skipEmptyLines) {
		this.skipEmptyLines = skipEmptyLines;
	}

	public boolean isIgnoreBOM() {
		return ignoreBOM;
	}

	public void setIgnoreBOM(boolean ignoreBOM) {
		this.ignoreBOM = ignoreBOM;
	}

}