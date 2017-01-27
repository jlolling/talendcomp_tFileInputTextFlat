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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Importer {
	
	private File inputFile;
	private DatasetProvider dsp = null;
	private FieldTokenizer ft = null;
	private final ImportAttributes attr = new ImportAttributes();
	private final List<FieldDescription> fields = new ArrayList<FieldDescription>();
	private String currentDataset = null;
	private boolean headerLineSkipped = false;
	private Map<Integer, Integer> schemaToStorageIndexMap = new HashMap<Integer, Integer>();
	private boolean debug = false;
	private boolean findHeaderPosByRegex = false;
	
	public void setImportFile(String file) {
		inputFile = new File(file);
	}
	
	public String getImportFile() {
		if (inputFile != null) {
			return inputFile.getAbsolutePath();
		} else {
			return null;
		}
	}

    private ArrayList<Integer> collectFieldDescriptionIndexes(Properties properties) {
        ArrayList<Integer> listIndexes = new ArrayList<Integer>();
        int index = 0;
        for (Object key : properties.keySet()) {
            index = extractIndexFromKey((String) key);
            if (index != -1) {
                Integer indexObj = Integer.valueOf(index);
                if (listIndexes.contains(indexObj) == false) {
                    listIndexes.add(indexObj);
                }
            }
        }
        Collections.sort(listIndexes);
        return listIndexes;
    }

    private int extractIndexFromKey(String key) {
        int index = -1;
        if (key.startsWith("COLUMN_") && key.endsWith("_NAME")) {
            int p0 = key.indexOf("_");
            if (p0 != -1) {
                int p1 = key.indexOf("_", p0 + 1);
                if (p1 != -1) {
                    index = Integer.parseInt(key.substring(p0 + 1, p1));
                }
            }
        }
        return index;
    }

    private FieldDescription getFieldDescription(String name) {
    	if (name == null || name.isEmpty()) {
    		throw new IllegalArgumentException("getFieldDescription name cannot be null or empty");
    	}
        FieldDescription fd = null;
        for (int i = 0; i < fields.size(); i++) {
            fd = fields.get(i);
            if (fd.getName() != null && fd.getName().equalsIgnoreCase(name.trim())) {
                return fd;
            }
        }
        return null;
    }

    public void initialize() throws Exception {
    	if (fields.isEmpty()) {
    		throw new Exception("Column list is empty!");
    	}
        for (int d = 0; d < fields.size(); d++) {
            FieldDescription fd = fields.get(d);
            if (fd.getAlternativeFieldDescriptionName() != null) {
            	FieldDescription altFd = getFieldDescription(fd.getAlternativeFieldDescriptionName());
            	if (altFd != null) {
            		if (altFd.isEnabled()) {
                        fd.setAlternativeFieldDescription(altFd);
            		} else {
            			System.err.println();
            		}
            	} else {
                    throw new Exception("Check column: " + fd.toString() + ": the alternativ column: " + fd.getAlternativeFieldDescriptionName() + " does not exits.");
            	}
            }
            if (fd.isEnabled()) {            	
                if (fd.validate() == false) {
                    throw new Exception("Check column: " + fd.toString() + ":" + fd.getErrorMessage());
                } else {
                    if (BasicDataType.isNumberType(fd.getBasicTypeId()) && fd.getFieldFormat() == null) {
                        System.err.println("Column " + fd + " has numeric type without defined number locale. This can result in wrong parsing if value is a fraction number");
                    }
                }
            }
        }
		if (inputFile.exists() == false) {
			throw new Exception("Input file:" + inputFile.getAbsolutePath() + " does not exists");
		}
		if (inputFile.canRead() == false) {
			throw new Exception("Cannot read input file:" + inputFile.getAbsolutePath());
		}
		dsp = new CSVFileDatasetProvider();
		dsp.setupDatasetProvider(inputFile, attr);
		ft = dsp.createParser(attr);
		ft.setDebug(debug);
		if (debug) {
			System.out.println("\nInitial set list of columns:");
			FieldDescription.debugOutFieldDescriptions(fields);
		}
    	sortFieldDescriptions();
		headerLineSkipped = false;
	}
	
	public void setFileCharset(String charset) {
		attr.setCharsetName(charset);
	}
	
	public void skipBOM(boolean skip) {
		attr.setIgnoreBOM(skip);
	}
	
	public void setDelimiter(String delimiter) {
		attr.setDelimiter(delimiter);
	}
	
	public void setEnclosure(String enclosure) {
		attr.setEnclosure(enclosure);
	}
	
	public void setHasHeaderRow(boolean hasHaeder) {
		attr.setHeaderLine(hasHaeder);
	}
	
	public void setRowsToSkip(int skippedRows) {
		attr.setCountSkipRows(skippedRows);
	}
	
	public void setSkipEmptyLines(boolean skip) {
		attr.setSkipEmptyLines(skip);
	}

	public void setIgnoreLinebreakInEnclosures(boolean ignore) {
		attr.setIgnoreLineBreakInEnclosedValues(ignore);
	}
	
	public void setIgnoreNotNullConstraints(boolean ignore) {
		attr.setIgnoreNotNullConstraint(ignore);
	}
	
	public void allowEnclosureInFieldContent(boolean allow) {
		attr.setAllowEnclosureInText(allow);
	}
	
	public void addFieldDescription(FieldDescription fd) {
		fields.add(fd);
		fd.setSchemaColumnIndex(fields.indexOf(fd));
	}
	
	public void skipTopRows() {
		if (attr.getCountSkipRows() > 0) {
			for (int i = 0; i < attr.getCountSkipRows(); i++) {
				try {
					dsp.getNextDataset();
				} catch (Exception e) {
					// ignore errors here
				}
			}
		}
	}
	
	public void skipHeaderRow() throws Exception {
		if (attr.hasHeaderLine()) {
			currentDataset = dsp.getNextDataset();
			headerLineSkipped = true;
		}
	}
	
	private int findPosition(List<String> headers, String pattern) {
		int pos = 0;
		boolean found = false;
		if (findHeaderPosByRegex) {
			if (pattern.startsWith("^") == false) {
				pattern = "^" + pattern;
			}
			if (pattern.endsWith("$") == false) {
				pattern = pattern + "$";
			}
			Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			for (String header : headers) {
				Matcher m = p.matcher(header);
				if (m.find()) {
					found = true;
					break;
				}
				pos++;
			}
		} else {
			for (String header : headers) {
				if (header.equalsIgnoreCase(pattern)) {
					found = true;
					break;
				}
				pos++;
			}
		}
		if (found) {
			return pos;
		} else {
			return -1;
		}
	}
	
	private void sortList(List<FieldDescription> listFd) {
		Collections.sort(listFd, new Comparator<FieldDescription>() {

			@Override
			public int compare(FieldDescription o1, FieldDescription o2) {
		    	if (o1 != null && o2 != null) {
		    		if (o1.isEnabled() && o2.isEnabled()) {
			    		if (o1.getPositionType() == FieldDescription.DELIMITER_POSITION || o1.getPositionType() == FieldDescription.DELIMITER_POSITION_WITH_LENGTH) {
			    			return o1.getDelimPos() - o2.getDelimPos();
			    		} else {
			    			return o1.getAbsPos() - o2.getAbsPos();
			    		}
		    		} else if (o1.isEnabled() == false && o2.isEnabled()) {
		    			return 1;
		    		} else if (o1.isEnabled() && o2.isEnabled() == false) {
		    			return -1;
		    		} else {
		    			return 0;
		    		}
		    	} else {
		    		return 0;
		    	}
			}
			
		});
	}
	
	public void reconfigureFieldDescriptionByHeaderLine() throws Exception {
		if (attr.hasHeaderLine()) {
			if (debug) {
				System.out.println("\nConfigure field positions by header line...");
			}
			String headerLine = (String) (headerLineSkipped ? currentDataset : dsp.getNextDataset());
			if (ft.parseHeaderLine(headerLine)) {
				List<String> headers = ft.getHeaderData();
				for (FieldDescription fd : fields) {
					int pos = findPosition(headers, fd.getName());
					if (pos == -1) {
						if (fd.isIgnoreIfMissing() == false) {
							throw new Exception("Column " + fd.getName() + " not found in header line!");
						} else {
							fd.setEnabled(false); // disable field to avoid fetching values
						}
					}
					fd.setDelimPos(pos); // position given by header
				}
				sortFieldDescriptions();
			} else {
				System.err.println("Could not configure columns by header line because there is not header line");
			}
		} else {
			throw new Exception("Reconfiguring column list failed because none header line is set");
		}
	}
	
	private void sortFieldDescriptions() {
		sortList(fields);
		if (debug) {
			System.out.println("\nReordered list of columns:");
			FieldDescription.debugOutFieldDescriptions(fields);
		}
		ft.setFieldDescriptions(fields);
		for (int i = 0, n = fields.size(); i < n; i++) {
			FieldDescription fd = fields.get(i);
			schemaToStorageIndexMap.put(fd.getSchemaColumnIndex(), i);
		}
	}
	
	private int getFieldIndex(int index) {
		Integer i = schemaToStorageIndexMap.get(index);
		if (i == null) {
			return index;
		} else {
			return i;
		}
	}
	
	public boolean nextDataRow() throws Exception {
		if (dsp == null) {
			throw new Exception("Dataset provider not initialized!");
		} else {
			currentDataset = dsp.getNextDataset();
			if (currentDataset != null) {
				ft.parseRawData(currentDataset);
				return true;
			} else {
				return false;
			}
		}
	}
	
	public void close() {
		if (dsp != null) {
			dsp.closeDatasetProvider();
		}
	}
	
	public String getCurrentLine() {
		return currentDataset;
	}
	
	public Object getData(int schemaColumnIndex) {
    	return getData(fields.get(getFieldIndex(schemaColumnIndex)));
	}
    
	private Object getData(FieldDescription fd) {
		if (fd == null) {
			return FieldTokenizer.NULL;
		}
		Object value = ft.getData(getFieldIndex(fd.getSchemaColumnIndex()));
		if (value == FieldTokenizer.NULL) {
			return getData(fd.getAlternativeFieldDescription());
		}
    	return value;
	}
	
	public Object getObjectAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : new Object();
		} else {
			return value;
		}
	}
	
	public String getStringAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : "";
		} else {
			return (String) value;
		}
	}

	public Character getCharAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : ' ';
		} else {
			return (Character) value;
		}
	}

	public Integer getIntegerAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : 0;
		} else {
			return (Integer) value;
		}
	}

	public Short getShortAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : (short) 0;
		} else {
			return (Short) value;
		}
	}

	public Long getLongAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : 0l;
		} else {
			return (Long) value;
		}
	}

	public Double getDoubleAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : 0d;
		} else {
			return (Double) value;
		}
	}

	public Float getFloatAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : 0f;
		} else {
			return (Float) value;
		}
	}

	public Boolean getBooleanAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : false;
		} else {
			return (Boolean) value;
		}
	}

	public BigDecimal getBigDecimalAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : BigDecimal.ZERO;
		} else {
			return (BigDecimal) value;
		}
	}

	public Date getDateAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : new Date(0l);
		} else {
			return (Date) value;
		}
	}
	
	public Timestamp getTimestampAt(int index, boolean nullable) {
		Object value = getData(index);
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : new Timestamp(0l);
		} else {
			return (Timestamp) value;
		}
	}

	public long getCurrentLineNum() {
		if (dsp == null) {
			throw new IllegalStateException("Dataset provider not initialized!");
		} else {
			return dsp.getCurrentRowNum();
		}
	}

	public List<FieldDescription> getFieldDescriptions() {
		return fields;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isFindHeaderPosByRegex() {
		return findHeaderPosByRegex;
	}

	public void setFindHeaderPosByRegex(boolean findHeaderPosByRegex) {
		this.findHeaderPosByRegex = findHeaderPosByRegex;
	}
	
	public void loadFieldDescriptionConfiguration(String configFilePath) throws Exception {
		if (configFilePath == null || configFilePath.trim().isEmpty()) {
			throw new IllegalArgumentException("configFilePath cannot be null or empty");
		}
		File configFile = new File(configFilePath);
		if (configFile.canRead() == false) {
			throw new Exception("Configuration file cann not be read or does not exists: " + configFilePath);
		}
		initConfig(loadProperties(configFile));
	}

    private Properties loadProperties(File propertiesFile) throws Exception {
		if (propertiesFile.getName().endsWith(".importconfig") == false) {
			throw new Exception("Unknown configuration file format! It must be of *.importconfig!");
		}
        FileInputStream fin = null;
        final Properties importProperties = new Properties();
        try {
            fin = new FileInputStream(propertiesFile);
            importProperties.load(fin);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception e) {}
            }
        }
        return importProperties;
    }

    private void initConfig(Properties importProperties) throws Exception {
        if (importProperties == null || importProperties.isEmpty()) {
            throw new IllegalArgumentException("ImportProperties cannot be null or empty");
        }
    	attr.setupFrom(importProperties);
        ArrayList<Integer> listIndexes = collectFieldDescriptionIndexes(importProperties);
        fields.clear();
        int index = 0;
        for (Integer propertySearchIndex : listIndexes) {
            fields.add(new FieldDescription(index++, propertySearchIndex.intValue(), importProperties));
        }
        initialize();
    }
    
    public void saveConfigToFile(String configFilePath, boolean renableFieldsNotFound) throws Exception {
    	if (configFilePath.trim().toLowerCase().endsWith(".importconfig") == false) {
    		configFilePath = configFilePath.trim() + ".importconfig";
    	}
    	Properties props = new Properties();
    	attr.storeInto(props);
    	int lastPos = 0;
    	if (renableFieldsNotFound) {
        	for (FieldDescription fd : fields) {
        		if (lastPos <= fd.getDelimPos()) {
        			lastPos = fd.getDelimPos() + 1;
        		}
        		if (lastPos <= fd.getAbsPos()) {
        			lastPos = fd.getAbsPos() + 1;
        		}
        	}
    	}
    	for (FieldDescription fd : fields) {
    		if (renableFieldsNotFound) {
        		FieldDescription temp = fd.clone();
    			if (temp.isEnabled() == false) {
    				temp.setEnabled(true);
    			}
    			if ((temp.getPositionType() == FieldDescription.DELIMITER_POSITION || temp.getPositionType() == FieldDescription.DELIMITER_POSITION_WITH_LENGTH) && temp.getDelimPos() == -1) {
    				temp.setDelimPos(lastPos++);
    			}
        		temp.fillInProperties(props);
    		} else if (fd.isEnabled()) {
        		fd.fillInProperties(props);
    		}
    	}
    	File configFile = new File(configFilePath);
    	if (configFile.getParentFile().exists() == false) {
    		configFile.getParentFile().mkdirs();
    	}
    	TreeMap<Object, Object> map = new TreeMap<Object, Object>(props);
    	StringBuffer sb = new StringBuffer();
    	for (Map.Entry<Object, Object> entry : map.entrySet()) {
    		sb.append(entry.getKey());
    		sb.append("=");
    		sb.append(entry.getValue() != null ? entry.getValue() : "");
    		sb.append("\n");
    	}
        final FileWriter fw = new FileWriter(configFile);
        fw.append(sb.toString());
        fw.flush();
        fw.close();
    }

}
