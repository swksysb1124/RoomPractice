package crop.computer.askey.roompractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import crop.computer.askey.roompractice.model.DataModel;

public class MainActivity extends AppCompatActivity implements DataModel.OnDataCallback {

    DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataModel = new DataModel(this);
    }


    public void insert(View view) {
        EditText edtFirst = findViewById(R.id.edtFirstNameForInsert);
        String first = edtFirst.getText().toString();

        EditText edtLast = findViewById(R.id.edtLastNameForInsert);
        String last = edtLast.getText().toString();

        dataModel.insert(first, last, this);
    }

    public void query(View view) {
        EditText edtFirst = findViewById(R.id.edtFirstNameForQuery);
        final String first = edtFirst.getText().toString();

        dataModel.query(first, this);
    }

    public void delete(View view) {
        EditText edtFirst = findViewById(R.id.edtFirstNameForDelete);
        final String first = edtFirst.getText().toString();

        dataModel.delete(first, this);
    }

    public void update(View view) {
        EditText edtOldFirst = findViewById(R.id.edtOldFirstForUpdate);
        final String oldFirst = edtOldFirst.getText().toString();

        EditText edtNewFirst = findViewById(R.id.edtNewFirstForUpdate);
        final String newFirst = edtNewFirst.getText().toString();

        dataModel.update(oldFirst, newFirst, this);
    }

    private void toast(final String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    private void updateResult(final Object data) {

        TextView txtResult = findViewById(R.id.txtResult);

        if(data instanceof ArrayList) {
            List list = (ArrayList) data;
            StringBuilder builder = new StringBuilder();
            for(Object item: list) {
                builder.append(item.toString()).append("\n");
            }
            txtResult.setText(builder.toString());

        }else {
            txtResult.setText(data.toString());
        }
    }

    @Override
    public void onSuccess(String key, Object data) {
        toast(key + " Complete");
        updateResult(data);
    }

    @Override
    public void onFail(String key, String message) {

    }
}
