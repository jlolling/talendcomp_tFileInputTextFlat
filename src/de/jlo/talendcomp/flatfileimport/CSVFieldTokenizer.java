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


/**
 * parser for a String-line
 */
public final class CSVFieldTokenizer extends AbstractFieldTokenizer {

	private String rowData;
    private char[] data;
	private int lastPosDel = 0;
	private int lastPosAbs = 0;
	private int lastDelimiterIndex = 0;
	private String fieldDelimiter;
    private char[] delimiterChars;
	private String enclosure;
    private char[] enclosureChars;
    private boolean allowEnclosureInText = false;
    
	public boolean isAllowEnclosureInText() {
		return allowEnclosureInText;
	}

	public void setAllowEnclosureInText(boolean allowEnclosureInText) {
		this.allowEnclosureInText = allowEnclosureInText;
	}

	public CSVFieldTokenizer() {
        setRowData(null);
        setDelimiter(null);
        setEnclosure(null);
	}

    /**
     * sets the data of a row
     * @param rowData_loc
     */
	public void setRowData(String rowData) {
        this.rowData = rowData;
        this.data = getChars(rowData);
        lastPosDel = 0;
        lastPosAbs = 0;
        lastDelimiterIndex = 0;
	}

    private static final char[] getChars(String s) {
        if (s == null) {
            return new char[0];
        } else {
            return s.toCharArray();
        }
    }

	public String extractDataAtDelimiter(int fieldNum, int length) throws ParserException {
        String value = null;
        if (fieldNum < lastDelimiterIndex) {
        	throw new ParserException("Current field index " + fieldNum + " is lower then last field index:" + lastDelimiterIndex);
        }
        int countDelimiters = lastDelimiterIndex;
        boolean inField = false;
        boolean atEnclosureStart = false;
        boolean atEnclosureStop = false;
        boolean atDelimiter = false;
        boolean useEnclosure = enclosureChars.length > 0;
        boolean fieldStartsWithEnclosure = false;
        boolean continueField = false;
        int currPos = lastPosDel;
        StringBuilder sb = new StringBuilder();
        while (currPos < data.length && countDelimiters <= fieldNum) {
            if (atEnclosureStart) {
                atEnclosureStart = false;
                fieldStartsWithEnclosure = true;
                currPos = currPos + enclosureChars.length;
                atEnclosureStop = startsWith(data, enclosureChars, currPos);
                if (atEnclosureStop == false) {
                    // don't check delimiter here because these chars are part of value
                    inField = true;
                }
            } else if (atEnclosureStop) {
                atEnclosureStop = false;
                currPos = currPos + enclosureChars.length;
                atDelimiter = startsWith(data, delimiterChars, currPos);
                if (atDelimiter == false && currPos < data.length) {
                	if (allowEnclosureInText) {
                		inField = true;
                		continueField = true;
                		sb.append(enclosureChars);
                	} else {
                        throw new ParserException("Delimiter after enclosure stop missing at position:" + currPos + " in field number:" + fieldNum);
                	}
                }
            } else if (atDelimiter) {
                countDelimiters++;
                fieldStartsWithEnclosure = false;
                currPos = currPos + delimiterChars.length;
                atDelimiter = startsWith(data, delimiterChars, currPos);
                if (atDelimiter == false) {
                    if (useEnclosure && currPos < data.length) {
                        atEnclosureStart = startsWith(data, enclosureChars, currPos);
                        if (atEnclosureStart == false) {
                            inField = true;
                        }
                    } else {
                        inField = true;
                    }
                }
            } else if (inField) {
                if (continueField == false && countDelimiters == fieldNum) {
                    sb.setLength(0);
                }
                continueField = false;
                while (currPos < data.length) {
                    if (fieldStartsWithEnclosure) {
                        atEnclosureStop = startsWith(data, enclosureChars, currPos);
                        if (atEnclosureStop) {
                            break;
                        }
                    } else {
                        atDelimiter = startsWith(data, delimiterChars, currPos);
                        if (atDelimiter || atEnclosureStart) {
                            break;
                        }
                    }
                    if (countDelimiters == fieldNum) {
                        sb.append(data[currPos]);
                    }
                    currPos++;
                }
                inField = false;
                if (countDelimiters == fieldNum) {
                    value = sb.toString();
                }
            } else {
                if (useEnclosure) {
                    atEnclosureStart = startsWith(data, enclosureChars, currPos);
                }
                atDelimiter = startsWith(data, delimiterChars, currPos);
                if (atEnclosureStart == false && atDelimiter == false) {
                    inField = true;
                }
            }
        }
        lastPosDel = currPos;
        lastDelimiterIndex = fieldNum + 1;
        if (length > 0 && value != null && value.length() > length) {
            value = value.substring(0, length);
        }
        return value;
    }
    
