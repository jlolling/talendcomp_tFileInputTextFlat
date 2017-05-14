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

import java.util.HashMap;

/**
 *
 * @author jan
 */
public final class BasicDataType {

    private int id;
    private String name;
    private static HashMap<Integer, BasicDataType> list = new HashMap<Integer, BasicDataType>();
    public static final BasicDataType CHARACTER = new BasicDataType(0, "Character");
    public static final BasicDataType DATE = new BasicDataType(1, "Date");
    public static final BasicDataType NUMBER = new BasicDataType(2, "Number");
    public static final BasicDataType CLOB = new BasicDataType(4, "CLOB");
    public static final BasicDataType BOOLEAN = new BasicDataType(8, "Boolean");

    private BasicDataType(int id, String name) {
        this.id = id;
        this.name = name;
        if (list.containsKey(id)) {
            throw new IllegalArgumentException("id=" + id + " already exists");
        }
        list.put(id, this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static BasicDataType getBasicDataType(int id) {
        return list.get(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BasicDataType) {
            return id == ((BasicDataType) o).getId();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.id;
        return hash;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static int getIdFromClass(String className) {
    	if ("String".equals(className)) {
    		return CHARACTER.id;
    	} else if ("Boolean".equals(className)) {
    		return BOOLEAN.id;
    	} else if ("Date".equals(className)) {
    		return DATE.id;
    	} else if ("Timestamp".equals(className)) {
    		return DATE.id;
    	} else if ("Short".equals(className)) {
    		return NUMBER.id;
    	} else if ("Integer".equals(className)) {
    		return NUMBER.id;
    	} else if ("Long".equals(className)) {
    		return NUMBER.id;
    	} else if ("BigDecimal".equals(className)) {
    		return NUMBER.id;
    	} else if ("Double".equals(className)) {
    		return NUMBER.id;
    	} else if ("Float".equals(className)) {
    		return NUMBER.id;
    	} else {
    		return CHARACTER.id;
    	}
    }
    
    public static String getDataClassName(int id) {
    	if (id == BOOLEAN.id) {
    		return "Boolean";
    	} else if (id == CHARACTER.id) {
    		return "String";
    	} else if (id == CLOB.id) {
    		return "String";
    	} else if (id == DATE.id) {
    		return "Date";
    	} else if (id == NUMBER.id) {
    		return "Double";
    	} else {
    		throw new RuntimeException("unsupported id=" + id);
    	}
    }

    public static boolean isNumberType(int type) {
    	return type == NUMBER.id;
    }

    public static boolean isStringType(int type) {
    	return type == CLOB.id || type == CHARACTER.id;
    }

    public static boolean isDateType(int type) {
    	return type == DATE.id;
    }

    public static boolean isBooleanType(int type) {
    	return type == BOOLEAN.id;
    }

}
