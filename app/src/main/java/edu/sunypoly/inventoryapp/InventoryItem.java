package edu.sunypoly.inventoryapp;

import android.os.Bundle;

import java.util.HashMap;

class InventoryItem {
    private int id ;
    private int barcode;
    private String qr;
    private String name;
    private String type;
    private int serial;
    private String room;
    private String brand;
    private String acquired;

    public static final String ID = "id";
    public static final String BARCODE = "barcode";
    public static final String QR = "qr";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String SERIAL = "serial";
    public static final String ROOM = "room";
    public static final String BRAND = "brand";
    public static final String ACQUIRED = "acquired";

    private HashMap<String, String> fieldsMap;

    public static final String[] fields = new String[]{ID, BARCODE, QR, NAME, TYPE, SERIAL, ROOM,
            BRAND, ACQUIRED};

    public InventoryItem(){
        fieldsMap = new HashMap<>();
    }

    public InventoryItem(final int id, final int barcode, final String qr, final String name,
                         final String type, final int serial, final String room, final String brand,
                         final String acquired) {
        this.id = id;
        this.barcode = barcode;
        this.qr = qr;
        this.name = name;
        this.type = type;
        this.serial = serial;
        this.room = room;
        this.brand = brand;
        this.acquired = acquired;

        fieldsMap = new HashMap<String, String>(){{
            put(ID, "" + id);
            put(BARCODE, "" + barcode);
            put(QR, qr);
            put(NAME, name);
            put(TYPE, type);
            put(SERIAL, "" + serial);
            put(ROOM, room);
            put(BRAND, brand);
            put(ACQUIRED, acquired);
        }};
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        fieldsMap.put(ID, "" + id);
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
        fieldsMap.put(BARCODE, "" + barcode);
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
        fieldsMap.put(QR, qr);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        fieldsMap.put(NAME, name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        fieldsMap.put(TYPE, type);
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
        fieldsMap.put(SERIAL, "" + serial);
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
        fieldsMap.put(ROOM, room);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
        fieldsMap.put(BRAND, brand);
    }

    public String getAcquired() {
        return acquired;
    }

    public void setAcquired(String acquired) {
        this.acquired = acquired;
        fieldsMap.put(ACQUIRED, acquired);
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + id +
                ", barcode=" + barcode +
                ", qr='" + qr + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", serial=" + serial +
                ", room='" + room + '\'' +
                ", brand='" + brand + '\'' +
                ", acquired='" + acquired + '\'' +
                '}';
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();

        bundle.putInt(ID, id);
        bundle.putInt(BARCODE, barcode);
        bundle.putString(QR, qr);
        bundle.putString(NAME, name);
        bundle.putString(TYPE, type);
        bundle.putInt(SERIAL, serial);
        bundle.putString(ROOM, room);
        bundle.putString(BRAND, brand);
        bundle.putString(ACQUIRED, acquired);

        return bundle;
    }

    public String get(String fieldId){
        return fieldsMap.get(fieldId);
    }
}
