package com.euyemekhane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen extends Activity {

	private Document doc;
	private Elements baslik;
	private String[] baslikStr;
	private String yemek = null;
	private String yemekTarihi = null;
	private MenuDAL dalMenu = new MenuDAL(this);
	private Menu entMenu;
	private static final int SPLASH_DISPLAY_TIME = 2000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try {
					doc = Jsoup.connect("http://sksdb.ege.edu.tr/genel/oele-yemek-menuesue").get();
					baslik = doc.select("td h1"); //sayfanin ustundeki basligi cekiyor
					baslikStr = baslik.text().toLowerCase().split("\\s+");

					if (dalMenu.SonOgleYemekGetir() != null && dalMenu.SonOgleYemekGetir().getAy().equals(baslikStr[0])) { //baslikta yazan ay ile veritabanindaki ay ayn�ysa guncelleme yapmiyor

						//Toast.makeText(getApplicationContext(), "��le yeme�i g�ncel", Toast.LENGTH_LONG).show();

					} else {

						//Toast.makeText(getApplicationContext(), "��le yeme�i g�ncelleniyor", Toast.LENGTH_LONG).show();
						Elements yemekler = doc.select("td p span"); //tarihi ve o gunku yemegi cekiyor

						for (Element x : yemekler) {
							//System.out.println("\n" + x.text());
							entMenu = new Menu();
							if (x.text().matches("\\d.*\\d.*\\d.*")) { //bos gelen veriler eleniyor
								yemekTarihi = x.text().trim();
							}
							if (x.text().contains("cal")) {
								yemek = x.text().trim();
							}
							if (yemek != null && yemekTarihi != null) { //yemek ve tarihin ikisi de null degilse veritabanina ekleniyor
								entMenu.setAy(baslikStr[0]);
								entMenu.setTur("ogle");
								entMenu.setTarih(yemekTarihi);
								entMenu.setMenu(yemek);
								dalMenu.MenuKaydet(entMenu);
								yemek = null;
								yemekTarihi = null;
							}
						}

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					doc = null;
					baslik = null;
					baslikStr = null;
					doc = Jsoup.connect("http://sksdb.ege.edu.tr/genel/akam-yemek-menuesue").get();
					baslik = doc.select("td h1"); //sayfanin ustundeki basligi cekiyor
					baslikStr = baslik.text().toLowerCase().split("\\s+");

					if (dalMenu.SonAksamYemekGetir() != null && dalMenu.SonAksamYemekGetir().getAy().equals(baslikStr[0])) { //baslikta yazan ay ile veritabanindaki ay ayn�ysa guncelleme yapmiyor

						Toast.makeText(getApplicationContext(), "Yemek listesi g�ncel", Toast.LENGTH_LONG).show();

					} else {

						Toast.makeText(getApplicationContext(), "Yemek listesi g�ncelleniyor", Toast.LENGTH_LONG).show();
						Elements yemekler = doc.select("td p span"); //tarihi ve o gunku yemegi cekiyor

						for (Element x : yemekler) {
							//System.out.println("\n" + x.text());
							entMenu = new Menu();
							if (x.text().matches("\\d.*\\d.*\\d.*")) { //bos gelen veriler eleniyor
								yemekTarihi = x.text().trim();
							}
							if (x.text().contains("cal")) {
								yemek = x.text().trim();
							}
							if (yemek != null && yemekTarihi != null) { //yemek ve tarihin ikisi de null degilse veritabanina ekleniyor
								entMenu.setAy(baslikStr[0]);
								entMenu.setTur("aksam");
								entMenu.setTarih(yemekTarihi);
								entMenu.setMenu(yemek);
								dalMenu.MenuKaydet(entMenu);
								yemek = null;
								yemekTarihi = null;
							}
						}

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);
				SplashScreen.this.finish();
				
			}
		}, SPLASH_DISPLAY_TIME);
		
	}

}
