package com.oyunprojem;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

public class OyunAna extends Application {

    // Pencere ve Dünya Boyutları
    private static final int PENCERE_GENISLIK = 800;
    private static final int PENCERE_YUKSEKLIK = 600;
    private static int DUNYA_GENISLIK = 2000;

    // Oyun Elemanları
    private static final int ZEMIN_YUKSEKLIK = 60;
    private static final int OYUNCU_BASLANGIC_X = 100;
    private static final int OYUNCU_BASLANGIC_Y = 100;

    // Sınıf seviyesindeki oyun elemanları
    private Oyuncu oyuncu;
    private List<Rectangle> platformlar = new ArrayList<>();
    private List<Dusman> dusmanlar = new ArrayList<>();
    private List<Circle> jetonlar = new ArrayList<>();
    private Rectangle hedef;
    private Text kazandinizMetni;
    private Text bolumMetni;
    private ImageView arkaPlan;

    private boolean oyunBitti = false;
    private int puan = 0;

    // Bölüm Sistemi
    private int guncelBolum = 1;
    private static final int MAKSIMUM_BOLUM = 2;

    // Tuş durumu değişkenleri
    private boolean ziplamaTusuBasili = false;
    private boolean solTusuBasili = false;
    private boolean sagTusuBasili = false;

    private AnimationTimer oyunDongusu;

    // Panel Mimarisi (Menüsüz Hali)
    private AnchorPane root;        // Ana panel (Kamerayı ve Arayüzü tutar)
    private AnchorPane oyunDunyasi; // Kayan dünya (Oyuncu, platformlar vb. buradadır)

