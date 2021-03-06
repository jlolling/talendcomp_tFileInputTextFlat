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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jan.lolling@gmail.com
 */
public abstract class AbstractFieldTokenizer implements FieldTokenizer {

    private final List<Object> fieldDataList = new ArrayList<Object>(100);
    protected List<FieldDescription> descriptions;
    private boolean ignoreNotNullConstraints = false;
    private TypeUtil typeUtil = new TypeUtil();
    protected boolean debug = false;

    protected int getListDataSize() {
        return fieldDataList.size();
    }
    
    protected void clearListData() {
        fieldDataList.clear();
    }
    
    protected void addDataValue(Object value) {
    	if (value == null) {
    		value = NULL;
    	}
        fieldDataList.add(value);
    }
    
    @Override
	public Object getData(int fieldDescriptionIndex) {
    	return fieldDataList.get(fieldDescriptionIndex);
	}
    
	@Override
    public List<Object> getData() {
    	return fieldDataList;
    }

    @Override
	public List<String> getHeaderData() {
    	List<String> headers = new ArrayList<String>();
    	for (Object o : fieldDataList) {
    		headers.add(o.toString());
    	}
    	return headers;
    }
    
    protected boolean hasFieldDescriptions() {
        return descriptions != null && descriptions.isEmpty() == false;
    }

    @Override
    public int getCountFieldDescriptions() {
        if (hasFieldDescriptions() == false) {
            return 0;
        } else {
            return descriptions.size();
        }
    }
    
    protected FieldDescription getFieldDescriptionAt(int index) {
        return descriptions.get(index);
    }

    protected FieldDescription getAlternativeFieldDescriptionFor(int index) {
        return descriptions.get(index).getAlternativeFieldDescription();
    }

    @Override
	public void setFieldDescriptions(List<FieldDescription> listDescriptions) {
		if (listDescriptions == null || listDescriptions.isEmpty()) {
			throw new IllegalArgumentException("listDescriptions cannot be empty or null");
		}
		this.descriptions = listDescriptions;
	}
    
    /**
     * regex filter of a content
     * @param pattern
     * @param content
     * @return filtered text
     */
	protected static String filter(Pattern pattern, String content) {
		if (pattern != null) {
			if (content != null) {
				final StringBuffer sb = new StringBuffer();
		        Matcher matcher = pattern.matcher(content);
		        while (matcher.find()) {
		            if (matcher.start() < matcher.end()) {
		                sb.append(matcher.group());
		            }
		        }
		        content = sb.toString();
			}
		}
		if (NULL.equals(content)) {
			content = null;
		}
		return content;
	}

    protected Object convertStringValue(FieldDescription fd, String strValue) throws ParserException {
        Object value = null;
    	try {
    		value = typeUtil.convertToDatatype(strValue, fd.getDataClassName(), fd.getFieldFormat());
		} catch (Exception e) {
			throw new ParserException("Column " + fd.getName() + " convert value failed:" + e.getMessage(), e);
		}
    	if (ignoreNotNullConstraints == false && value == null && fd.isNullEnabled() == false && fd.isIgnoreIfMissing() == false) {
    		throw new ParserException("Column " + fd.getName() + " convert value failed: Null not allowed");
    	}
        return value;
    }

	public boolean isIgnoreNotNullConstraints() {
		return ignoreNotNullConstraints;
	}

	public void setIgnoreNotNullConstraints(boolean ignoreNotNullConstraints) {
		this.ignoreNotNullConstraints = ignoreNotNullConstraints;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
