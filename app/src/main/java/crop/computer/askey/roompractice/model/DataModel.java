package crop.computer.askey.roompractice.model;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import crop.computer.askey.roompractice.room.appdatabase.Contact;
import crop.computer.askey.roompractice.room.entity.User;

public class DataModel {

    private Contact database;

    private Handler handler = new Handler(Looper.getMainLooper());

    public DataModel(Context context) {
        database = Room.databaseBuilder(
                context.getApplicationContext(),
                Contact.class,
                "Contact.db").build();
    }

    private void insertAll(final OnDataCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User[] users = {
                        new User("Jason", "Su"),
                        new User("Rico", "Su"),
                        new User("Gary", "Su"),
                        new User("NN", "Su")};

                database.userDao().insertAll(users);
                postDataSuccess("insertAll", "Insert All Complete", callback);
            }
        }).start();

    }

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

    public void query(final String first, final OnDataCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(first.isEmpty()) {
                    queryAll(callback);
                    return;
                }

                User user = database.userDao().findByFirstName(first);
                postDataSuccess("query", user, callback);
            }
        }).start();
    }

    public void delete(final String first, final OnDataCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.userDao().deleteByFirstName(first);
                postDataSuccess("delete", "DELETE Complete", callback);
            }
        }).start();

    }

    public void update(final String oldFirst, final String newFirst, final OnDataCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.userDao().updateFirstName(oldFirst, newFirst);
                postDataSuccess("update", "UPDATE Complete", callback);
            }
        }).start();
    }

    public synchronized void postDataSuccess(final String key, final Object data, final OnDataCallback callback) {
        if(handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(callback != null) {
                        callback.onSuccess(key, data);
                    }
                }
            });
        }
    }

    public synchronized void postDataFail(final String key, final String error, final OnDataCallback callback) {
        if(handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(callback != null) {
                        callback.onFail(key, error);
                    }
                }
            });
        }
    }

    public interface OnDataCallback {
        void onSuccess(String key, Object data);
        void onFail(String key, String message);
    }
}
