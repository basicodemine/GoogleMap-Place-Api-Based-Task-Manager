package gorev.yerservis.com.gorevgo;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class TaskSQLite extends SQLiteOpenHelper {


//
//    Long id;
//    String baslik = "";
//    String icerik = "";
//    String enlem = "";
//    String boylam = "";
//    String bitistarih = "";
//    String baslangictarih = "";
//    String adres = "";
//    String saat = "";
//    String cap = "";
//    String durum = "";

    private static final String DATABASE_NAME = "db_gorev";
    private static final String TABLE_NAME = "GOREVLER";
    private static final String KOLON_ISIM_ID = "id";
    private static final String KOLON_ISIM_BASLIK = "baslik";
    private static final String KOLON_ISIM_ICERIK = "icerik";
    private static final String KOLON_ISIM_ENLEM = "enlem";
    private static final String KOLON_ISIM_BOYLAM = "boylam";
    private static final String KOLON_ISIM_BITISTARIH = "tarih";
    private static final String KOLON_ISIM_BASLANGICTARIH = "tarihb";
    private static final String KOLON_ISIM_ADRES = "adres";
    private static final String KOLON_ISIM_SAAT = "saat";
    private static final String KOLON_ISIM_CAP = "cap";
    private static final String KOLON_ISIM_DURUM = "durum";
    private static final int DATABASE_VERSION = 1;



    public TaskSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createcmd =
                new StringBuilder().append("CREATE TABLE ")
                        .append(TABLE_NAME).append(" ( ")
                        .append(KOLON_ISIM_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                        .append(KOLON_ISIM_BASLIK).append(" TEXT, ")
                        .append(KOLON_ISIM_ICERIK).append(" TEXT, ")
                        .append(KOLON_ISIM_ENLEM).append(" TEXT, ")
                        .append(KOLON_ISIM_BOYLAM).append(" TEXT, ")
                        .append(KOLON_ISIM_BITISTARIH).append(" TEXT, ")
                        .append(KOLON_ISIM_BASLANGICTARIH).append(" TEXT, ")
                        .append(KOLON_ISIM_ADRES).append(" TEXT, ")
                        .append(KOLON_ISIM_SAAT).append(" TEXT, ")
                        .append(KOLON_ISIM_CAP).append(" TEXT, ")
                        .append(KOLON_ISIM_DURUM).append(" TEXT ").append(" ); ").toString();
        db.execSQL(createcmd);
        Log.e("DB KONTROL EDİLDİ", "Bildirim");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void bildirimEkle(TaskItem TaskItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KOLON_ISIM_BASLIK, TaskItem.getBaslik());
        values.put(KOLON_ISIM_ICERIK, TaskItem.getIcerik());
        values.put(KOLON_ISIM_ENLEM, TaskItem.getEnlem());
        values.put(KOLON_ISIM_BOYLAM, TaskItem.getBoylam());
        values.put(KOLON_ISIM_BITISTARIH, TaskItem.getBitistarih());
        values.put(KOLON_ISIM_BASLANGICTARIH, TaskItem.getBaslangictarih());
        values.put(KOLON_ISIM_ADRES, TaskItem.getAdres());
        values.put(KOLON_ISIM_SAAT, TaskItem.getSaat());
        values.put(KOLON_ISIM_CAP, TaskItem.getCap());
        values.put(KOLON_ISIM_DURUM, TaskItem.getDurum());

        db.insert(TABLE_NAME, null, values);
        db.close();
        RefreshCallback.getInstance().changeState(1);
    }

    public void bildirimSil(TaskItem TaskItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KOLON_ISIM_ID + " = ?", new String[]{TaskItem.getId().toString()});
        db.close();
    }


    public void hepsiniSil() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
        RefreshCallback.getInstance().changeState(1);
    }

    public TaskItem bildirimGetir(Long id) {
        String query = " SELECT * FROM " + TABLE_NAME +
                " WHERE " + KOLON_ISIM_ID + " = " + id.toString();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        TaskItem rt = null;
        if (cursor.moveToFirst()) {
            rt = new TaskItem(
                    cursor.getLong(cursor.getColumnIndex(KOLON_ISIM_ID)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_BASLIK)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_ICERIK)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_ENLEM)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_BOYLAM)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_BITISTARIH)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_BASLANGICTARIH)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_ADRES)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_SAAT)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_CAP)),
                    cursor.getString(cursor.getColumnIndex(KOLON_ISIM_DURUM))
            );
        }
        cursor.close();
        db.close();
        return rt;
    }


    public ArrayList<TaskItem> tumBildirimleriGetir() {
        ArrayList<TaskItem> bildirimList = new ArrayList<>();
        String sql_command = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_command, null);
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(cursor.getColumnIndex(KOLON_ISIM_ID));
            String baslik = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_BASLIK));
            String icerik = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_ICERIK));
            String enlem = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_ENLEM));
            String boylam = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_BOYLAM));
            String tarih = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_BITISTARIH));
            String tarihb = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_BASLANGICTARIH));
            String adres = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_ADRES));
            String saat = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_SAAT));
            String cap = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_CAP));
            String durum = cursor.getString(cursor.getColumnIndex(KOLON_ISIM_DURUM));

            TaskItem yenibildirim = new TaskItem(id, baslik, icerik, enlem, boylam, tarih, tarihb, adres, saat, cap, durum);
            Log.e("Oluşmuş Bildirim(SQL)", ":::" + yenibildirim.toString());
            bildirimList.add(yenibildirim);
        }
        cursor.close();
        db.close();
        return bildirimList;


    }
    public void bildirimGuncelle(TaskItem TaskItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KOLON_ISIM_BASLIK, TaskItem.getBaslik());
        values.put(KOLON_ISIM_ICERIK, TaskItem.getIcerik());
        values.put(KOLON_ISIM_ENLEM, TaskItem.getEnlem());
        values.put(KOLON_ISIM_BOYLAM, TaskItem.getBoylam());
        values.put(KOLON_ISIM_BITISTARIH, TaskItem.getBitistarih());
        values.put(KOLON_ISIM_BASLANGICTARIH, TaskItem.getBaslangictarih());
        values.put(KOLON_ISIM_ADRES, TaskItem.getAdres());
        values.put(KOLON_ISIM_SAAT, TaskItem.getSaat());
        values.put(KOLON_ISIM_CAP, TaskItem.getCap());
        values.put(KOLON_ISIM_DURUM, TaskItem.getDurum());

        for (int i = 0; i < values.size(); i++) {
            Log.e("Guncellenecek:" + i, values.toString() + " id=" + TaskItem.getId());
        }
        db.update(TABLE_NAME, values, KOLON_ISIM_ID + " = ?",
                new String[]{TaskItem.getId().toString()});
        db.close();
    }

    public int okunmadiSayisi() {
        int okunmayansayisi = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCount = db.rawQuery("select count(*) from " + TABLE_NAME + " where " + KOLON_ISIM_DURUM + "='NO'", null);
        mCount.moveToFirst();
        okunmayansayisi = mCount.getInt(0);
        mCount.close();
        return okunmayansayisi;
    }


    public void hepsiniOku() {
        String sql_command = "UPDATE " + TABLE_NAME + " SET " + KOLON_ISIM_DURUM + "='OK'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery(sql_command, null);
    }

    public void okunduYap(String id) {
        String sql_command = "UPDATE " + TABLE_NAME + " SET " + KOLON_ISIM_DURUM + "='OK' WHERE "+KOLON_ISIM_ID+" = "+id.toString();
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery(sql_command, null);
    }


}