    private static final boolean startsWith(char[] data, char[] search, int startPos) {
        if (search.length == 0 || data.length == 0) {
            return false;
        }
        if (startPos < 0 || startPos > (data.length - search.length)) {
            return false;
        }
        int searchPos = 0;
        int count = search.length;
        int dataPos = startPos;
        while (--count >= 0) {
            if (data[dataPos++] != search[searchPos++]) {
                return false;
            }
        }
        return true;
    }
    
    public int countDelimitedFields() throws ParserException {
        int countFields = data.length > 0 ? 1 : 0;
        boolean inField = false;
        boolean atEnclosureStart = false;
        boolean atEnclosureStop = false;
        boolean atDelimiter = false;
        boolean useEnclosure = enclosureChars.length > 0;
        boolean fieldStartsWithEnclosure = false;
        int currPos = 0;
        while (currPos < data.length) {
            if (atEnclosureStart) {
                atEnclosureStart = false;
                fieldStartsWithEnclosure = true;
                currPos = currPos + enclosureChars.length;
                atEnclosureStop = startsWith(data, enclosureChars, currPos);
                if (atEnclosureStop == false) {
                    // don't check delimiter here because these chars are part of the value
                    inField = true;
                }
            } else if (atEnclosureStop) {
                atEnclosureStop = false;
                currPos = currPos + enclosureChars.length;
                atDelimiter = startsWith(data, delimiterChars, currPos);
                if (atDelimiter == false && currPos < data.length) {
                	if (allowEnclosureInText) {
                		inField = true;
                	} else {
                        throw new ParserException("Delimiter after enclosure stop missing at position:" + currPos);
                	}
                }
            } else if (atDelimiter) {
                countFields++;
                fieldStartsWithEnclosure = false;
                currPos = currPos + delimiterChars.length;
                atDelimiter = startsWith(data, delimiterChars, currPos);
                if (atDelimiter == false) {
                    if (useEnclosure && currPos < data.length) {
                        atEnclosureStart = startsWith(data, enclosureChars, currPos);
                        if (atEnclosureStart == false) {
                            inField = true;
                        }
                    } else {
                        inField = true;
                    }
                }
            } else if (inField) {
                while (currPos < data.length) {
                    currPos++;
                    if (fieldStartsWithEnclosure) {
                        atEnclosureStop = startsWith(data, enclosureChars, currPos);
                        if (atEnclosureStop) {
                            break;
                        }
                    } else {
                        atDelimiter = startsWith(data, delimiterChars, currPos);
                        if (atEnclosureStart || atDelimiter) {
                            break;
                        }
                    }
                }
                inField = false;
            } else {
                if (useEnclosure) {
                    atEnclosureStart = startsWith(data, enclosureChars, currPos);
                }
                atDelimiter = startsWith(data, delimiterChars, currPos);
                if (atEnclosureStart == false && atDelimiter == false) {
                    inField = true;
                }
            }
        }
        return countFields;
    }
    
	private String extractDataAtAbsPos(int position, int length) {
		lastPosAbs = position + length;
		if (lastPosAbs > data.length) {
			lastPosAbs = data.length;
		}
		return rowData.substring(position, lastPosAbs);
	}

