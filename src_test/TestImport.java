import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.jlo.talendcomp.flatfileimport.CSVFieldTokenizer;
import de.jlo.talendcomp.flatfileimport.CSVFileDatasetProvider;
import de.jlo.talendcomp.flatfileimport.DatasetProvider;
import de.jlo.talendcomp.flatfileimport.FieldDescription;
import de.jlo.talendcomp.flatfileimport.ImportAttributes;
import de.jlo.talendcomp.flatfileimport.Importer;
import de.jlo.talendcomp.flatfileimport.ParserException;


public class TestImport {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		testTokenizer2();
	}
	
	public static void testTokenizer() { 
		String line = "\"\"feld1\nnext line\";\"feld \"  ;xx \"  \"2\";feld3;\"\";\"xxx\";\"  \"";
		CSVFieldTokenizer t = new CSVFieldTokenizer();
		t.setDelimiter(";");
		t.setEnclosure("\"");
		t.setAllowEnclosureInText(true);
		t.setRowData(line);
		try {
			System.out.println("0=" + t.extractDataAtDelimiter(0, 0));
			System.out.println("1=" + t.extractDataAtDelimiter(1, 0));
			System.out.println("2=" + t.extractDataAtDelimiter(2, 0));
			System.out.println("3=" + t.extractDataAtDelimiter(3, 0));
			System.out.println("4=" + t.extractDataAtDelimiter(4, 0));
			System.out.println("5=" + t.extractDataAtDelimiter(5, 0));
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testTokenizer2() {
		String line = "f1;	;f3;f4;f5;f6";
		CSVFieldTokenizer t = new CSVFieldTokenizer();
		t.setDelimiter(";");
		t.setAllowEnclosureInText(true);
		t.setRowData(line);
		try {
			System.out.println("0=" + t.extractDataAtDelimiter(0, 0));
			System.out.println("1=" + t.extractDataAtDelimiter(1, 0));
			System.out.println("2=" + t.extractDataAtDelimiter(2, 0));
			System.out.println("3=" + t.extractDataAtDelimiter(3, 0));
			System.out.println("4=" + t.extractDataAtDelimiter(4, 0));
			System.out.println("5=" + t.extractDataAtDelimiter(5, 0));
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testDatasetProvider() {
		DatasetProvider p = new CSVFileDatasetProvider();
		ImportAttributes properties = new ImportAttributes();
		properties.setEnclosure("\"");
		properties.setDelimiter(";");
		properties.setIgnoreLineBreakInEnclosedValues(true);
		try {
			File f = new File("/home/jlolling/test/text/test_missing.txt");
			p.setupDatasetProvider(f, properties);
			int i = 0;
			while (true) {
				String line = p.getNextDataset();
				if (line == null) {
					break;
				}
				System.out.println(i + "#" + line);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testDifferentFieldOrder() {
		Importer importer = new Importer();
		importer.setDebug(false);
		importer.setImportFile("/private/var/testdata/text/different_schemas/test_jan.csv");
		importer.setFileCharset("UTF-8");
		int pos = 0;
		importer.addFieldDescription(FieldDescription.createDelimited("f1", "String", true, pos++, 0, null, null, false, false, null, null));
		importer.addFieldDescription(FieldDescription.createDelimited("f4", "String", true, pos++, 0, null, null, false, false, null, null));
		importer.addFieldDescription(FieldDescription.createDelimited("f2", "String", false, pos++, 0, null, null, false, false, null, null));
		importer.addFieldDescription(FieldDescription.createDelimited("f3", "String", false, pos++, 0, null, null, false, false, null, null));
//		importer.addFieldDescription(FieldDescription.createDelimited("f5", "String", true, pos++, 0, null, null, false, false));
		importer.setDelimiter("|");
		importer.setHasHeaderRow(true);
		importer.setRowsToSkip(0);
		importer.setIgnoreNotNullConstraints(true);
		try {
			importer.initialize();
			importer.skipTopRows();
			importer.skipHeaderRow();
			importer.reconfigureFieldDescriptionByHeaderLine();
			int line = 0;
			long start = System.currentTimeMillis();
			System.out.println("##########################");
			while (true) {
				try {
					if (importer.nextDataRow() == false) {
						break;
					}
				} catch (Exception e1) {
					System.err.println("line " + line + ": " + e1.getMessage());
				}
				line++;
				for (int c = 0; c < pos; c++) {
					System.out.println(c + ":" + importer.getObjectAt(c, true));
				}
				System.out.println("##########################");
			}
			long stop = System.currentTimeMillis();
			System.out.println(line + " in " + (stop - start) + " ms speed=" + (line / ((stop - start) / 1000f)) + " rows/s");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testConfigureFieldByRegex() {
		Importer importer = new Importer();
		importer.setDebug(false);
		importer.setImportFile("/private/var/testdata/text/different_schemas/test_schema2.csv");
		importer.setFileCharset("UTF-8");
		int pos = 0;
		importer.addFieldDescription(FieldDescription.createDelimited("customer.Type", "String", true, pos++, 0, null, null, false, false, null, null));
		importer.addFieldDescription(FieldDescription.createDelimited("product", "String", true, pos++, 0, null, null, false, false, null, null));
		importer.addFieldDescription(FieldDescription.createDelimited("f[3]", "String", false, pos++, 0, null, null, false, false, null, null));
		importer.addFieldDescription(FieldDescription.createDelimited("F4", "String", false, pos++, 0, null, null, false, false, null, null));
		importer.addFieldDescription(FieldDescription.createDelimited("f5", "String", true, pos++, 0, null, null, false, false, null, null));
		importer.setDelimiter("|");
		importer.setHasHeaderRow(true);
		importer.setRowsToSkip(0);
		importer.setIgnoreNotNullConstraints(true);
		try {
			importer.initialize();
			importer.skipTopRows();
			importer.skipHeaderRow();
			importer.setFindHeaderPosByRegex(true);
			importer.reconfigureFieldDescriptionByHeaderLine();
			int line = 0;
			long start = System.currentTimeMillis();
			System.out.println("##########################"); 
			while (true) {
				try {
					if (importer.nextDataRow() == false) {
						break;
					}
				} catch (Exception e1) {
					System.err.println("line " + line + ": " + e1.getMessage());
				}
				line++;
				for (int c = 0; c < pos; c++) {
					System.out.println(c + ":" + importer.getObjectAt(c, true));
				}
				System.out.println("##########################");
			}
			long stop = System.currentTimeMillis();
			System.out.println(line + " in " + (stop - start) + " ms speed=" + (line / ((stop - start) / 1000f)) + " rows/s");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class row2Struct  {
	
		public String ID_MAG;
		public String ID_KLT;
		public String ID_PLT;
		public String ID_PRAC;
		public String NAZWA;
		public String SKROT;
		public String ID_W;
		public String ID_P;
		public String MIASTO;
		public String KOD;
		public String ULICA;
		public String NR_LOK;
		public String NIP;
		public String TEL;
		public String KOM;
		public String FAX;
		public String EMAIL;
		public Float RABAT;
		public String ID_FOR_PLT;
		public Integer TER_PLT;
		public String ID_CENNIK;
		public String ID_GC_KLT;
		public String ID_GL_KLT;
		public String ID_GR_KLT;
		public String ID_GRX_KLT;
		public Float LIM_KRED;
		public Float SALDO_PLT;
		public Float LIM_PRZET;
		public Float SALDO_PRZET;
		public Integer WSK_BLOK;
		public String ID_TYP_KLT;
		public String ID_KAT_KLT;
		public String ID_SIEC;
		public String ID_POW;
		public String ID_ST_KLT;
		public Integer ST_MOB;
		public Integer WSK_DOMYSLNY;
	
	}

	
	public static void testTalendCode() throws Exception {
		Map<String, Object> globalMap = new HashMap<String, Object>();
		row2Struct row2 = null;
		de.jlo.talendcomp.flatfileimport.Importer tFileInputTextFlat_2 = new de.jlo.talendcomp.flatfileimport.Importer();
		tFileInputTextFlat_2.setDebug(false);
		tFileInputTextFlat_2
				.setImportFile("/private/var/testdata/text/different_schemas/test2.txt");
		tFileInputTextFlat_2.setFileCharset("ISO-8859-15");
		tFileInputTextFlat_2.setSkipEmptyLines(true);
		tFileInputTextFlat_2.setHasHeaderRow(true);
		tFileInputTextFlat_2.setIgnoreNotNullConstraints(false);
		tFileInputTextFlat_2.setDelimiter("|");
		// helper to configure fields and get values
		final class ImportHelper_tFileInputTextFlat_2 {

			public void configureFields(
					de.jlo.talendcomp.flatfileimport.Importer importer) {
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_MAG", "String", true, 0,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_KLT", "String", true, 1,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_PLT", "String", true, 2,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_PRAC", "String", true, 3,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("NAZWA", "String", true, 4, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("SKROT", "String", true, 5, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_W", "String", true, 6, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_P", "String", true, 7, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("MIASTO", "String", true, 8,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("KOD", "String", true, 9, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ULICA", "String", true, 10,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("NR_LOK", "String", true, 11,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("NIP", "String", true, 12, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TEL", "String", true, 13, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("KOM", "String", true, 14, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("FAX", "String", true, 15, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("EMAIL", "String", true, 16,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("RABAT", "Float", true, 17, 0,
								"dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_FOR_PLT", "String", true,
								18, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TER_PLT", "Integer", true,
								19, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_CENNIK", "String", true,
								20, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_GC_KLT", "String", true,
								21, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_GL_KLT", "String", true,
								22, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_GR_KLT", "String", true,
								23, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_GRX_KLT", "String", true,
								24, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("LIM_KRED", "Float", true, 25,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("SALDO_PLT", "Float", true,
								26, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("LIM_PRZET", "Float", true,
								27, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("SALDO_PRZET", "Float", true,
								28, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("WSK_BLOK", "Integer", true,
								29, 0, "en", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_TYP_KLT", "String", true,
								30, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_KAT_KLT", "String", true,
								31, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_SIEC", "String", true, 32,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_POW", "String", true, 33,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ID_ST_KLT", "String", true,
								34, 0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ST_MOB", "Integer", true, 35,
								0, "dd-MM-yyyy", null, true, true, null, null));
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("WSK_DOMYSLNY", "Integer",
								true, 36, 0, "dd-MM-yyyy", null, true,
								true, null, null));
			} // end of configureFields

			public void fillOutputFlow(
					de.jlo.talendcomp.flatfileimport.Importer importer,
					row2Struct flow) throws Exception {
				flow.ID_MAG = importer.getStringAt(0, true);
				flow.ID_KLT = importer.getStringAt(1, true);
				flow.ID_PLT = importer.getStringAt(2, true);
				flow.ID_PRAC = importer.getStringAt(3, true);
				flow.NAZWA = importer.getStringAt(4, true);
				flow.SKROT = importer.getStringAt(5, true);
				flow.ID_W = importer.getStringAt(6, true);
				flow.ID_P = importer.getStringAt(7, true);
				flow.MIASTO = importer.getStringAt(8, true);
				flow.KOD = importer.getStringAt(9, true);
				flow.ULICA = importer.getStringAt(10, true);
				flow.NR_LOK = importer.getStringAt(11, true);
				flow.NIP = importer.getStringAt(12, true);
				flow.TEL = importer.getStringAt(13, true);
				flow.KOM = importer.getStringAt(14, true);
				flow.FAX = importer.getStringAt(15, true);
				flow.EMAIL = importer.getStringAt(16, true);
				flow.RABAT = importer.getFloatAt(17, true);
				flow.ID_FOR_PLT = importer.getStringAt(18, true);
				flow.TER_PLT = importer.getIntegerAt(19, true);
				flow.ID_CENNIK = importer.getStringAt(20, true);
				flow.ID_GC_KLT = importer.getStringAt(21, true);
				flow.ID_GL_KLT = importer.getStringAt(22, true);
				flow.ID_GR_KLT = importer.getStringAt(23, true);
				flow.ID_GRX_KLT = importer.getStringAt(24, true);
				flow.LIM_KRED = importer.getFloatAt(25, true);
				flow.SALDO_PLT = importer.getFloatAt(26, true);
				flow.LIM_PRZET = importer.getFloatAt(27, true);
				flow.SALDO_PRZET = importer.getFloatAt(28, true);
				flow.WSK_BLOK = importer.getIntegerAt(29, true);
				flow.ID_TYP_KLT = importer.getStringAt(30, true);
				flow.ID_KAT_KLT = importer.getStringAt(31, true);
				flow.ID_SIEC = importer.getStringAt(32, true);
				flow.ID_POW = importer.getStringAt(33, true);
				flow.ID_ST_KLT = importer.getStringAt(34, true);
				flow.ST_MOB = importer.getIntegerAt(35, true);
				flow.WSK_DOMYSLNY = importer.getIntegerAt(36, true);
			} // end of fillOutputFlow

		}
		final ImportHelper_tFileInputTextFlat_2 helper_tFileInputTextFlat_2 = new ImportHelper_tFileInputTextFlat_2();
		helper_tFileInputTextFlat_2
				.configureFields(tFileInputTextFlat_2);
		globalMap
				.put("tFileInputTextFlat_2_HEADER_CONFIG_ERROR", false);
		try {
			tFileInputTextFlat_2.initialize();
			tFileInputTextFlat_2.skipTopRows();
			tFileInputTextFlat_2.skipHeaderRow();
			try {
				tFileInputTextFlat_2
						.reconfigureFieldDescriptionByHeaderLine();
			} catch (Exception e) {
				globalMap.put(
						"tFileInputTextFlat_2_HEADER_CONFIG_ERROR",
						true);
				globalMap.put("tFileInputTextFlat_2_ERROR_MESSAGE",
						e.getMessage());
				throw e;
			}
		} catch (Exception e) {
			tFileInputTextFlat_2.close(); // close file handle in case
											// of errors
			globalMap.put("tFileInputTextFlat_2_ERROR_MESSAGE",
					e.getMessage());
			throw e;
		}
		int countLinesDelivered_tFileInputTextFlat_2 = 0;
		int lineNumber_tFileInputTextFlat_2 = -1;
		int countRejects_tFileInputTextFlat_2 = 0;
		try {
			while (true) { // main loop will be closed in end section
				// retrieve next data set
				lineNumber_tFileInputTextFlat_2++;
				row2 = new row2Struct();
				try {
					if (tFileInputTextFlat_2.nextDataRow() == false) {
						break;
					}
				} catch (Exception e) {
					String message = "nextDataRow failed in line "
							+ countLinesDelivered_tFileInputTextFlat_2
							+ ":" + e.getMessage();
					globalMap.put("tFileInputTextFlat_2_ERROR_MESSAGE",
							message);
					row2 = null;
					tFileInputTextFlat_2.close(); // close file handle
													// in case of errors
					throw new Exception(message, e);
				}
				if (row2 != null) {
					// retrieve values
					try {
						helper_tFileInputTextFlat_2.fillOutputFlow(
								tFileInputTextFlat_2, row2);
					} catch (Exception e) {
						String message = "fillOutputFlow failed in line "
								+ countLinesDelivered_tFileInputTextFlat_2
								+ ":" + e.getMessage();
						globalMap.put(
								"tFileInputTextFlat_2_ERROR_MESSAGE",
								message);
						tFileInputTextFlat_2.close(); // close file
														// handle in
														// case of
														// errors
						throw new Exception(message, e);
					}
				}
				countLinesDelivered_tFileInputTextFlat_2++;

				/**
				 * [tFileInputTextFlat_2 begin ] stop
				 */
				/**
				 * [tFileInputTextFlat_2 main ] start
				 */
				System.out.println("line " + countLinesDelivered_tFileInputTextFlat_2);

				/**
				 * [tFileInputTextFlat_2 main ] stop
				 */
			}
		} finally {
			
		}

	}
	
	public static void test2() throws Exception {
		Importer importer = new Importer();
		importer.setImportFile("/home/jlolling/test/text/bigdecimal_de.csv");
		importer.setFileCharset("UTF-8");
		importer.addFieldDescription(FieldDescription.createDelimited("value", "BigDecimal", false, 0, 0, "de", null, false, false, null, null));
		importer.addFieldDescription(FieldDescription.createDelimited("value", "Double", false, 1, 0, "de", null, false, false, null, null));
		importer.setDelimiter("|");
		importer.initialize();
		int line = 0;
		while (true) {
			try {
				if (importer.nextDataRow() == false) {
					break;
				}
			} catch (Exception e1) {
				System.err.println("line " + line + ": " + e1.getMessage());
				e1.printStackTrace();
				break;
			}
			System.out.println(importer.getBigDecimalAt(0, false));
			System.out.println(importer.getDoubleAt(1, false));
		}
		importer.close();
	}
	
	public static void testBOM() {
		Importer importer = new Importer();
		importer.setImportFile("/var/testdata/text/bom_test.txt");
		importer.setFileCharset("UTF-8");
		importer.addFieldDescription(FieldDescription.createDelimited("content", "String", false, 0, 0, null, null, false, false, null, null));
		importer.setDelimiter("|");
		try {
			importer.initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		int line = 0;
		while (true) {
			try {
				if (importer.nextDataRow() == false) {
					break;
				}
			} catch (Exception e1) {
				System.err.println("line " + line + ": " + e1.getMessage());
				e1.printStackTrace();
				break;
			}
			System.out.println(importer.getStringAt(0, false));
		}
		importer.close();
	}
	
}
