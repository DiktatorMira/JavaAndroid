package com.example.android213;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RateActivity extends AppCompatActivity {
    private static final String nbuUrl = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json", cacheFilename = "nbu_rates_cache.json";
    private static List<NbuRate> cachedNbuRates = null;
    private List<NbuRate> nbuRates;
    private LinearLayout ratesContainer;
    private Drawable rateBg;
    private ExecutorService pool;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rateBg = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.rate_shape);
        ratesContainer = findViewById(R.id.rate_container);
        findViewById(R.id.rate_btn_close).setOnClickListener(v -> finish());
        pool = Executors.newFixedThreadPool(3);

        if(cachedNbuRates == null) {
            try(FileInputStream fis = openFileInput(cacheFilename)) {
                Log.d("onCreate", "try restoring cache from file");
                String content = readAllText(fis);
                mapRates(content);
                Log.d("onCreate", "cache from file restored");
                showRates();
            } catch(IOException | JSONException ignored) {
                Log.d("onCreate", "file cache failed, reloading");
                pool.submit(this::loadRates);
            }
        } else {
            Log.d("onCreate", "cache from memory restored");
            nbuRates = cachedNbuRates;
            showRates();
        }
    }
    private boolean isRatesActual() {
        try {
            nbuRates.get(0).getExchangeDate().before(NbuRate.dateFormat.parse(NbuRate.dateFormat.format(new Date())));
        } catch (Exception ex) {
            Log.d("isRatesActual", ex.getCause() + ex.getMessage());
        }
        return false;
    }
    private void mapRates(String jsonText) throws JSONException {
        JSONArray jsonArr = new JSONArray(jsonText);
    }
    private void loadRates() {
        try {
            Log.d("loadRates", "Loading started");
            Thread.sleep(1);
            String text = fetchUrlText(nbuUrl);
            pool.submit(() -> {
                try(FileOutputStream fos = openFileOutput(cacheFilename, Context.MODE_PRIVATE)) {
                    fos.write(text.getBytes(StandardCharsets.UTF_8));
                    Log.d("loadRates", "file cache saved");
                } catch (IOException ex){
                    Log.d("", ex.getCause() + ex.getMessage());
                }
            });
            mapRates(text);
            runOnUiThread(this::showRates);
        } catch (RuntimeException | JSONException ignored) {}
        catch(InterruptedException ex) {
            Log.d("loadRates", "InterruptedException");
        }
    }
    private void showRates() {
        for (NbuRate rate : nbuRates) ratesContainer.addView(rateView(rate));
    }
    private View rateView(NbuRate rate) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10,5,10,5);
        LinearLayout layout  = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(layoutParams);
        layout.setBackground(rateBg);

        TextView tv = new TextView(this);
        tv.setText(rate.getCc());
        tv.setLayoutParams(layoutParams);
        layout.addView( tv );
        tv = new TextView(this);
        tv.setText(rate.getRate()+ "");
        tv.setLayoutParams(layoutParams);
        layout.addView( tv );

        layout.setOnClickListener(this::onRateClick);
        layout.setTag(rate);
        return layout;
    }
    private void onRateClick(View view) {
        NbuRate rate = (NbuRate)view.getTag();
        String message = getString(
                R.string.rate_info,rate.getText(), rate.getCc(),
                rate.getR030(), "13.03.2025", rate.getRate()
        );
        new AlertDialog.Builder(this).setTitle("Информация про курс").setMessage(message).setPositiveButton("OK", null).show();
    }
    private String fetchUrlText(String href) throws RuntimeException {
        try(InputStream urlStream = new URL(href).openStream()) { return readAllText(urlStream); }
        catch(IOException | android.os.NetworkOnMainThreadException | java.lang.SecurityException ex) {
            Log.d("loadRates", "MalformedURLException: "+ ex.getCause() + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
    private String readAllText(InputStream inputStream) {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream();
        int receivedBytes;
        while ((receivedBytes = inputStream.read(buffer)) > 0) byteBuilder.write(buffer, 0, receivedBytes);
        return byteBuilder.toString();
    }
    @Override protected void onDestroy() {
        if(pool != null) pool.shutdownNow();
        super.onDestroy();
    }
}