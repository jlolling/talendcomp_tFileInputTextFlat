import de.cimt.talendcomp.test.TalendFakeJob;
import de.jlo.talendcomp.TolerantDateParser;

public class Test extends TalendFakeJob {
	
	public static void main(String[] args) {
		Test t = new Test();
		try {
			t.testDateParser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testDateParser() {
		TolerantDateParser p = new TolerantDateParser();
		p.printPatterns();
		String text = "04.10.2012_23:59:11";
		try {
			System.out.println(TolerantDateParser.parseDate(text));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test() throws Exception {
		
		row1Struct row1 = null;
		// row1 ,
		de.jlo.talendcomp.flatfileimport.Importer tFileInputTextFlat_2 = new de.jlo.talendcomp.flatfileimport.Importer();
		tFileInputTextFlat_2.setDebug(true);
		tFileInputTextFlat_2
				.setImportFile("/private/var/testdata/text/different_schemas/regex/Input_File.csv");
		tFileInputTextFlat_2.setFileCharset("UTF-8");
		tFileInputTextFlat_2.skipBOM(false);
		tFileInputTextFlat_2.setSkipEmptyLines(false);
		tFileInputTextFlat_2.setHasHeaderRow(true);
		tFileInputTextFlat_2.setIgnoreNotNullConstraints(false);
		tFileInputTextFlat_2.setDelimiter(",");
		tFileInputTextFlat_2.setRowsToSkip(0);
		tFileInputTextFlat_2.setIgnoreLinebreakInEnclosures(!false);
		//tFileInputTextFlat_2.setEnclosure("\"");
		tFileInputTextFlat_2.allowEnclosureInFieldContent(false);
		// helper to configure fields and get values
		final class ImportHelper_tFileInputTextFlat_2 {

			public void configureFields(
					de.jlo.talendcomp.flatfileimport.Importer importer) {
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("*contact.*number*", // columnName
								"String", // type
								true, // nullable
								0, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								"99", // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("*(external).*(reference)*", // columnName
								"Double", // type
								true, // nullable
								1, // pos
								0, // length
								"en", // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited(
								"pp_product|product.purchased", // columnName
								"String", // type
								true, // nullable
								45, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								"Fundraiser")); // alternative
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited(
								"pp.start.date|date.DDI.signed", // columnName
								"Date", // type
								true, // nullable
								8, // pos
								0, // length
								"dd.MM.yyyy", // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited(
								"payment.plan.number|order.number", // columnName
								"Long", // type
								true, // nullable
								71, // pos
								0, // length
								"en", // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.jlo.talendcomp.flatfileimport.FieldDescription
						.createDelimited("Fundraiser", // columnName
								"String", // type
								true, // nullable
								4, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
			} // end of configureFields

			public void fillOutputFlow(
					de.jlo.talendcomp.flatfileimport.Importer importer,
					row1Struct flow) throws Exception {
				flow.ContactNumber = importer.getStringAt(0, true);
				flow.ExternalReference = importer.getDoubleAt(1, true);
				flow.PaymentPlanProduct = importer.getStringAt(2, true);
				flow.PaymentPlanStartDate = importer.getDateAt(3, true);
				flow.PaymentPlanNumber = importer.getLongAt(4, true);
				flow.Fundraiser = importer.getStringAt(5, true);
			} // end of fillOutputFlow

		}
		final ImportHelper_tFileInputTextFlat_2 helper_tFileInputTextFlat_2 = new ImportHelper_tFileInputTextFlat_2();
		try {
			helper_tFileInputTextFlat_2
					.configureFields(tFileInputTextFlat_2);
		} catch (Exception e) {
			throw e;
		}
		try {
			tFileInputTextFlat_2.initialize();
			tFileInputTextFlat_2.skipTopRows();
			tFileInputTextFlat_2.skipHeaderRow();
			try {
				// reconfigure fields, will fail if no header line is
				// set
				tFileInputTextFlat_2.setFindHeaderPosByRegex(true);
				tFileInputTextFlat_2
						.reconfigureFieldDescriptionByHeaderLine();
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			tFileInputTextFlat_2.close(); // close file handle in case
											// of errors
			throw e;
		}
		int countLinesDelivered_tFileInputTextFlat_2 = 0;
		int lineNumber_tFileInputTextFlat_2 = -1;
		int countRejects_tFileInputTextFlat_2 = 0;
			while (true) { // main loop will be closed in end section
				// retrieve next data set
				lineNumber_tFileInputTextFlat_2++;
				row1 = new row1Struct();
				try {
					if (tFileInputTextFlat_2.nextDataRow() == false) {
						break;
					}
				} catch (Exception e) {
					String message = "nextDataRow failed in line "
							+ countLinesDelivered_tFileInputTextFlat_2
							+ ":" + e.getMessage();
					row1 = null;
					tFileInputTextFlat_2.close(); // close file handle
													// in case of errors
					throw new Exception(message, e);
				}
				if (row1 != null) {
					// retrieve values
					try {
						helper_tFileInputTextFlat_2.fillOutputFlow(
								tFileInputTextFlat_2, row1);
					} catch (Exception e) {
						String message = "fillOutputFlow failed in line "
								+ countLinesDelivered_tFileInputTextFlat_2
								+ ":" + e.getMessage();
						tFileInputTextFlat_2.close(); // close file
														// handle in
														// case of
														// errors
						throw new Exception(message, e);
					}
				}
				countLinesDelivered_tFileInputTextFlat_2++;
				System.out.println(countLinesDelivered_tFileInputTextFlat_2 + " # " + row1);
			}
		
	}
	
	
	public static class row1Struct {
		
		public String ContactNumber;
		
		public String getContactNumber() {
			return this.ContactNumber;
		}
		
		public Double ExternalReference;
		
		public Double getExternalReference() {
			return this.ExternalReference;
		}
		
		public String PaymentPlanProduct;
		
		public String getPaymentPlanProduct() {
			return this.PaymentPlanProduct;
		}
		
		public java.util.Date PaymentPlanStartDate;
		
		public java.util.Date getPaymentPlanStartDate() {
			return this.PaymentPlanStartDate;
		}
		
		public Long PaymentPlanNumber;
		
		public Long getPaymentPlanNumber() {
			return this.PaymentPlanNumber;
		}
		
		public String Fundraiser;
		
		public String getFundraiser() {
			return this.Fundraiser;
		}
		
		public String toString() {
			return String.valueOf(ExternalReference);
		}
	}

}
