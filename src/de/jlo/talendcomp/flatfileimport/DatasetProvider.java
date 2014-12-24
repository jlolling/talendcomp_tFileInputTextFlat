package de.jlo.talendcomp.flatfileimport;

import java.io.File;

public interface DatasetProvider {

    void setupDatasetProvider(File file, ImportAttributes properties) throws Exception;
    
    String getNextDataset() throws Exception;

    void closeDatasetProvider();
    
    FieldTokenizer createParser(ImportAttributes properties);
    
    long getCurrentRowNum();
    
    void skipEmptyLines(boolean skip);
    
    boolean isSkipEmptyLines();

}
