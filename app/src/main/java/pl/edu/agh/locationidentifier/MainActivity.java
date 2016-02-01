package pl.edu.agh.locationidentifier;

import android.graphics.PointF;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    public static final int TIME_BETWEEN_NOTIFICATION = 5000;
    long lastTimeUserWasNotifiedAboutBarcode = 0;
    TextToSpeech textToSpeech;
    private QRCodeReaderView mydecoderview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTimeUserWasNotifiedAboutBarcode >= TIME_BETWEEN_NOTIFICATION) {
            lastTimeUserWasNotifiedAboutBarcode = currentTimeMillis;
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void cameraNotFound() {
        Toast.makeText(this, "Error: Camera not found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void QRCodeNotFoundOnCamImage() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
}
