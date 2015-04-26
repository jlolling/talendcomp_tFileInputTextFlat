package de.jlo.talendcomp.flatfileimport;

import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class FieldDescription {

    private String name;
    private int basicType                                  = -1;
    private String dataClassName                           = null;
    private String format                                  = "";
    private static final String DATE_FORMAT_TEMPLATE       = "dd-MM-yyy";
    private int index                                      = 0;
    private static int lastIndex                           = 0;
    private int delimPos                                   = -1;
    private int absPos                                     = -1;
    private int length                                     = 0;
    private String defaultValue                            = "";
    private String alternativeFieldDescriptionName         = null;
    private boolean enabled                                = true;
    private boolean nullEnabled                            = true;
    private boolean ignoreDatasetIfInvalid                 = false;
    private boolean trimRequired                           = false;
    static public final int ABSOLUTE_POSITION              = 0;
    static public final int RELATIVE_POSITION              = 1;
    static public final int DELIMITER_POSITION             = 2;
    static public final int DELIMITER_POSITION_WITH_LENGTH = 3;
    private int positionType                               = -1;
    private boolean valid;
    private String errorMessage;
    public static final String sep = System.getProperty("line.separator");
    private String filterRegex;
    private Pattern filterPattern = null;
    private String regexCompilerMessage;
    private FieldDescription alternativeValueFieldDescription = null;
    private Locale locale = Locale.getDefault();
    static public final int ORACLE_ROWID = -100;
    private boolean ignoreIfMissing = false;
    
    private FieldDescription() {}
    
    public static FieldDescription createDelimited(
    		String name, 
    		String className, 
    		boolean nullable, 
    		int delimiterPos, 
    		int length, 
    		String format, 
    		String regex, 
    		boolean trim, 
    		boolean ignoreIfMissing, 
    		String defaultValue,
    		String alternative) {
    	FieldDescription fd = new FieldDescription();
    	fd.setSchemaColumnIndex(lastIndex++);
    	fd.setName(name);
    	fd.setNullEnabled(nullable);
    	fd.setPositionType(DELIMITER_POSITION);
    	fd.setDelimPos(delimiterPos);
    	fd.setFilterRegex(regex);
    	fd.setTrimRequired(trim);
    	fd.setDataClassName(className);
    	fd.setFormat(format);
    	fd.computePositionType();
    	fd.setIgnoreIfMissing(ignoreIfMissing);
    	fd.setDefaultValue(defaultValue);
    	fd.setAlternativeFieldDescriptionName(alternative);
    	return fd;
    }

    public static FieldDescription createAbsolutePos(
    		String name, 
    		String className, 
    		boolean nullable, 
    		int startPos, 
    		int length, 
    		String format, 
    		String regex, 
    		boolean trim, 
    		String defaultValue,
    		String alternative) {
    	FieldDescription fd = new FieldDescription();
    	fd.setSchemaColumnIndex(lastIndex++);
    	fd.setName(name);
    	fd.setNullEnabled(nullable);
    	fd.setPositionType(ABSOLUTE_POSITION);
    	fd.setAbsPos(startPos);
    	fd.setLength(length);
    	fd.setFilterRegex(regex);
    	fd.setTrimRequired(trim);
    	fd.setDataClassName(className);
    	fd.setFormat(format);
    	fd.computePositionType();
    	fd.setDefaultValue(defaultValue);
    	fd.setAlternativeFieldDescriptionName(alternative);
    	return fd;
    }

    public static FieldDescription createRelativePos(
    		String name, 
    		String className, 
    		boolean nullable, 
    		int length, 
    		String format, 
    		String regex, 
    		boolean trim, 
    		String defaultValue,
    		String alternative) {
    	FieldDescription fd = new FieldDescription();
    	fd.setSchemaColumnIndex(lastIndex++);
    	fd.setName(name);
    	fd.setNullEnabled(nullable);
    	fd.setPositionType(RELATIVE_POSITION);
    	fd.setLength(length);
    	fd.setFilterRegex(regex);
    	fd.setTrimRequired(trim);
    	fd.setDataClassName(className);
    	fd.setFormat(format);
    	fd.computePositionType();
    	fd.setDefaultValue(defaultValue);
    	fd.setAlternativeFieldDescriptionName(alternative);
    	return fd;
    }

    protected FieldDescription(int index, int descriptionIndex, Properties properties) {
        this.index = index;
    	setupFromProperties(descriptionIndex, properties);
        if (positionType == -1) {
            computePositionType();
        }
        if ((basicType == BasicDataType.DATE.getId()) && ((format == null) || (format.trim().length() < 2))) {
            this.format = DATE_FORMAT_TEMPLATE;
        }
    }

    private static Locale createLocale(String localeName) {
        if (localeName == null || localeName.length() == 0) {
            localeName = "en_US";
        }
        Locale locale = null;
        int pos = localeName.indexOf('_');
        if (pos > 1) {
            String language = localeName.substring(0, pos);
            String country = localeName.substring(pos + 1);
            locale = new Locale(language, country);
        } else {
            locale = new Locale(localeName);
        }
        return locale;
    }
    
    public Locale getNumberFormatLocale() {
        return locale;
    }
    
    /**
     * FieldDescription which will be applied if own value is null (after check the defaultValue)
     * @return FieldDescription
     */
    public FieldDescription getAlternativeFieldDescription() {
        return alternativeValueFieldDescription;
    }

    public void setAlternativeFieldDescription(FieldDescription alternativeValueFieldDescription) {
        this.alternativeValueFieldDescription = alternativeValueFieldDescription;
    }
    
    public String getAlternativeFieldDescriptionName() {
        return alternativeFieldDescriptionName;
    }

    public void setAlternativeFieldDescriptionName(String alternativeFieldDescriptionName) {
    	if (alternativeFieldDescriptionName != null && alternativeFieldDescriptionName.trim().isEmpty() == false) {
            this.alternativeFieldDescriptionName = alternativeFieldDescriptionName;
    	}
    }
    
    public boolean isDummy() {
        return getName().startsWith("#");
    }
    
    public String getName() {
        if (name != null) {
            return name;
        } else {
            return "#Test-" + this.delimPos;
        }
    }

    public void setName(String name) {
        if (name != null && name.trim().length() == 0) {
        	name = null;
        }
        this.name = name;
    }

    public int getBasicTypeId() {
        return basicType;
    }

    public void setBasicTypeId(int basicType) {
        this.basicType = basicType;
        dataClassName = BasicDataType.getDataClassName(basicType);
    }

    public String getFieldFormat() {
        return format;
    }

    public void setFormat(String format) {
        if (format != null && format.trim().length() == 0) {
            this.format = null;
        } else {
            this.format = format;
        }
    }

    public int getSchemaColumnIndex() {
        return index;
    }

    public void setSchemaColumnIndex(int index) {
        this.index = index;
    }

    public int getDelimPos() {
        return delimPos;
    }

    public void setDelimPos(int delimPos) {
        this.delimPos = delimPos;
    }

    public int getAbsPos() {
        return absPos;
    }

    public void setAbsPos(int absPos) {
        this.absPos = absPos;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPositionType() {
        return positionType;
    }

    public void setPositionType(int positionType) {
        this.positionType = positionType;
    }

    public boolean validate() {
    	if (enabled == false) {
    		return true;
    	}
        valid = true;
        if ((name == null) || (name.length() == 0)) {
            errorMessage = "Name cannot be null or empty";
            valid = false;
        } else if (basicType == -1 && dataClassName == null) {
            errorMessage = "Missing basic type or data class";
            valid = false;
        } else if (filterRegex != null && filterPattern == null) {
            errorMessage = "filterRegex: " + regexCompilerMessage;
        	valid = false;
        } else {
            if (positionType == -1) {
                errorMessage = "Missing positioning type";
                valid = false;
            } else {
                switch (positionType) {
                    case FieldDescription.ABSOLUTE_POSITION:
                        if (absPos == -1) {
                            errorMessage = "Abolute position not set";
                            valid = false;
                        } else if (length == 0) {
                            errorMessage = "Length is 0";
                            valid = false;
                        }
                        break;
                    case FieldDescription.RELATIVE_POSITION:
                        if (length == 0) {
                            errorMessage = "Length is 0";
                            valid = false;
                        }
                        break;
                    case FieldDescription.DELIMITER_POSITION_WITH_LENGTH:
                        if (delimPos == -1) {
                            errorMessage = "Missing delimiter position";
                            valid = false;
                        } else if (length == 0) {
                            errorMessage = "Length is 0";
                            valid = false;
                        }
                        break;
                    case FieldDescription.DELIMITER_POSITION:
                        if (delimPos == -1) {
                            errorMessage = "Missing delimiter position";
                            valid = false;
                        }
                        break;
                } // switch (positionType)
            } // if (positionType == -1)
        } // if (name == null)
        return valid;
    }

    private void computePositionType() {
        if (basicType == -1) {
            valid = false;
            errorMessage = "Basic type not set";
        } else {
            if (basicType == BasicDataType.DATE.getId()) {
                if (format != null) {
                    valid = true;
                } else {
                    valid = false;
                    errorMessage = "Format is missing"; 
                }
            } // if (basicType == BASICTYPE_DATE)
        }
        if (absPos != -1) {
            positionType = ABSOLUTE_POSITION;
            if (length != 0) {
                valid = true;
            } else {
                valid = false;
                errorMessage = "Length is 0";
            }
        } else if (delimPos != -1) {
            if (length != 0) {
                positionType = DELIMITER_POSITION_WITH_LENGTH;
            } else {
                positionType = DELIMITER_POSITION;
            }
        } else if (length != 0) {
            positionType = RELATIVE_POSITION;
            valid = true;
        } else {
            valid = false;
            errorMessage = "Length is 0";
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String value) {
        this.defaultValue = value;
    }
    
    public String getPropertiesString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("# -----------------------------");
    	sb.append(sep);
    	Properties props = new Properties();
    	fillInProperties(props);
    	Map.Entry<?, ?> entry = null;
    	for (Iterator<?> it = props.entrySet().iterator(); it.hasNext(); ) {
    		entry = (Map.Entry<?, ?>) it.next();
    		sb.append(entry.getKey());
    		sb.append('=');
    		sb.append(entry.getValue());
    		sb.append(sep);
    	}
    	return sb.toString();
    }
    
    public void fillInProperties(Properties props) {
        if (name != null) {
            props.put("COLUMN_" + String.valueOf(index) + "_NAME", name);
            props.put("COLUMN_" + String.valueOf(index) + "_CLASS", dataClassName);
            if (basicType == -1) {
            	basicType = BasicDataType.getIdFromClass(dataClassName);
            }
            props.put("COLUMN_" + String.valueOf(index) + "_BASICTYPE", String.valueOf(basicType));
            if (format != null) {
                props.put("COLUMN_" + String.valueOf(index) + "_FORMAT", format);
            }
            if (locale != null) {
                props.put("COLUMN_" + String.valueOf(index) + "_LOCALE", locale.toString());
            } else {
                props.remove("COLUMN_" + String.valueOf(index) + "_LOCALE");
            }
            props.put("COLUMN_" + String.valueOf(index) + "_POSITIONTYPE", String.valueOf(positionType));
            props.put("COLUMN_" + String.valueOf(index) + "_DELIMITERCOUNT", String.valueOf(delimPos));
            if (absPos != -1) {
                props.put("COLUMN_" + String.valueOf(index) + "_ABSPOS", String.valueOf(absPos));
            }
            if (length > 0) {
                props.put("COLUMN_" + String.valueOf(index) + "_LENGTH", String.valueOf(length));
            }
            props.put("COLUMN_" + String.valueOf(index) + "_ENABLED", String.valueOf(enabled));
            if (defaultValue != null) {
                props.put("COLUMN_" + String.valueOf(index) + "_DEFAULT", defaultValue);
            } else {
                props.remove("COLUMN_" + String.valueOf(index) + "_DEFAULT");
            }
            if (alternativeFieldDescriptionName != null) {
                props.put("COLUMN_" + String.valueOf(index) + "_ALTERNATIVE_FIELD", alternativeFieldDescriptionName);
            } else {
                props.remove("COLUMN_" + String.valueOf(index) + "_ALTERNATIVE_FIELD");
            }
            props.put("COLUMN_" + String.valueOf(index) + "_NULL_ENABLED", String.valueOf(nullEnabled));
            props.put("COLUMN_" + String.valueOf(index) + "_IGNORE_DATASET_IF_INVALID", String.valueOf(ignoreDatasetIfInvalid));
            props.put("COLUMN_" + String.valueOf(index) + "_TRIM", String.valueOf(trimRequired));
            props.put("COLUMN_" + String.valueOf(index) + "_IGNORE_MISSING_COLUMN", String.valueOf(ignoreIfMissing));
            if (filterRegex != null) {
                props.put("COLUMN_" + String.valueOf(index) + "_REGEX", filterRegex);
            } else {
                props.remove("COLUMN_" + String.valueOf(index) + "_REGEX");
            }
        }
    }

    public void setupFromProperties(int descriptionIndex, Properties props) {
        name = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_NAME"); 
        basicType = Integer.parseInt(props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_BASICTYPE", "-1"));
        dataClassName = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_CLASS");
        if (dataClassName == null && basicType != -1) {
        	dataClassName = BasicDataType.getDataClassName(basicType);
        }
        setLocale(props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_LOCALE"));
        format = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_FORMAT");
        positionType = Integer.parseInt(props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_POSITIONTYPE", "0"));
        delimPos = Integer.parseInt(props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_DELIMITERCOUNT", "0"));
        absPos = Integer.parseInt(props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_ABSPOS", "0"));
        length = Integer.parseInt(props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_LENGTH", "0"));
        enabled = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_ENABLED", "false").equals("true");
        defaultValue = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_DEFAULT"); 
        alternativeFieldDescriptionName = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_ALTERNATIVE_FIELD");
        nullEnabled = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_NULL_ENABLED", "false").equals("true");
        ignoreDatasetIfInvalid = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_IGNORE_DATASET_IF_INVALID", "false").equals("true");
        trimRequired = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_TRIM", "false").equals("true");
        ignoreIfMissing = props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_IGNORE_MISSING_COLUMN", "false").equals("true");
        setFilterRegex(props.getProperty("COLUMN_" + String.valueOf(descriptionIndex) + "_REGEX"));
    }

    @Override
	public boolean equals(Object obj) {
    	if (obj instanceof FieldDescription) {
    		String otherName = ((FieldDescription) obj).getName();
    		if (name !=  null && otherName != null && name.toLowerCase().trim().equals(otherName.toLowerCase().trim())) {
    			return true;
    		}
    	}
    	return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	/**
     * ordnet die unterschiedlichen Datentypen zu Basic-Typen und gibt für
     * die ausgewählte Spalte den Basictyp zurück
     * @param index der Spalte
     * @return Basic-Type
     */
    static public int getColumnBasicType(int type) {
        if ((type == Types.VARCHAR) || (type == Types.CHAR)) {
            return BasicDataType.CHARACTER.getId();
        } else if (type == Types.NUMERIC) {
            return BasicDataType.NUMBER.getId();
        } else if (type == Types.INTEGER) {
            return BasicDataType.NUMBER.getId();
        } else if (type == Types.TINYINT) {
            return BasicDataType.NUMBER.getId();
        } else if (type == Types.BIGINT) {
            return BasicDataType.NUMBER.getId();
        } else if (type == Types.FLOAT) {
            return BasicDataType.NUMBER.getId();
        } else if (type == Types.REAL) {
            return BasicDataType.NUMBER.getId();
        } else if (type == Types.DOUBLE) {
            return BasicDataType.NUMBER.getId();
        } else if (type == Types.DECIMAL) {
            return BasicDataType.NUMBER.getId();
        } else if (type == Types.SMALLINT) {
            return BasicDataType.NUMBER.getId();
        } else if ((type == Types.DATE) || (type == Types.TIME) || (type == Types.TIMESTAMP)) {
            return BasicDataType.DATE.getId();
        } else if (type == Types.CLOB) {
            return BasicDataType.CLOB.getId();
        } else if ((type == Types.LONGVARBINARY) || (type == Types.BLOB)) {
            return BasicDataType.NUMBER.getId();
        } else if (type == Types.BOOLEAN) {
        	return BasicDataType.BOOLEAN.getId();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return name; //$NON-NLS-1$
    }

	public boolean isNullEnabled() {
		return nullEnabled;
	}

	public void setNullEnabled(boolean nullEnabled) {
		this.nullEnabled = nullEnabled;
	}

    public boolean isIgnoreDatasetIfInvalid() {
		return ignoreDatasetIfInvalid;
	}

	public void setIgnoreDatasetIfInvalid(boolean ignoreDatasetIfInvalid) {
		this.ignoreDatasetIfInvalid = ignoreDatasetIfInvalid;
	}

	public boolean isTrimRequired() {
        return trimRequired;
    }

    public void setTrimRequired(boolean trim) {
        this.trimRequired = trim;
    }

	public static String getPositioningTypeName(int positioningType) {
		switch (positioningType) {
		case ABSOLUTE_POSITION:
			return "absolute";
		case DELIMITER_POSITION:
			return "afternumberdelimiter";
		case DELIMITER_POSITION_WITH_LENGTH:
			return "afternumberdelimiterwithlength";
		case RELATIVE_POSITION:
			return "following";
		default:
			return "unknown positioning type";
		}
	}

	public String getRegex() {
		return filterRegex;
	}
	
	public Pattern getFilterPattern() {
		return filterPattern;
	}

	public void setFilterRegex(String regex) {
		if (regex != null && regex.trim().length() > 0) {
			this.filterRegex = regex;
	        try {
	            filterPattern = Pattern.compile(regex);
	        } catch (PatternSyntaxException pse) {
	        	filterPattern = null;
	        	regexCompilerMessage = pse.getMessage();
	        }
	    } else {
	    	this.filterRegex = null;
	    	this.filterPattern = null;
	    }
	}

    public void setLocale(String localeString) {
        locale = createLocale(localeString);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

	public String getDataClassName() {
		if (dataClassName == null || dataClassName.isEmpty()) {
			return BasicDataType.getDataClassName(basicType);
		} else {
			return dataClassName;
		}
	}

	public void setDataClassName(String dataClassName) {
		this.dataClassName = dataClassName;
	}

	public boolean isIgnoreIfMissing() {
		return ignoreIfMissing;
	}

	public void setIgnoreIfMissing(boolean ignoreIfMissing) {
		this.ignoreIfMissing = ignoreIfMissing;
	}
	
	public String getParameterString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(" #");
		sb.append(index);
		sb.append(" ,delim pos: ");
		if (delimPos != -1) {
			sb.append(delimPos);
		} else {
			sb.append("not set");
		}
		if (alternativeFieldDescriptionName != null) {
			sb.append(", alternative: " + alternativeFieldDescriptionName);
		}
		if (defaultValue != null) {
			sb.append(", defaultValue: " + defaultValue);
		}
		if (enabled == false) {
			sb.append(" , disabled");
		}
		return sb.toString();
	}
	
	public static void debugOutFieldDescriptions(List<FieldDescription> list) {
		int listIndex = 0;
		System.out.println("-----------------------");
		for (FieldDescription fd : list) {
			System.out.println(listIndex++ + ":" + fd.getParameterString());
		}
	}
	
	public FieldDescription clone() {
		FieldDescription clone = new FieldDescription();
		clone.setAbsPos(absPos);
		clone.setAlternativeFieldDescriptionName(alternativeFieldDescriptionName);
		clone.setAlternativeFieldDescription(alternativeValueFieldDescription);
		if (basicType != -1) {
			clone.setBasicTypeId(basicType);
		}
		clone.setDataClassName(dataClassName);
		clone.setDefaultValue(defaultValue);
		clone.setDelimPos(delimPos);
		clone.setEnabled(enabled);
		clone.setFilterRegex(filterRegex);
		clone.setFormat(format);
		clone.setIgnoreDatasetIfInvalid(ignoreDatasetIfInvalid);
		clone.setIgnoreIfMissing(ignoreIfMissing);
		clone.setSchemaColumnIndex(index);
		clone.setLength(length);
		clone.setLocale(locale);
		clone.setName(name);
		clone.setNullEnabled(nullEnabled);
		clone.setPositionType(positionType);
		clone.setTrimRequired(trimRequired);
		return clone;
	}
	
}
