# JavaFX 2D Platform Oyunu Motoru

Bu proje, Java ve JavaFX kullanılarak sıfırdan geliştirilmiş, "arşa çıkmış" eksiksiz bir 2D platform oyunu motorudur. Bu yolculukta, basit bir kareden yola çıkarak ana menüsü, seviyeleri ve hareketli düşmanları olan profesyonel bir oyunun tüm çekirdek mekaniklerini inşa ettik.

<img width="998" height="782" alt="Ekran görüntüsü 2025-11-09 194932" src="https://github.com/user-attachments/assets/819f5a75-bc14-4b03-8e6d-07db3dea507a" />
---

## ✨ Temel Özellikler (Features)

Bu motor, modern bir 2D platform oyununda beklenen birçok profesyonel özelliği içerir:

* **Nesne Yönelimli Mimari (OOP):** Tüm oyun mantığı, yönetilebilir sınıflara (`OyunAna`, `Oyuncu`, `Dusman`) bölünmüştür.
* **Sprite Animasyonu:** Oyuncu karakteri için `idle` (durma) ve `walk` (yürüme) animasyon kareleri arasında geçiş.
* **Fizik Motoru:** Yerçekimi, platform çarpışma tespiti (kenardan düşme hatası düzeltmesi dahil) ve zıplama.
* **Side-Scroller Kamera:** Oyuncuyu takip eden ve 2000 piksel genişliğindeki dünyada kayan bir kamera.
* **Seviye Sistemi:** Birden fazla bölüm (`Bölüm 1`, `Bölüm 2`) yükleme ve bölümler arası geçiş.
* **Hareketli Düşmanlar:** Platformlar üzerinde devriye gezen (`patrol`) yapay zekaya sahip `Dusman` sınıfı.
* **Savaş Mekaniği:** Mario tarzı "ezme" (stomp) mekaniği ile düşmanların üstüne zıplayarak onları yok etme.
* **Ölüm ve Yeniden Başlama:** Düşmana yandan çarpma veya boşluğa düşme durumunda bölümü yeniden başlatan "ölüm düzlemi".
* **Toplanabilir Öğeler:** Puan veren ve bölümü bitirmek için toplanması zorunlu olan "jetonlar".
* **Kazanma Koşulu:** Tüm jetonlar toplandığında aktifleşen "hedef" noktası.
* **Komple Ses Yönetimi:** `jump`, `coin`, `hit`, `stomp` için ses efektleri.
* **Arayüz (UI) Yönetimi:**
    * Oyun içi HUD (Puan ve Bölüm göstergesi).
    * "Oyunu Başlat" ve "Çıkış" butonları olan bir **Ana Menü**.
    * Oyun sonu "Kazandınız!" ekranı.
* **Görsel Dünya:** Kayan bir arka plan (`background.png`) ile zenginleştirilmiş görsellik.

---

## 🛠️ Kullanılan Teknolojiler

* **Java** (JDK 23)
* **JavaFX** (Controls, Media)
* **Maven** (Bağımlılık yönetimi için)
* **IntelliJ IDEA** (Geliştirme ortamı)

---

## 🚀 Kurulum ve Çalıştırma

Bu projeyi yerel makinenizde çalıştırmak için:


1.  Bu depoyu klonlayın (veya ZIP olarak indirin):
    ```bash
    git clone [https://github.com/SENIN-KULLANICI-ADIN/JavaFXPlatformerGame.git](https://github.com/SENIN-KULLANICI-ADIN/JavaFXPlatformerGame.git)
    ```
    *(Yukarıdaki linki kendi GitHub repo linkiniz ile güncelleyin.)*

2.  Projeyi IntelliJ IDEA ile açın.
3.  IntelliJ, `pom.xml` dosyasını otomatik olarak algılayacak ve gerekli JavaFX bağımlılıklarını (media, controls) indirecektir.
4.  `src/main/resources` klasörünün tüm görsel (`.png`) ve ses (`.wav`) dosyalarını içerdiğinden emin olun.
5.  **ÖNEMLİ:** Oyunu çalıştırmak için `OyunAna.java` dosyasını **değil**, **`Launcher.java`** dosyasını çalıştırın.

---

## 🎮 Nasıl Oynanır

* **Hareket:** `A` / `Sol Ok Tuşu` (Sola Git) - `D` / `Sağ Ok Tuşu` (Sağa Git)
* **Zıplama:** `Boşluk (Space)`
* **Amaç:** Tüm sarı jetonları toplayın ve mor hedefe ulaşın. Kırmızı düşmanlara yandan çarpmamaya dikkat edin, ancak onları yenmek için üzerlerine zıplayabilirsiniz!

---

## 🌟 Varlıklar (Assets)

Bu projede kullanılan tüm görseller ve sesler, [Kenney](https://www.kenney.nl/) ve [OpenGameArt.org](https://opengameart.org/) sitelerinden alınan ücretsiz ve kamuya açık varlıklardır.
