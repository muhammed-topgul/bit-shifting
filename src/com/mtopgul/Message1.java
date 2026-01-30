package com.mtopgul;

public class Message1 {
    private int label;
    private long latitude;
    private long longitude;
    private int gridOrigin;

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

    // Maskeleri 'static final' yaparak çalışma zamanı hesaplamasından kurtuluyoruz
    private static final long MASK_6 = (1L << 6) - 1;   // 0x3F
    private static final long MASK_19 = (1L << 19) - 1; // 0x7FFFF
    private static final long MASK_4 = (1L << 4) - 1;   // 0x0F

    public byte[] encode() {
        byte[] data = new byte[6];

        // Dinamik metot çağrısı yerine 'hardcoded' bit kaydırma
        // JIT compiler bunu çok daha agresif optimize eder
        long packed = (label & MASK_6) |
                ((latitude & MASK_19) << 6) |
                ((longitude & MASK_19) << 25) |
                ((long)(gridOrigin & MASK_4) << 44);

        // Döngü yerine 'Unrolled Loop' (Döngüyü açma) tekniği
        // İşlemci dallanma tahmini (branch prediction) maliyetini sıfıra indirir
        data[0] = (byte) (packed);
        data[1] = (byte) (packed >>> 8);
        data[2] = (byte) (packed >>> 16);
        data[3] = (byte) (packed >>> 24);
        data[4] = (byte) (packed >>> 32);
        data[5] = (byte) (packed >>> 40);

        return data;
    }

    public void decode(byte[] data) {
        // Little Endian manuel birleştirme (Unrolled loop)
        long packed = (data[0] & 0xFFL) |
                ((data[1] & 0xFFL) << 8) |
                ((data[2] & 0xFFL) << 16) |
                ((data[3] & 0xFFL) << 24) |
                ((data[4] & 0xFFL) << 32) |
                ((data[5] & 0xFFL) << 40);

        // Maskeleme ve kaydırmayı tek satırda yapıyoruz
        label      = (int) (packed & MASK_6);
        latitude   = (packed >>> 6) & MASK_19;
        longitude  = (packed >>> 25) & MASK_19;
        gridOrigin = (int) (packed >>> 44) & 0x0F;
    }
}
