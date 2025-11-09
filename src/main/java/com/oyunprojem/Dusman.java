package com.oyunprojem;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Dusman extends ImageView {

    // Düşman Özellikleri
    private static final int DUSMAN_BOYUT = 50;
    private double hizX = 1.5;

    // Devriye (Patrol) Sınırları
    private double baslangicX;
    private double hareketMenzili;

    public Dusman(double x, double y, double menzil) {
        // 1. Görseli yükle
        try {
            Image dusmanGorseli = new Image(getClass().getResourceAsStream("/dusman.png"));
            this.setImage(dusmanGorseli);
        } catch (Exception e) {
            System.err.println("Hata: /dusman.png dosyası 'resources' klasöründe bulunamadı!");
        }

        // 2. Boyutları ayarla
        this.setFitWidth(DUSMAN_BOYUT);
        this.setFitHeight(DUSMAN_BOYUT);
        this.setPreserveRatio(true);

        // 3. Konum ve devriye ayarları
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.baslangicX = x;
        this.hareketMenzili = menzil;

        this.setScaleX(-1);
    }

    // Düşmanın kendi güncelleme mantığı
    public void guncelle() {
        this.setLayoutX(this.getLayoutX() + hizX);

        if (this.getLayoutX() <= baslangicX) {
            hizX = Math.abs(hizX);
            this.setScaleX(-1);
        }

        if (this.getLayoutX() >= baslangicX + hareketMenzili) {
            hizX = -Math.abs(hizX);
            this.setScaleX(1);
        }
    }

    // Düşman boyutunu dışarıya vermek için
    public static int getBoyut() {
        return DUSMAN_BOYUT;
    }
}