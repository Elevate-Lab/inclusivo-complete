package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.dsciiita.inclusivo.databinding.ActivityPDFViewerBinding;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PDFViewerActivity extends AppCompatActivity implements OnLoadCompleteListener {

    ActivityPDFViewerBinding binding;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPDFViewerBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view-> finish());

        String link, name;
        Intent intent = getIntent();
        if(intent.hasExtra("resume_link")) {
            link = intent.getStringExtra("resume_link");
            name = intent.getStringExtra("name");
            binding.progressBar.setVisibility(View.VISIBLE);

            new RetrivePDFfromUrl().execute(link);

            binding.downloadResume.setOnClickListener(view-> {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if(!checkStorage()){
                        ActivityCompat.requestPermissions(PDFViewerActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                    } else {
                        downloadFile(link, name);
                    }
                } else {
                    downloadFile(link, name);
                }
            });
        } else {
            finish();
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }

        binding.errorAnim.setOnClickListener(view -> binding.errorAnim.playAnimation());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }


    @Override
    public void loadComplete(int nbPages) {
        binding.progressBar.setVisibility(View.GONE);
        binding.downloadResume.setVisibility(View.VISIBLE);
    }


    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
                binding.progressBar.setVisibility(View.GONE);
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            binding.pdfView.fromStream(inputStream).enableSwipe(true)
                    .swipeHorizontal(false)
                    .spacing(5)
                    .enableDoubletap(true)
                    .enableAnnotationRendering(true)
                    .onLoad(PDFViewerActivity.this::loadComplete)
                    .scrollHandle(new DefaultScrollHandle(PDFViewerActivity.this))
                    .load();
        }
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Saved in /storage/emulated/0/Download/inclusivo", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };

    public void downloadFile(String link, String name){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading...");
        progressDialog.show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Inclusivo");
        request.setDescription("Downloading File");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/inclusivo/"+name+".pdf");

        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }


    private boolean checkStorage () {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result==PackageManager.PERMISSION_DENIED){
            Toast.makeText(this, "Requires storage permission", Toast.LENGTH_SHORT).show();
        }
        return result == PackageManager.PERMISSION_GRANTED;
    }
}