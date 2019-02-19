package edu.sunypoly.inventoryapp;

class InventoryItem {
    public int id ;
    public int barcode;
    public String qr;
    public String name;
    public String type;
    public int serial;
    public String room;
    public String brand;
    public String acquired;

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
}
