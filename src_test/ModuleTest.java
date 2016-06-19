import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.*;
import org.junit.Test;

import de.jlo.talendcomp.flatfileimport.CSVFileDatasetProvider;
import de.jlo.talendcomp.flatfileimport.ImportAttributes;

public class ModuleTest {

	@Test
	public void testDatasetProvider_multiLine1() throws Exception {
		// """Man"" Wolf"
		String line = "\"\"\"v12\"\"\nx1\";\"v\n13\"" + "\n" + "\"\"\"v12\"\"x2\";v23";
		BufferedReader bf = new BufferedReader(new StringReader(line));
		CSVFileDatasetProvider ds = new CSVFileDatasetProvider();
		ImportAttributes attr = new ImportAttributes();
		attr.setDelimiter(";");
		attr.setEnclosure("\"");
		attr.setIgnoreLineBreakInEnclosedValues(true);
		ds.setupDatasetProvider(bf, attr);
		String row = null;
		int countLines = 0;
		while ((row = ds.getNextDataset()) != null) {
			System.out.println(row);
			countLines++;
			System.out.println("---------------------------");
		}
		assertEquals(2, countLines);
	}
	
	
}
