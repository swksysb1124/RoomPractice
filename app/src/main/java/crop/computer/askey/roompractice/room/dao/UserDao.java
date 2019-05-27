package crop.computer.askey.roompractice.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import crop.computer.askey.roompractice.room.entity.User;

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
