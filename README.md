# Room Practice

練習 Room API 完成資料儲存。

Room 是基於 SQLite 資料庫完成資料儲存的，但相對高階，很多SQLite操作不需要實作。

以下是Room API簡單說明：

## 定義 Entry
簡單說 Entry 就是 表格的資料結構。

#### `@Entity`
後面接()，可以在()中設定表格特性，包含
- 設定表格名稱(tableName)，預設是用類別的名稱當成表格名稱
- 設定主鍵(primaryKeys)，可以一個或多個，也可以自動生成。


#### `@PrimaryKey`
一定要設定主鍵(Primary Key)，可以一個或多個主鍵。也可以設定自動生成 `@PrimaryKey(autoGenerate = true)`


#### `@ColumnInfo`
可以利用 `@ColumnInfo(name = "first_name")` 設定欄位名稱，預設適用變數名稱作為欄位名稱


#### `@Ignore`
預設上 Room 會將Entry物件中每一個資料成員生成資料欄位，我們可以藉由 `@Ignore` 要求 Room 忽略某些資料成員。

```java
@Entity(tableName = "users")
public class User {

    /**
     * PRIMARY KEY
     */
    @PrimaryKey(autoGenerate = true)
    public int _id;

    /**
     * Column Field
     */
    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    /**
     * Ignore
     */
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
```


## 定義 DAO

### `@Dao`
宣告介面為DAO (Data Access Object)，可以透過DAO操作資料庫。

### `@Query` 執行 SQL 指令

```java
@Query("SELECT * FROM users " +
        "WHERE _id IN (:userIds)")
List<User> loadAllByIds(int[] userIds);
```

```java
@Query("UPDATE users " +
        "SET first_name = :newFirst " +
        "WHERE first_name = :oldFirst")
void updateFirstName(String oldFirst, String newFirst);
```

```java
@Query("DELETE FROM users " +
        "WHERE first_name = :first")
void deleteByFirstName(String first);
```

### `@Insert` 插入資料
```java
@Insert
void insertAll(User... users);
```
### `@Update` 更新資料
```java
@Update
void updateUser(User... user);
```
### `@Delete` 刪除資料
```java
@Delete
void delete(User user);
```

### 完整DAO程式碼
```java
@Dao
public interface UserDao {

    // CREATE
    @Insert
    void insertAll(User... users);

    // READ
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users " +
            "WHERE _id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users " +
            "WHERE first_name LIKE :first AND last_name LIKE :last " +
            "LIMIT 1")
    User findByFullName(String first, String last);

    @Query("SELECT * FROM users " +
            "WHERE first_name LIKE :first " +
            "LIMIT 1")
    User findByFirstName(String first);

    // UPDATE
    @Update
    void updateUser(User... user);

    @Query("UPDATE users " +
            "SET first_name = :newFirst " +
            "WHERE first_name = :oldFirst")
    void updateFirstName(String oldFirst, String newFirst);

    // DELETE
    @Delete
    void delete(User user);

    @Query("DELETE FROM users " +
            "WHERE first_name = :first")
    void deleteByFirstName(String first);

}
```


## 定義 App Database

定義資料庫，設定包含哪些 **Entity(Table)** 及 **版本**

需要繼承 RoomDatabase 並包含 需要用到的 DAO。
```java
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // 需要用到User表格，所以需要他的 DAO
    public abstract UserDao userDao();
}
```

## 操作資料庫

### 取得資料庫物件 並 定義資料庫名稱
```java
AppDatabase database = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME) // 定義
                .build();
```

### 透過 DAO 操作資料庫

由 資料庫物件 取得 DAO，進而由DAO操作資料庫。

:::warning
特別注意的是，透過DAO操作資料庫必須是異步操作，可以使用 `Thread` 或是 `AsyncTask` 完成。
:::

#### 程式碼示範

```java
private void queryAll(final OnDataCallback callback) {

    new Thread(new Runnable() {
        @Override
        public void run() {
            final List<User> users = database.userDao().getAll();
            postDataSuccess("queryAll", users, callback);
        }
    }).start();

}

public void insert(final String first, final String last, final OnDataCallback callback) {

    new Thread(new Runnable() {
        @Override
            public void run() {
            database.userDao().insertAll(new User(first, last));
            postDataSuccess("insert", "INSERT Complete", callback);
        }
    }).start();

}
```

## 參考
- [Save data in a local database using Room](https://developer.android.com/training/data-storage/room)
