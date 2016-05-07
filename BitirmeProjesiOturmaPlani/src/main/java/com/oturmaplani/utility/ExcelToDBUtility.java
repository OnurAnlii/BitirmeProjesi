package com.oturmaplani.utility;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.oturmaplani.model.Ogrenci;
import com.oturmaplani.model.Sinif;

public class ExcelToDBUtility {
	private static FileInputStream input = null;
	private static OPCPackage fs = null;
	private static Workbook wb = null;
	private static Sheet sheet = null;
	private static Row row = null;
	private static int[] MF104 = null, MF105 = null, MF106 = null, MF107 = null;
	private static int[][] tumListe;

	public static boolean getExcelImportFile(String path, int satirSonu, String[] sinifAdi, int[] sinifMevcut) {
		ArrayList<Ogrenci> ogrenciListesi = new ArrayList<Ogrenci>();
		int sira = 1;
		try {
			if(sinifMevcut[0] != 0){
				MF104 = IntStream.rangeClosed(1, sinifMevcut[0]).toArray();
			}
			if(sinifMevcut[1] != 0){
				MF105 = IntStream.rangeClosed(1, sinifMevcut[1]).toArray();
			}
			if(sinifMevcut[2] != 0){
				MF106 = IntStream.rangeClosed(1, sinifMevcut[2]).toArray();
			}
			if(sinifMevcut[3] != 0){
				MF107 = IntStream.rangeClosed(1, sinifMevcut[3]).toArray();
			}
			tumListe = new int[][]{MF104, MF105, MF106, MF107};
			siniflariKaristir(sinifMevcut);
			input = new FileInputStream(path);
			fs = OPCPackage.open(input);
			wb = WorkbookFactory.create(fs);
			sheet = wb.getSheetAt(0);
			for(int i = 1, j = 1; i < satirSonu; i++,j++){
				Ogrenci ogrenci = new Ogrenci();
				row = (Row) sheet.getRow(i);
				ogrenci.setOgrenciAdi((row.getCell(1) == null) ? "0" : row.getCell(1).toString());
				ogrenci.setOgrenciSoyad((row.getCell(2) == null) ? "0" : row.getCell(2).toString());
				ogrenci.setOgrenciNo((row.getCell(0) == null) ? "0" : row.getCell(0).toString());
				ogrenciListesi.add(ogrenci);
				if(sinifMevcut[sira-1] == j){
					sira++;
					j=0;
				}
			}
			sira = 1;
			Collections.shuffle(ogrenciListesi);
			int i = 0;
			ArrayList<Ogrenci> geciciListe = new ArrayList<Ogrenci>();
			for (int[] temp : tumListe) {
				for(int j = 1 ; j<= temp.length ; i++, j++){
					ogrenciListesi.get(i).setSira(temp[j - 1]);
					ogrenciListesi.get(i).setSinifAdi(new Sinif(sinifAdi[sira -1]));
					geciciListe.add(ogrenciListesi.get(i));
				}
				DBtoExcelUtility.getExcelFile(geciciListe);
				geciciListe.clear();
				sira++;
			}
			input.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				if (input != null)
					input.close();
			} catch (Exception exx) {
				exx.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	private static void siniflariKaristir(int[] sinifMevcut) {
		if(MF104 != null)
			siraNumaralariniKaristir(MF104);
		if(MF105 != null)
			siraNumaralariniKaristir(MF105);
		if(MF106 != null)
			siraNumaralariniKaristir(MF106);
		if(MF107 != null)
			siraNumaralariniKaristir(MF107);
	}
	
	public static void siraNumaralariniKaristir(int[] ar) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
}