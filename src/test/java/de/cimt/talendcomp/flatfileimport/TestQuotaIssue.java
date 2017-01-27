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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.cimt.talendcomp.test.TalendFakeJob;

public class TestQuotaIssue extends TalendFakeJob {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestQuotaIssue test = new TestQuotaIssue();
		try {
			test.test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test() throws Exception {
		row1Struct row1 = new row1Struct();
		
		currentComponent = "tFileInputTextFlat_1";

		int tos_count_tFileInputTextFlat_1 = 0;

		// row1 ,
		de.cimt.talendcomp.flatfileimport.Importer tFileInputTextFlat_1 = new de.cimt.talendcomp.flatfileimport.Importer();
		tFileInputTextFlat_1.setDebug(true);
		tFileInputTextFlat_1
				.setImportFile("/Volumes/Data/Talend/testdata/text/single_quota_issue/TRANS_DAILY_20160125.txt");
		globalMap.put("tFileInputTextFlat_1_FILENAME",
				tFileInputTextFlat_1.getImportFile());
		tFileInputTextFlat_1.skipBOM(false);
		tFileInputTextFlat_1.setSkipEmptyLines(false);
		tFileInputTextFlat_1.setHasHeaderRow(true);
		tFileInputTextFlat_1.setIgnoreNotNullConstraints(false);
		tFileInputTextFlat_1.setRowsToSkip(0);
		tFileInputTextFlat_1.setDelimiter("\t");
		tFileInputTextFlat_1.setFileCharset("UTF-8");
		tFileInputTextFlat_1.setIgnoreLinebreakInEnclosures(false);
		tFileInputTextFlat_1.setEnclosure("\"");
		tFileInputTextFlat_1.allowEnclosureInFieldContent(false);
		// helper to configure fields and get values
		final class ImportHelper_tFileInputTextFlat_1 {

			public void configureFields(
					de.cimt.talendcomp.flatfileimport.Importer importer) {

				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("MONTH", // columnName
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
						.createDelimited("CHIP", // columnName
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
						.createDelimited("SKU", // columnName
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
						.createDelimited("CARDTECH", // columnName
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
						.createDelimited("TRANSRC", // columnName
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
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("LOGICMOD", // columnName
								"String", // type
								true, // nullable
								5, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("INTRCHG_FEE", // columnName
								"String", // type
								true, // nullable
								6, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("CAT_CODE", // columnName
								"String", // type
								true, // nullable
								7, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("FULLSRC", // columnName
								"String", // type
								true, // nullable
								8, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANINS", // columnName
								"String", // type
								true, // nullable
								9, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANFXF", // columnName
								"String", // type
								true, // nullable
								10, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANITEM", // columnName
								"String", // type
								true, // nullable
								11, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("Column0", // columnName
								"String", // type
								true, // nullable
								12, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("ACCTNBR", // columnName
								"String", // type
								true, // nullable
								13, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANDTE", // columnName
								"String", // type
								true, // nullable
								14, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANCDE", // columnName
								"String", // type
								true, // nullable
								15, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANAMT", // columnName
								"String", // type
								true, // nullable
								16, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("POSTDTE", // columnName
								"String", // type
								true, // nullable
								17, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("DBANAME", // columnName
								"String", // type
								true, // nullable
								18, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("DBALOC", // columnName
								"String", // type
								true, // nullable
								19, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("DBACNTRY", // columnName
								"String", // type
								true, // nullable
								20, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANMER", // columnName
								"String", // type
								true, // nullable
								21, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANMCC", // columnName
								"String", // type
								true, // nullable
								22, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("PLANNBR", // columnName
								"String", // type
								true, // nullable
								23, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANTYP", // columnName
								"String", // type
								true, // nullable
								24, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("TRANGRP", // columnName
								"String", // type
								true, // nullable
								25, // pos
								0, // length
								null, // pattern
								null, // regex
								false, // trim
								false, // ignoreMissing
								null, // defaultValue
								null)); // alternative
				importer.addFieldDescription(de.cimt.talendcomp.flatfileimport.FieldDescription
						.createDelimited("LOGO", // columnName
								"String", // type
								true, // nullable
								26, // pos
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
				flow.MONTH = importer.getStringAt(0, true);
				flow.CHIP = importer.getStringAt(1, true);
				flow.SKU = importer.getStringAt(2, true);
				flow.CARDTECH = importer.getStringAt(3, true);
				flow.TRANSRC = importer.getStringAt(4, true);
				flow.LOGICMOD = importer.getStringAt(5, true);
				flow.INTRCHG_FEE = importer.getStringAt(6, true);
				flow.CAT_CODE = importer.getStringAt(7, true);
				flow.FULLSRC = importer.getStringAt(8, true);
				flow.TRANINS = importer.getStringAt(9, true);
				flow.TRANFXF = importer.getStringAt(10, true);
				flow.TRANITEM = importer.getStringAt(11, true);
				flow.Column0 = importer.getStringAt(12, true);
				flow.ACCTNBR = importer.getStringAt(13, true);
				flow.TRANDTE = importer.getStringAt(14, true);
				flow.TRANCDE = importer.getStringAt(15, true);
				flow.TRANAMT = importer.getStringAt(16, true);
				flow.POSTDTE = importer.getStringAt(17, true);
				flow.DBANAME = importer.getStringAt(18, true);
				flow.DBALOC = importer.getStringAt(19, true);
				flow.DBACNTRY = importer.getStringAt(20, true);
				flow.TRANMER = importer.getStringAt(21, true);
				flow.TRANMCC = importer.getStringAt(22, true);
				flow.PLANNBR = importer.getStringAt(23, true);
				flow.TRANTYP = importer.getStringAt(24, true);
				flow.TRANGRP = importer.getStringAt(25, true);
				flow.LOGO = importer.getStringAt(26, true);
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
				System.out.println("POSTDTE=" + row1.POSTDTE);
				System.out.println("DBANAME=" + row1.DBANAME);
				System.out.println("DBALOC=" + row1.DBALOC);
				System.out.println("--------------------------------");
				
				
				/**
				 * [tFileInputTextFlat_1 begin ] stop
				 */
				/**
				 * [tFileInputTextFlat_1 main ] start
				 */

				currentComponent = "tFileInputTextFlat_1";

				tos_count_tFileInputTextFlat_1++;
				
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	public static class row1Struct implements routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_COMPDEV_test_tFileInputTextFlat_issue_tab = new byte[0];
		static byte[] commonByteArray_COMPDEV_test_tFileInputTextFlat_issue_tab = new byte[0];

		public String MONTH;

		public String getMONTH() {
			return this.MONTH;
		}

		public String CHIP;

		public String getCHIP() {
			return this.CHIP;
		}

		public String SKU;

		public String getSKU() {
			return this.SKU;
		}

		public String CARDTECH;

		public String getCARDTECH() {
			return this.CARDTECH;
		}

		public String TRANSRC;

		public String getTRANSRC() {
			return this.TRANSRC;
		}

		public String LOGICMOD;

		public String getLOGICMOD() {
			return this.LOGICMOD;
		}

		public String INTRCHG_FEE;

		public String getINTRCHG_FEE() {
			return this.INTRCHG_FEE;
		}

		public String CAT_CODE;

		public String getCAT_CODE() {
			return this.CAT_CODE;
		}

		public String FULLSRC;

		public String getFULLSRC() {
			return this.FULLSRC;
		}

		public String TRANINS;

		public String getTRANINS() {
			return this.TRANINS;
		}

		public String TRANFXF;

		public String getTRANFXF() {
			return this.TRANFXF;
		}

		public String TRANITEM;

		public String getTRANITEM() {
			return this.TRANITEM;
		}

		public String Column0;

		public String getColumn0() {
			return this.Column0;
		}

		public String ACCTNBR;

		public String getACCTNBR() {
			return this.ACCTNBR;
		}

		public String TRANDTE;

		public String getTRANDTE() {
			return this.TRANDTE;
		}

		public String TRANCDE;

		public String getTRANCDE() {
			return this.TRANCDE;
		}

		public String TRANAMT;

		public String getTRANAMT() {
			return this.TRANAMT;
		}

		public String POSTDTE;

		public String getPOSTDTE() {
			return this.POSTDTE;
		}

		public String DBANAME;

		public String getDBANAME() {
			return this.DBANAME;
		}

		public String DBALOC;

		public String getDBALOC() {
			return this.DBALOC;
		}

		public String DBACNTRY;

		public String getDBACNTRY() {
			return this.DBACNTRY;
		}

		public String TRANMER;

		public String getTRANMER() {
			return this.TRANMER;
		}

		public String TRANMCC;

		public String getTRANMCC() {
			return this.TRANMCC;
		}

		public String PLANNBR;

		public String getPLANNBR() {
			return this.PLANNBR;
		}

		public String TRANTYP;

		public String getTRANTYP() {
			return this.TRANTYP;
		}

		public String TRANGRP;

		public String getTRANGRP() {
			return this.TRANGRP;
		}

		public String LOGO;

		public String getLOGO() {
			return this.LOGO;
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_COMPDEV_test_tFileInputTextFlat_issue_tab.length) {
					if (length < 1024 && commonByteArray_COMPDEV_test_tFileInputTextFlat_issue_tab.length == 0) {
						commonByteArray_COMPDEV_test_tFileInputTextFlat_issue_tab = new byte[1024];
					} else {
						commonByteArray_COMPDEV_test_tFileInputTextFlat_issue_tab = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_COMPDEV_test_tFileInputTextFlat_issue_tab, 0, length);
				strReturn = new String(commonByteArray_COMPDEV_test_tFileInputTextFlat_issue_tab, 0, length,
						utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		@Override
		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_COMPDEV_test_tFileInputTextFlat_issue_tab) {

				try {

					int length = 0;

					this.MONTH = readString(dis);

					this.CHIP = readString(dis);

					this.SKU = readString(dis);

					this.CARDTECH = readString(dis);

					this.TRANSRC = readString(dis);

					this.LOGICMOD = readString(dis);

					this.INTRCHG_FEE = readString(dis);

					this.CAT_CODE = readString(dis);

					this.FULLSRC = readString(dis);

					this.TRANINS = readString(dis);

					this.TRANFXF = readString(dis);

					this.TRANITEM = readString(dis);

					this.Column0 = readString(dis);

					this.ACCTNBR = readString(dis);

					this.TRANDTE = readString(dis);

					this.TRANCDE = readString(dis);

					this.TRANAMT = readString(dis);

					this.POSTDTE = readString(dis);

					this.DBANAME = readString(dis);

					this.DBALOC = readString(dis);

					this.DBACNTRY = readString(dis);

					this.TRANMER = readString(dis);

					this.TRANMCC = readString(dis);

					this.PLANNBR = readString(dis);

					this.TRANTYP = readString(dis);

					this.TRANGRP = readString(dis);

					this.LOGO = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		@Override
		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.MONTH, dos);

				// String

				writeString(this.CHIP, dos);

				// String

				writeString(this.SKU, dos);

				// String

				writeString(this.CARDTECH, dos);

				// String

				writeString(this.TRANSRC, dos);

				// String

				writeString(this.LOGICMOD, dos);

				// String

				writeString(this.INTRCHG_FEE, dos);

				// String

				writeString(this.CAT_CODE, dos);

				// String

				writeString(this.FULLSRC, dos);

				// String

				writeString(this.TRANINS, dos);

				// String

				writeString(this.TRANFXF, dos);

				// String

				writeString(this.TRANITEM, dos);

				// String

				writeString(this.Column0, dos);

				// String

				writeString(this.ACCTNBR, dos);

				// String

				writeString(this.TRANDTE, dos);

				// String

				writeString(this.TRANCDE, dos);

				// String

				writeString(this.TRANAMT, dos);

				// String

				writeString(this.POSTDTE, dos);

				// String

				writeString(this.DBANAME, dos);

				// String

				writeString(this.DBALOC, dos);

				// String

				writeString(this.DBACNTRY, dos);

				// String

				writeString(this.TRANMER, dos);

				// String

				writeString(this.TRANMCC, dos);

				// String

				writeString(this.PLANNBR, dos);

				// String

				writeString(this.TRANTYP, dos);

				// String

				writeString(this.TRANGRP, dos);

				// String

				writeString(this.LOGO, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		@Override
		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("MONTH=" + MONTH);
			sb.append(",CHIP=" + CHIP);
			sb.append(",SKU=" + SKU);
			sb.append(",CARDTECH=" + CARDTECH);
			sb.append(",TRANSRC=" + TRANSRC);
			sb.append(",LOGICMOD=" + LOGICMOD);
			sb.append(",INTRCHG_FEE=" + INTRCHG_FEE);
			sb.append(",CAT_CODE=" + CAT_CODE);
			sb.append(",FULLSRC=" + FULLSRC);
			sb.append(",TRANINS=" + TRANINS);
			sb.append(",TRANFXF=" + TRANFXF);
			sb.append(",TRANITEM=" + TRANITEM);
			sb.append(",Column0=" + Column0);
			sb.append(",ACCTNBR=" + ACCTNBR);
			sb.append(",TRANDTE=" + TRANDTE);
			sb.append(",TRANCDE=" + TRANCDE);
			sb.append(",TRANAMT=" + TRANAMT);
			sb.append(",POSTDTE=" + POSTDTE);
			sb.append(",DBANAME=" + DBANAME);
			sb.append(",DBALOC=" + DBALOC);
			sb.append(",DBACNTRY=" + DBACNTRY);
			sb.append(",TRANMER=" + TRANMER);
			sb.append(",TRANMCC=" + TRANMCC);
			sb.append(",PLANNBR=" + PLANNBR);
			sb.append(",TRANTYP=" + TRANTYP);
			sb.append(",TRANGRP=" + TRANGRP);
			sb.append(",LOGO=" + LOGO);
			sb.append("]");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row1Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

}


}
