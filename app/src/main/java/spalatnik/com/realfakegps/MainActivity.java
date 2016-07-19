package spalatnik.com.realfakegps;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import spalatnik.com.realfakegps.util.ErrorUtil;
import spalatnik.com.realfakegps.util.FakeGPS;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    CheckBox randomCheck;
    private EditText latitudeView;
    private EditText longitudeView;
    FakeGPS fakeGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fakeGPS = new FakeGPS(this, getContentResolver());

        latitudeView = (EditText) findViewById(R.id.latitude);
        longitudeView = (EditText) findViewById(R.id.longitude);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enterFakeCoordinates()) {
                    Snackbar.make(view, "Engaging fake GPS", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        randomCheck = (CheckBox)findViewById(R.id.checkBox);
        randomCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox)view;
                if(checkBox.isChecked()){
                    Snackbar.make(view, "Enabling random movement", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fakeGPS.enableRandom();
                }
                else {
                    Snackbar.make(view, "Disabling random movement", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fakeGPS.disableRandom();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean enterFakeCoordinates() {
        String latitudeTxt = latitudeView.getText().toString();
        String longitudeTxt = longitudeView.getText().toString();
        boolean failed = false;
        if(StringUtils.isEmpty(latitudeTxt)) {
            latitudeView.setError(getString(R.string.required_latitude));
            failed = true;
        }
        if(StringUtils.isEmpty(longitudeTxt)) {
            longitudeView.setError(getString(R.string.required_longitude));
            failed = true;
        }
        if(!NumberUtils.isNumber(latitudeTxt)) {
            ErrorUtil.showError(getApplicationContext(), "Latitude is not a valid number");
            latitudeView.setError(getString(R.string.invalid_latitude));
            failed = true;
        }
        if(!NumberUtils.isNumber(longitudeTxt)) {
            ErrorUtil.showError(getApplicationContext(), "Longitude is not a valid number");
            longitudeView.setError(getString(R.string.invalid_longitude));
            failed = true;
        }

        if(!failed) {
            fakeGPS.fakeLocation(latitudeTxt, longitudeTxt);
        }
        return !failed;
    }
}
