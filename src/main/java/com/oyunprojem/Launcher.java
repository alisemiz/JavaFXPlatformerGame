package com.oyunprojem; // Paket adınızın aynı olduğundan emin olun

import javafx.application.Application;

// Bu sınıf, JavaFX'in "runtime components are missing" hatasını
// aşmak için kullandığımız bir başlatıcıdır.
public class Launcher {

    public static void main(String[] args) {
        // Asıl JavaFX uygulamamızı (OyunAna sınıfı) buradan başlatıyoruz.
        Application.launch(OyunAna.class, args);
    }
}