	private String extractDataAtLastPos(int length) {
		int beginIndex;
		if (length == 0) {
			return null;
		} else if (length < 0) {
			beginIndex = lastPosAbs + length;
			if (beginIndex < 0) {
				beginIndex = 0;
			}
			return rowData.substring(beginIndex, lastPosAbs);
		} else {
			beginIndex = lastPosAbs;
			lastPosAbs = beginIndex + length;
			if (lastPosAbs > data.length) {
				lastPosAbs = data.length;
			}
			return rowData.substring(beginIndex, lastPosAbs);
		}
	}

	/**
     * use the given rowData (see Constructor)
     * @param field which contains the extraction description
     * @return String data of appropriated field
     */
	private String extractData(FieldDescription field) throws ParserException {
		String value = null;
		try {
	    	if (field.getPositionType() == FieldDescription.DELIMITER_POSITION) {
				value = extractDataAtDelimiter(field.getDelimPos(), field.getLength());
	    	} else if (field.getPositionType() == FieldDescription.ABSOLUTE_POSITION) {
				value = extractDataAtAbsPos(field.getAbsPos(), field.getLength());
			} else if (field.getPositionType() == FieldDescription.RELATIVE_POSITION) {
				value = extractDataAtLastPos(field.getLength());
			} else if (field.getPositionType() == FieldDescription.DELIMITER_POSITION_WITH_LENGTH) {
				value = extractDataAtDelimiter(field.getDelimPos(), field.getLength());
			}
	        if (value != null && field.isTrimRequired()) {
	            value = value.trim();
	        }
			if (value != null && value.isEmpty() == false) {
				value = filter(field.getFilterPattern(), value);
			}
			if (value == null || value.isEmpty()) {
				value = field.getDefaultValue();
			}
	        if (field.getBasicTypeId() == BasicDataType.NUMBER.getId()) {
	            if (value != null && value.endsWith("-")) {
	                value = "-" + value.replace('-', ' ').trim();
	            }
	        }
		} catch (ParserException e) {
			throw new ParserException("Extract field " + field.getName() + " failed:" + e.getMessage(), e);
		}
		return value;
	}
    
    /**
     * parse raw data (one row) and uses the given list of field descriptions
     * @param rawdata to be analyzed
     */
	public boolean parseRawData(String rawdata) throws ParserException {
		setRowData(rawdata);
		clearListData();
		if (rowData != null) {
			for (FieldDescription fd : descriptions) {
                if (fd.isEnabled()) {
                    try {
           				addDataValue(convertStringValue(fd, extractData(fd)));
                    } catch (ParserException e) {
                    	if (fd.isIgnoreDatasetIfInvalid()) {
                    		return false;
                    	} else {
                    		throw e;
                    	}
                    }
                } else {
                	addDataValue(null);
                }
			}
			return true;
		} else {
			return false;
		}
	}
    
    /**
     * parse raw data (one row) and uses the given list of field descriptions
     * @param rawdata to be analyzed
     */
	@Override
	public boolean parseHeaderLine(String rawdata) throws ParserException {
		if (hasFieldDescriptions() == false) {
			throw new IllegalStateException("descriptions cannot be empty or null");
		}
		setRowData(rawdata);
		clearListData();
		if (rowData != null) {
			int countDelimitersInHeader = countDelimitedFields();
			for (int i = 0; i < countDelimitersInHeader; i++) {
				String header = extractDataAtDelimiter(i, 0);
				if (header != null) {
					header = header.trim();
				}
        		addDataValue(header);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * delimiter to be used for field separations
     * @param delimiter
     */
	public void setDelimiter(String delimiter) {
		this.fieldDelimiter = delimiter;
        delimiterChars = getChars(fieldDelimiter);
	}

	public String getDelimiter() {
		return fieldDelimiter;
	}

    /**
     * sets the text enclosure (e.g. " or ') 
     * @param enclosure
     */
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
        enclosureChars = getChars(enclosure);
	}
    
    public String getEnclosure() {
        return enclosure;
    }     

}
