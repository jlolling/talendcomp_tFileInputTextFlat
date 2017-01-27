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

import java.util.List;

public interface FieldTokenizer {

    public static final String NULL = "\\N";

    public int countDelimitedFields() throws ParserException;
    
    public int getCountFieldDescriptions();
    
    /**
     * parse the raw data by the given field descriptions
     * @param rawdata
     * @return true if the parser has data which should be stored in the database
     * @throws ParserException will be thrown if the parser found any failures in the rawdata
     */
    public boolean parseRawData(String rawdata) throws ParserException;
    
    public boolean parseHeaderLine(String rawdata) throws ParserException;
    
    public void setFieldDescriptions(List<FieldDescription> listOfDescriptions);
        
    public void setRowData(String rowData);
    
    /**
	 * returns the field related to the field description with the same index
	 * 
	 * @param index
	 * @return field data
	 */
	public Object getData(int index);
	
	public List<Object> getData();
	
	public List<String> getHeaderData();

	public void setDebug(boolean debug);
	
	public boolean isDebug();

}