    @Override
    public void start(Stage primaryStage) {
        try {
            // Panelleri oluştur
            root = new AnchorPane();
            root.setPrefSize(PENCERE_GENISLIK, PENCERE_YUKSEKLIK);
            oyunDunyasi = new AnchorPane();
            root.getChildren().add(oyunDunyasi); // Kayan dünyayı ana panele ekle

            Scene scene = new Scene(root, PENCERE_GENISLIK, PENCERE_YUKSEKLIK);

            // Klavye Dinleyicileri
            scene.setOnKeyPressed((KeyEvent event) -> {
                if (event.getCode() == KeyCode.SPACE) ziplamaTusuBasili = true;
                if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) solTusuBasili = true;
                if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) sagTusuBasili = true;
            });
            scene.setOnKeyReleased((KeyEvent event) -> {
                if (event.getCode() == KeyCode.SPACE) ziplamaTusuBasili = false;
                if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) solTusuBasili = false;
                if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) sagTusuBasili = false;
            });

            // Arayüz (UI) Elemanları
            bolumMetni = new Text(10, 30, "");
            bolumMetni.setFont(Font.font("Arial", 24));
            bolumMetni.setFill(Color.BLACK);

            kazandinizMetni = new Text("TEBRİKLER!\nOYUNU BİTİRDİNİZ!");
            kazandinizMetni.setFont(Font.font("Arial", 60));
            kazandinizMetni.setFill(Color.BLUEVIOLET);
            kazandinizMetni.setLayoutX((PENCERE_GENISLIK - kazandinizMetni.getLayoutBounds().getWidth()) / 2.5);
            kazandinizMetni.setLayoutY(PENCERE_YUKSEKLIK / 2);
            kazandinizMetni.setVisible(false);

            // Oyuncuyu Oluştur
            oyuncu = new Oyuncu(OYUNCU_BASLANGIC_X, OYUNCU_BASLANGIC_Y);

            // Arka Planı Yükle
            try {
                Image backgroundImage = new Image(getClass().getResourceAsStream("/background.png"));
                arkaPlan = new ImageView(backgroundImage);
                arkaPlan.setFitHeight(PENCERE_YUKSEKLIK);
                oyunDunyasi.getChildren().add(0, arkaPlan); // Kayan dünyaya en alta ekle
            } catch (Exception e) {
                System.err.println("Hata: background.png dosyası 'resources' klasöründe bulunamadı!");
            }

            // İlk bölümü yükle
            bolumuYukle(guncelBolum);

            // Arayüzü (UI) 'root'a ekle (Sabit kalacaklar, kaymayacaklar)
            root.getChildren().addAll(bolumMetni, kazandinizMetni);

            // Oyun Döngüsünü Oluştur VE BAŞLAT
            oyunDongusu = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    oyunuGuncelle();
                }
            };
            oyunDongusu.start(); // <-- Menü yok, oyun hemen başlar

            // Pencereyi Göster
            primaryStage.setTitle("Basit 2D Platform Oyunu");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bölüm Yükleme Metodu
    private void bolumuYukle(int bolumNumarasi) {

        oyunBitti = false;
        kazandinizMetni.setVisible(false);
        platformlar.clear();
        dusmanlar.clear();
        jetonlar.clear();

        // Arka plan hariç kayan dünyadaki her şeyi temizle
        oyunDunyasi.getChildren().removeIf(node -> node != arkaPlan);

        guncelBolum = bolumNumarasi;
        puanMetniniGuncelle();

        if (bolumNumarasi == 1) {
            DUNYA_GENISLIK = 2000;
            oyunDunyasi.setPrefWidth(DUNYA_GENISLIK);
            arkaPlan.setFitWidth(DUNYA_GENISLIK);

            platformlar.add(olusturPlatform(0, PENCERE_YUKSEKLIK - ZEMIN_YUKSEKLIK, DUNYA_GENISLIK, ZEMIN_YUKSEKLIK, Color.GREEN));
            platformlar.add(olusturPlatform(200, 400, 150, 30, Color.DARKORANGE)); // P1 (index 1)
            platformlar.add(olusturPlatform(450, 300, 100, 30, Color.DARKORANGE)); // P2 (index 2)
            platformlar.add(olusturPlatform(650, 450, 100, 30, Color.DARKORANGE)); // P3 (index 3)
            platformlar.add(olusturPlatform(900, 350, 200, 30, Color.DARKORANGE)); // P4 (index 4)
            platformlar.add(olusturPlatform(1200, 300, 150, 30, Color.DARKORANGE)); // P5 (index 5)
            platformlar.add(olusturPlatform(1500, 250, 100, 30, Color.DARKORANGE)); // P6 (index 6)
            platformlar.add(olusturPlatform(1850, PENCERE_YUKSEKLIK - ZEMIN_YUKSEKLIK, 50, 30, Color.GREEN));
            dusmanlar.add(new Dusman(300, PENCERE_YUKSEKLIK - ZEMIN_YUKSEKLIK - Dusman.getBoyut(), 100));
            dusmanlar.add(new Dusman(platformlar.get(2).getLayoutX(), platformlar.get(2).getLayoutY() - Dusman.getBoyut(), 50));
            dusmanlar.add(new Dusman(platformlar.get(4).getLayoutX(), platformlar.get(4).getLayoutY() - Dusman.getBoyut(), 150));
            jetonlar.add(olusturJeton(platformlar.get(1).getLayoutX() + 75, platformlar.get(1).getLayoutY() - 30));
            jetonlar.add(olusturJeton(600, 450));
            jetonlar.add(olusturJeton(platformlar.get(4).getLayoutX() + 30, platformlar.get(4).getLayoutY() - 30));
            jetonlar.add(olusturJeton(1600, 400));
            hedef = olusturPlatform(1850, PENCERE_YUKSEKLIK - ZEMIN_YUKSEKLIK - 80, 50, 80, Color.PURPLE);

        } else if (bolumNumarasi == 2) {
            DUNYA_GENISLIK = 1200;
            oyunDunyasi.setPrefWidth(DUNYA_GENISLIK);
            arkaPlan.setFitWidth(DUNYA_GENISLIK);
            platformlar.add(olusturPlatform(0, PENCERE_YUKSEKLIK - ZEMIN_YUKSEKLIK, 400, ZEMIN_YUKSEKLIK, Color.GREEN));
            platformlar.add(olusturPlatform(800, PENCERE_YUKSEKLIK - ZEMIN_YUKSEKLIK, 400, ZEMIN_YUKSEKLIK, Color.GREEN));
            platformlar.add(olusturPlatform(500, 450, 100, 30, Color.DARKORANGE)); // P1 (index 2)
            platformlar.add(olusturPlatform(650, 350, 100, 30, Color.DARKORANGE)); // P2 (index 3)
            dusmanlar.add(new Dusman(platformlar.get(2).getLayoutX(), platformlar.get(2).getLayoutY() - Dusman.getBoyut(), 50));
            dusmanlar.add(new Dusman(850, PENCERE_YUKSEKLIK - ZEMIN_YUKSEKLIK - Dusman.getBoyut(), 150));
            jetonlar.add(olusturJeton(platformlar.get(2).getLayoutX() + 35, platformlar.get(2).getLayoutY() - 100));
            jetonlar.add(olusturJeton(platformlar.get(3).getLayoutX() + 50, platformlar.get(3).getLayoutY() - 30));
            hedef = olusturPlatform(1100, PENCERE_YUKSEKLIK - ZEMIN_YUKSEKLIK - 80, 50, 80, Color.PURPLE);
        }

        // Elemanları 'oyunDunyasi'na ekle (KAYACAK OLANLAR)
        oyunDunyasi.getChildren().addAll(platformlar);
        oyunDunyasi.getChildren().addAll(dusmanlar);
        oyunDunyasi.getChildren().addAll(jetonlar);
        oyunDunyasi.getChildren().add(hedef);
        oyunDunyasi.getChildren().add(oyuncu); // Oyuncu kayan dünyaya eklenir

        oyuncu.pozisyonuSifirla(OYUNCU_BASLANGIC_X, OYUNCU_BASLANGIC_Y);
    }

    // Yardımcı Metotlar (Platform, Jeton oluşturma)
    private Rectangle olusturPlatform(double x, double y, double genislik, double yukseklik, Color renk) {
        Rectangle platform = new Rectangle(genislik, yukseklik, renk);
        platform.setLayoutX(x);
        platform.setLayoutY(y);
        return platform;
    }
    private Circle olusturJeton(double x, double y) {
        Circle jeton = new Circle(15, Color.GOLD);
        jeton.setLayoutX(x);
        jeton.setLayoutY(y);
        return jeton;
    }

    // Ana Oyun Döngüsü
    private void oyunuGuncelle() {
        if (oyunBitti) return;
        oyuncu.hareketEt(solTusuBasili, sagTusuBasili, DUNYA_GENISLIK);
        if (ziplamaTusuBasili && oyuncu.isYerdeMi()) {
            oyuncu.zipla();
            sesCal("jump.wav");
        }
        oyuncu.guncelle(platformlar, DUNYA_GENISLIK, solTusuBasili, sagTusuBasili);
        for (Dusman dusman : dusmanlar) {
            dusman.guncelle();
        }
        for (Dusman dusman : new ArrayList<>(dusmanlar)) {
            if (oyuncu.getBoundsInParent().intersects(dusman.getBoundsInParent())) {
                double oyuncuHizi = oyuncu.getHizY();
                double oyuncuAlti = oyuncu.getLayoutY() + Oyuncu.getOyuncuBoyut();
                double dusmanUstu = dusman.getLayoutY();
                if (oyuncuHizi > 0 && oyuncuAlti <= dusmanUstu + 20) {
                    sesCal("stomp.wav");
                    oyunDunyasi.getChildren().remove(dusman);
                    dusmanlar.remove(dusman);
                    puan += 50;
                    puanMetniniGuncelle();
                    oyuncu.dusmanUstuZipla();
                } else {
                    sesCal("hit.wav");
                    puan = 0;
                    bolumuYukle(guncelBolum);
                    return;
                }
            }
        }
        if (oyuncu.getLayoutY() > PENCERE_YUKSEKLIK + 100) {
            sesCal("hit.wav");
            puan = 0;
            bolumuYukle(guncelBolum);
            return;
        }
        List<Circle> toplananJetonlar = new ArrayList<>();
        for (Circle jeton : jetonlar) {
            if (oyuncu.getBoundsInParent().intersects(jeton.getBoundsInParent())) {
                toplananJetonlar.add(jeton);
                puan += 10;
            }
        }
        if (!toplananJetonlar.isEmpty()) {
            sesCal("coin.wav");
            jetonlar.removeAll(toplananJetonlar);
            oyunDunyasi.getChildren().removeAll(toplananJetonlar);
            puanMetniniGuncelle();
        }
        if (oyuncu.getBoundsInParent().intersects(hedef.getBoundsInParent())) {
            if (jetonlar.isEmpty()) {
                if (guncelBolum == MAKSIMUM_BOLUM) {
                    oyunBitti = true;
                    oyunDongusu.stop();
                    kazandinizMetni.setVisible(true);
                } else {
                    guncelBolum++;
                    puan = 0;
                    bolumuYukle(guncelBolum);
                }
            }
        }

        // Kamera Takibi
        double kameraX = oyuncu.getLayoutX() - (PENCERE_GENISLIK / 2);
        kameraX = Math.max(0, kameraX);
        kameraX = Math.min(DUNYA_GENISLIK - PENCERE_GENISLIK, kameraX);

        // Kayan dünyayı hareket ettir
        oyunDunyasi.setLayoutX(-kameraX);

        // Arayüzü (UI) kameraya sabitle (root'a göre)
        bolumMetni.setLayoutX(10);
        bolumMetni.setLayoutY(30);
        kazandinizMetni.setLayoutX((PENCERE_GENISLIK - kazandinizMetni.getLayoutBounds().getWidth()) / 2.5);
        kazandinizMetni.setLayoutY(PENCERE_YUKSEKLIK / 2);
    }

    // Puan Metnini Güncelleme
    private void puanMetniniGuncelle() {
        bolumMetni.setText("Puan: " + puan + "  -  Bölüm: " + guncelBolum);
    }

    // Ses Çalma
    private void sesCal(String dosyaAdi) {
        try {
            URL resource = getClass().getResource("/" + dosyaAdi);
            if (resource == null) { System.err.println("Ses dosyası bulunamadı: " + dosyaAdi); return; }
            Media media = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Ses çalınırken hata oluştu: " + e.getMessage());
        }
    }

    // Main Metot
    public static void main(String[] args) {
        launch(args);
    }
}