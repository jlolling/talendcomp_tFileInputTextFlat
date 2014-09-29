package de.cimt.talendcomp.flatfileimport;

import java.io.File;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Importer {
	
	private File inputFile;
	private DatasetProvider dsp = null;
	private FieldTokenizer ft = null;
	private Properties properties;
	private final ImportAttributes attr = new ImportAttributes();
	private final List<FieldDescription> fields = new ArrayList<FieldDescription>();
	private String currentDataset = null;
	private boolean headerLineSkipped = false;
	private Map<Integer, Integer> fdIndexMap = new HashMap<Integer, Integer>();
	private boolean debug = false;
	private boolean findHeaderPosByRegex = false;
	
	public void setImportFile(String file) {
		inputFile = new File(file);
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

    public void initializeFromProperties() throws Exception {
		attr.setupFrom(properties);
	    ArrayList<Integer> listIndexes = collectFieldDescriptionIndexes(properties);
	    int index = 0;
	    for (Integer descriptionIndex : listIndexes) {
	        fields.add(new FieldDescription(index++, descriptionIndex.intValue(), properties));
	    }
	    initialize();
	}
    
    public void initialize() throws Exception {
    	if (fields.isEmpty()) {
    		throw new Exception("Field description list is empty!");
    	}
    	for (int d = 0; d < fields.size(); d++) {
	        FieldDescription fd = fields.get(d);
	        if (fd.validate() == false) {
	        	throw new Exception("Field " + fd + " is invalid:" + fd.getErrorMessage());
	        }
	        if (fd.getAlternativeFieldDescriptionName() != null) {
	            fd.setAlternativeFieldDescription(getFieldDescription(fd.getAlternativeFieldDescriptionName()));
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
			System.out.println("Initial set list of field descriptions:" + fields.hashCode());
			FieldDescription.debugOutFieldDescriptions(fields);
		}
		ft.setFieldDescriptions(fields);
		headerLineSkipped = false;
	}
	
	public void setFileCharset(String charset) {
		attr.setCharsetName(charset);
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
	
	public void setConfig(Properties properties) {
		this.properties = properties;
	}
	
	public void addFieldDescription(FieldDescription fd) {
		fields.add(fd);
		fd.setIndex(fields.indexOf(fd));
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
			String headerLine = (String) (headerLineSkipped ? currentDataset : dsp.getNextDataset());
			if (ft.parseHeaderLine(headerLine)) {
				List<String> headers = ft.getHeaderData();
				for (FieldDescription fd : fields) {
					int pos = findPosition(headers, fd.getName());
					if (pos == -1) {
						if (fd.isIgnoreIfMissing() == false) {
							throw new Exception("Field " + fd.getName() + " not found in header line!");
						} else {
							fd.setEnabled(false); // disable field to avoid fetching values
						}
					}
					fd.setDelimPos(pos); // position given by header
				}
				sortList(fields);
				if (debug) {
					System.out.println("Reordered list of field descriptions:"+fields.hashCode());
					FieldDescription.debugOutFieldDescriptions(fields);
				}
				ft.setFieldDescriptions(fields);
				for (int i = 0, n = fields.size(); i < n; i++) {
					FieldDescription fd = fields.get(i);
					fdIndexMap.put(fd.getIndex(), i);
				}
			} else {
				System.err.println("Could not configure fields by header line because there is not header line");
			}
		} else {
			throw new Exception("reconfigureFieldDescriptionByHeaderLine failed because not header line is set");
		}
	}
	
	private int getFieldIndex(int index) {
		Integer i = fdIndexMap.get(index);
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
	
	public Object getObjectAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : new Object();
		} else {
			return value;
		}
	}
	
	public String getStringAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : "";
		} else {
			return (String) value;
		}
	}

	public Character getCharAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : ' ';
		} else {
			return (Character) value;
		}
	}

	public Integer getIntegerAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : 0;
		} else {
			return (Integer) value;
		}
	}

	public Short getShortAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : (short) 0;
		} else {
			return (Short) value;
		}
	}

	public Long getLongAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : 0l;
		} else {
			return (Long) value;
		}
	}

	public Double getDoubleAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : 0d;
		} else {
			return (Double) value;
		}
	}

	public Float getFloatAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : 0f;
		} else {
			return (Float) value;
		}
	}

	public Boolean getBooleanAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : false;
		} else {
			return (Boolean) value;
		}
	}

	public BigDecimal getBigDecimalAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : BigDecimal.ZERO;
		} else {
			return (BigDecimal) value;
		}
	}

	public Date getDateAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
		if (value == FieldTokenizer.NULL) {
			return nullable ? null : new Date(0l);
		} else {
			return (Date) value;
		}
	}
	
	public Timestamp getTimestampAt(int index, boolean nullable) {
		Object value = ft.getData(getFieldIndex(index));
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
	
}
