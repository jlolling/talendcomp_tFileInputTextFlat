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

import de.cimt.talendcomp.test.TalendFakeJob;

public class TestMultiLineFieldSupport extends TalendFakeJob {

	public static void main(String[] args) {
		TestMultiLineFieldSupport test = new TestMultiLineFieldSupport();
		try {
			test.testMulitiLineSupport();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class row1Struct {
		
		final static byte[] commonByteArrayLock_COMPDEV_test_problem_linebreak = new byte[0];
		static byte[] commonByteArray_COMPDEV_test_problem_linebreak = new byte[0];

		public String ColumnA;

		public String getColumnA() {
			return this.ColumnA;
		}

		public String ColumnB;

		public String getColumnB() {
			return this.ColumnB;
		}

		public String ColumnD;

		public String getColumnD() {
			return this.ColumnD;
		}

		public String ColumnE;

		public String getColumnE() {
			return this.ColumnE;
		}
		
		public String ColumnF;

		public String getColumnF() {
			return this.ColumnF;
		}

		@Override
		public String toString() {
			return ColumnA + "|" + ColumnB + "|" + ColumnD + "|" + ColumnE + "|" + ColumnF;
		}
	}	
	
	public void testMulitiLineSupport() throws Exception {
		/**
		 * [tFileInputTextFlat_1 begin ] start
		 */

		row1Struct row1 = new row1Struct();

		ok_Hash.put("tFileInputTextFlat_1", false);
		start_Hash.put("tFileInputTextFlat_1",
				System.currentTimeMillis());

		currentComponent = "tFileInputTextFlat_1";

		int tos_count_tFileInputTextFlat_1 = 0;

		// row1 ,
		de.cimt.talendcomp.flatfileimport.Importer tFileInputTextFlat_1 = new de.cimt.talendcomp.flatfileimport.Importer();
		tFileInputTextFlat_1.setDebug(true);
		tFileInputTextFlat_1
				.setImportFile("/Volumes/Data/Talend/testdata/text/flat/output.csv");
		globalMap.put("tFileInputTextFlat_1_FILENAME",
				tFileInputTextFlat_1.getImportFile());
		tFileInputTextFlat_1.skipBOM(false);
		tFileInputTextFlat_1.setSkipEmptyLines(false);
		tFileInputTextFlat_1.setHasHeaderRow(true);
		tFileInputTextFlat_1.setIgnoreNotNullConstraints(true);
		tFileInputTextFlat_1.setRowsToSkip(0);
		tFileInputTextFlat_1.setDelimiter(",");
		tFileInputTextFlat_1.setFileCharset("UTF-8");
		tFileInputTextFlat_1.setIgnoreLinebreakInEnclosures(!false);
		tFileInputTextFlat_1.setEnclosure("\"");
		tFileInputTextFlat_1.allowEnclosureInFieldContent(false);
		// helper to configure fields and get values
		final class ImportHelper_tFileInputTextFlat_1 {

			public void configureFields(
					de.cimt.talendcomp.flatfileimport.Importer importer) {

				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("Column A", // columnName
								"String", // type
								true, // nullable
								0, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("Column B", // columnName
								"String", // type
								true, // nullable
								1, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("Column D", // columnName
								"String", // type
								true, // nullable
								2, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("Column E", // columnName
								"String", // type
								true, // nullable
								3, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("Column F", // columnName
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
					de.cimt.talendcomp.flatfileimport.Importer importer,
					row1Struct flow) throws Exception {
				flow.ColumnA = importer.getStringAt(0, true);
				flow.ColumnB = importer.getStringAt(1, true);
				flow.ColumnD = importer.getStringAt(2, true);
				flow.ColumnE = importer.getStringAt(3, true);
				flow.ColumnF = importer.getStringAt(4, true);
			} // end of fillOutputFlow

		}
		final ImportHelper_tFileInputTextFlat_1 helper_tFileInputTextFlat_1 = new ImportHelper_tFileInputTextFlat_1();
		try {
			helper_tFileInputTextFlat_1
					.configureFields(tFileInputTextFlat_1);
			globalMap.put("tFileInputTextFlat_1_HEADER_CONFIG_ERROR",
					false);
		} catch (Exception e) {
			globalMap.put("tFileInputTextFlat_1_ERROR_MESSAGE",
					e.getMessage());
			throw e;
		}
		try {
			tFileInputTextFlat_1.initialize();
			tFileInputTextFlat_1.skipTopRows();
			tFileInputTextFlat_1.skipHeaderRow();
			try {
				// reconfigure fields, will fail if no header line is
				// set
				tFileInputTextFlat_1.setFindHeaderPosByRegex(false);
				tFileInputTextFlat_1
						.reconfigureFieldDescriptionByHeaderLine();
			} catch (Exception e) {
				globalMap.put(
						"tFileInputTextFlat_1_HEADER_CONFIG_ERROR",
						true);
				throw e;
			}
		} catch (Exception e) {
			tFileInputTextFlat_1.close(); // close file handle in case
											// of errors
			globalMap.put("tFileInputTextFlat_1_ERROR_MESSAGE",
					e.getMessage());
			throw e;
		}
		int countLinesDelivered_tFileInputTextFlat_1 = 0;
		int lineNumber_tFileInputTextFlat_1 = -1;
		int countRejects_tFileInputTextFlat_1 = 0;
		System.out.println("###########################################");
		try {
			while (true) { // main loop will be closed in end section
				// retrieve next data set
				lineNumber_tFileInputTextFlat_1++;
				row1 = new row1Struct();
				try {
					if (tFileInputTextFlat_1.nextDataRow() == false) {
						break;
					}
				} catch (Exception e) {
					String message = "nextDataRow failed in line "
							+ countLinesDelivered_tFileInputTextFlat_1
							+ ":" + e.getMessage();
					globalMap.put("tFileInputTextFlat_1_ERROR_MESSAGE",
							message);
					row1 = null;
					tFileInputTextFlat_1.close(); // close file handle
													// in case of errors
					throw new Exception(message, e);
				}
				if (row1 != null) {
					// retrieve values
					try {
						helper_tFileInputTextFlat_1.fillOutputFlow(
								tFileInputTextFlat_1, row1);
					} catch (Exception e) {
						String message = "fillOutputFlow failed in line "
								+ countLinesDelivered_tFileInputTextFlat_1
								+ ":" + e.getMessage();
						globalMap.put(
								"tFileInputTextFlat_1_ERROR_MESSAGE",
								message);
						tFileInputTextFlat_1.close(); // close file
														// handle in
														// case of
														// errors
						throw new Exception(message, e);
					}
				}
				countLinesDelivered_tFileInputTextFlat_1++;

				/**
				 * [tFileInputTextFlat_1 begin ] stop
				 */
				/**
				 * [tFileInputTextFlat_1 main ] start
				 */

				currentComponent = "tFileInputTextFlat_1";

				tos_count_tFileInputTextFlat_1++;

				/**
				 * [tFileInputTextFlat_1 main ] stop
				 */
				System.out.println(countLinesDelivered_tFileInputTextFlat_1 + ": " +row1);
				System.out.println("----------------------------------------------------");
			} // end of while of tFileInputTextFlat_1
		} finally { // finally from try of tFileInputTextFlat_1
			tFileInputTextFlat_1.close(); // close file handle
		}
		globalMap.put("tFileInputTextFlat_1_NB_LINE",
				countLinesDelivered_tFileInputTextFlat_1);
		globalMap.put("tFileInputTextFlat_1_NB_REJECTED",
				countRejects_tFileInputTextFlat_1);

		ok_Hash.put("tFileInputTextFlat_1", true);
		end_Hash.put("tFileInputTextFlat_1", System.currentTimeMillis());
		
	}

}
