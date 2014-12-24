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
    
}
