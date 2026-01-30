package com.mtopgul;

public class Message1 {
    // Toplam 48 bit = 6 Byte
    private int label;       // 6 bits
    private long latitude;   // 19 bits (Ham veri)
    private long longitude;  // 19 bits (Ham veri)
    private int gridOrigin;  // 4 bits

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public int getGridOrigin() {
        return gridOrigin;
    }

    public void setGridOrigin(int gridOrigin) {
        this.gridOrigin = gridOrigin;
    }

    /**
     * Değeri hedef havuzun içine belirli bir bit aralığına güvenlice yerleştirir.
     */
    private long packValue(long pool, long value, int startBit, int length) {
        // Uzunluğa göre maske oluştur (Örn: 19 bit için 0x7FFFF)
        long mask = (1L << length) - 1;
        // Değeri maskele ve hedeflenen başlangıç bitine kaydır
        return pool | ((value & mask) << startBit);
    }

    /**
     * Havuzun içinden belirli bir bit aralığını söküp alır.
     */
    private long unpackValue(long pool, int startBit, int length) {
        long mask = (1L << length) - 1;
        return (pool >> startBit) & mask;
    }

    public byte[] encode() {
        byte[] data = new byte[6];
        long packed = 0;

        packed = packValue(packed, label, 0, 6);        // 0-5. bitler
        packed = packValue(packed, latitude, 6, 19);   // 6-24. bitler
        packed = packValue(packed, longitude, 25, 19); // 25-43. bitler
        packed = packValue(packed, gridOrigin, 44, 4); // 44-47. bitler

        // LITTLE ENDIAN: En küçük anlamlı byte (LSB) dizinin başında (index 0)
        for (int i = 0; i < 6; i++) {
            data[i] = (byte) ((packed >> (8 * i)) & 0xFF);
        }
        return data;
    }

    public void decode(byte[] data) {
        if (data == null || data.length < 6) return;

        // Byte dizisini Little Endian olarak long bir havuzda birleştir
        long packed = 0;
        for (int i = 0; i < 6; i++) {
            packed |= ((long) (data[i] & 0xFF)) << (8 * i);
        }

        // Bitleri olduğu gibi (raw) geri al
        label = (int) unpackValue(packed, 0, 6);
        latitude = unpackValue(packed, 6, 19);
        longitude = unpackValue(packed, 25, 19);
        gridOrigin = (int) unpackValue(packed, 44, 4);
    }

    @Override
    public String toString() {
        return "M1Message{" +
                "label=" + label +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", gridOrigin=" + gridOrigin +
                '}';
    }
}
