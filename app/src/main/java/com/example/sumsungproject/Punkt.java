package com.example.sumsungproject;

import java.util.ArrayList;
import java.util.HashMap;

public class Punkt {
    public String id, address;
    public int last_up_d;
    public int last_up_m;
    public int n_workers;
    public HashMap<String, HashMap<String, HashMap<String, String>>> telephones;

    public Punkt() {
    }

    public Punkt(String id, String address, int last_up_d, int last_up_m, int n_workers) {
        this.n_workers = n_workers;
        this.last_up_m = last_up_m;
        this.last_up_d = last_up_d;
        this.id = id;
        this.address = address;
        telephones = new HashMap<>();
        for (int i = 0; i <=6;i++){
            HashMap<String, HashMap<String, String>> tele = new HashMap<>();
            for (int j = 8; j <= 22; j++) {
                HashMap<String, String> tel = new HashMap<>();
                for (int k = 1; k <= n_workers; k++) {
                    tel.put("i" + k, "0");
                }
                tele.put("i" + j, tel);

            }
            telephones.put("i" + i, tele);
        }

    }
}
