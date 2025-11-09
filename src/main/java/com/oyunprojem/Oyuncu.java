package com.oyunprojem;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Oyuncu extends ImageView {

    // Boyut
    private static final int OYUNCU_BOYUT = 64;

    // Fizik
    private static final double HAREKET_HIZI = 5;
    private static final double YERCEKIMI = 1;
    private static final double ZIPLAMA_GUCU = -20;
    private double hizY = 0;
    private boolean yerdeMi = false;

    // Animasyon Görselleri
    private Image idleFrame;
    private Image walkFrame1;
    private Image walkFrame2;

    // Animasyon Sayaçları
    private int animasyonGecikmesi = 0;
    private int animasyonKaresi = 0;
    private static final int ANIMASYON_HIZI = 5;

    // Yapılandırıcı (Constructor)
    public Oyuncu(int baslangicX, int baslangicY) {

        try {
            idleFrame = new Image(getClass().getResourceAsStream("/idle.png"));
            walkFrame1 = new Image(getClass().getResourceAsStream("/walk1.png"));
            walkFrame2 = new Image(getClass().getResourceAsStream("/walk2.png"));

            if (idleFrame == null || walkFrame1 == null || walkFrame2 == null) {
                throw new Exception("Animasyon görselleri bulunamadı.");
            }

            this.setImage(idleFrame);
        } catch (Exception e) {
            System.err.println("Hata: Animasyon görselleri 'resources' klasöründe bulunamadı!");
            System.err.println("Lütfen idle.png, walk1.png, ve walk2.png dosyalarının src/main/resources altında olduğundan emin olun.");
            e.printStackTrace();
        }

        this.setFitWidth(OYUNCU_BOYUT);
        this.setFitHeight(OYUNCU_BOYUT);
        this.setPreserveRatio(true);

        pozisyonuSifirla(baslangicX, baslangicY);
    }

    // Fizik ve Çarpışma Mantığı
    public void guncelle(List<Rectangle> platformlar, int dunyaGenislik, boolean sol, boolean sag) {

        hizY = hizY + YERCEKIMI;
        this.setLayoutY(this.getLayoutY() + hizY);

        this.yerdeMi = false;

        for (Rectangle platform : platformlar) {

            double oyuncuMinX = this.getBoundsInParent().getMinX();
            double oyuncuMaxX = this.getBoundsInParent().getMaxX();
            double oyuncuMinY = this.getLayoutY();
            double oyuncuMaxY = this.getLayoutY() + OYUNCU_BOYUT;

            double platformMinX = platform.getLayoutX();
            double platformMaxX = platform.getLayoutX() + platform.getWidth();
            double platformMinY = platform.getLayoutY();
            double platformMaxY = platform.getLayoutY() + platform.getHeight();

            boolean yataydaHizali = (oyuncuMaxX - 10) > platformMinX && (oyuncuMinX + 10) < platformMaxX;
            boolean dikeydeHizali = oyuncuMaxY > platformMinY && oyuncuMinY < platformMaxY;

            if (yataydaHizali && dikeydeHizali) {
                if (hizY >= 0 && oyuncuMaxY >= platformMinY && (oyuncuMinY < (platformMinY + 10))) {
                    this.setLayoutY(platformMinY - OYUNCU_BOYUT);
                    hizY = 0;
                    this.yerdeMi = true;
                }
                else if (hizY < 0 && oyuncuMinY <= platformMaxY && (oyuncuMaxY > platformMaxY)) {
                    hizY = 0;
                    this.setLayoutY(platformMaxY);
                }
            }
        }

        // Animasyon Mantığı
        boolean hareketEdiyor = (sol && !sag) || (sag && !sol);
        if (hareketEdiyor) {
            animasyonGecikmesi++;
            if (animasyonGecikmesi > ANIMASYON_HIZI) {
                animasyonGecikmesi = 0;
                animasyonKaresi = (animasyonKaresi + 1) % 2;
                if (animasyonKaresi == 0) {
                    this.setImage(walkFrame1);
                } else {
                    this.setImage(walkFrame2);
                }
            }
        } else {
            this.setImage(idleFrame);
            animasyonGecikmesi = 0;
            animasyonKaresi = 0;
        }
    }

    // Yatay Hareket
    public void hareketEt(boolean sol, boolean sag, int dunyaGenislik) {
        if (sol && !sag) {
            this.setLayoutX(this.getLayoutX() - HAREKET_HIZI);
            this.setScaleX(-1);
        }
        if (sag && !sol) {
            this.setLayoutX(this.getLayoutX() + HAREKET_HIZI);
            this.setScaleX(1);
        }
        if (this.getLayoutX() < 0) this.setLayoutX(0);
        if (this.getLayoutX() + OYUNCU_BOYUT > dunyaGenislik) {
            this.setLayoutX(dunyaGenislik - OYUNCU_BOYUT);
        }
    }

    // Zıplama
    public void zipla() {
        if (yerdeMi) {
            this.hizY = ZIPLAMA_GUCU;
            this.yerdeMi = false;
        }
    }

    // Pozisyon Sıfırlama
    public void pozisyonuSifirla(int x, int y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.hizY = 0;
    }

    // Durum Metodları
    public boolean isYerdeMi() {
        return yerdeMi;
    }
    public static int getOyuncuBoyut() {
        return OYUNCU_BOYUT;
    }
    public double getHizY() {
        return hizY;
    }
    public void dusmanUstuZipla() {
        this.hizY = ZIPLAMA_GUCU / 2;
    }
}