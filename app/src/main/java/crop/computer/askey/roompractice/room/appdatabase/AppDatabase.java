package crop.computer.askey.roompractice.room.appdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import crop.computer.askey.roompractice.room.dao.UserDao;
import crop.computer.askey.roompractice.room.entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
