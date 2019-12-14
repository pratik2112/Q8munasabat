package com.q8munasabat.utills;

public class Exiter extends android.app.Activity {

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        System.exit(0);
    }
}