package crop.computer.askey.roompractice.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

import java.util.Locale;

// @Entity
// 後面接()，可以在()中設定表格特性，包含
// - 設定表格名稱(tableName)，預設是用類別的名稱當成表格名稱
// - 設定主鍵(primaryKeys)，可以一個或多個，也可以自動生成。


@Entity(tableName = "users")
public class User {

    /**
     * PRIMARY KEY
     */
    // 一定要設定 Primary Key
    @PrimaryKey(autoGenerate = true)
    public int _id;

    /**
     * Column Field
     */
    // 可以利用 @ColumnInfo 設定欄位名稱，預設適用變數名稱作為欄位名稱
    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    /**
     * Ignore
     */
    // 預設上 Room 會將Entry物件中每一個資料成員生成資料欄位，
    // 我們可以藉由 @Ignore 要求 Room 忽略某些資料成員
    @Ignore
    Bitmap picture;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "[id: %d, first-name: %s, last-name: %s]",
                _id, firstName, lastName);
    }
}
