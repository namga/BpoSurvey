package com.bpohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import android.widget.Toast;
import android.os.SystemClock;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import android.util.Base64;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity {
    private WebView webView;
    private ValueCallback<Uri[]> filePathCallback;
    private static final int FILE_CHOOSER_REQUEST = 4102;
    private static final int SAVE_FILE_REQUEST = 4103;
    private byte[] pendingSaveBytes;
    private String pendingSaveMime = "application/octet-stream";
    private long lastRootBackAt = 0L;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(14,24,43));
        getWindow().setNavigationBarColor(Color.rgb(14,24,43));
        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(245,248,247));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setSupportZoom(false);
        webView.addJavascriptInterface(new FileSaveBridge(), "AndroidDownloads");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, WebChromeClient.FileChooserParams params) {
                if (filePathCallback != null) filePathCallback.onReceiveValue(null);
                filePathCallback = callback;
                try {
                    // Do not restrict the Android picker to application/json MIME types.
                    // Files saved by different file managers may be reported as application/octet-stream.
                    // Open all files and validate the selected JSON backup inside the app.
                    Intent picker = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    picker.addCategory(Intent.CATEGORY_OPENABLE);
                    picker.setType("*/*");
                    picker.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(picker, FILE_CHOOSER_REQUEST);
                } catch (Exception firstError) {
                    try {
                        Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                        picker.addCategory(Intent.CATEGORY_OPENABLE);
                        picker.setType("*/*");
                        startActivityForResult(Intent.createChooser(picker, "Chọn file sao lưu JSON"), FILE_CHOOSER_REQUEST);
                    } catch (Exception fallbackError) {
                        filePathCallback = null;
                        callback.onReceiveValue(null);
                        Toast.makeText(MainActivity.this, "Không mở được trình chọn tệp. Hãy kiểm tra ứng dụng Tệp trên điện thoại.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if ("file".equals(uri.getScheme()) || "about".equals(uri.getScheme())) return false;
                if ("https".equals(uri.getScheme()) || "http".equals(uri.getScheme())) {
                    try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); } catch (Exception ignored) {}
                    return true;
                }
                return true;
            }
        });
        setContentView(webView);
        if (savedInstanceState == null) webView.loadUrl("file:///android_asset/index.html");
        else webView.restoreState(savedInstanceState);
    }

    private class FileSaveBridge {
        private static final int BACKUP_ITERATIONS = 210000;
        private SecretKey deriveBackupKey(String password, byte[] salt, int iterations) throws Exception {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
            try {
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                byte[] keyBytes = factory.generateSecret(spec).getEncoded();
                return new SecretKeySpec(keyBytes, "AES");
            } finally { spec.clearPassword(); }
        }
        @JavascriptInterface public String encryptBackup(String plainJson, String password) {
            try {
                if (password == null || password.length() < 8) return "ERROR:Mật khẩu cần tối thiểu 8 ký tự.";
                byte[] salt = new byte[16]; byte[] iv = new byte[12]; SecureRandom random = new SecureRandom();
                random.nextBytes(salt); random.nextBytes(iv);
                SecretKey key = deriveBackupKey(password, salt, BACKUP_ITERATIONS);
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
                byte[] ciphertext = cipher.doFinal(plainJson.getBytes(StandardCharsets.UTF_8));
                JSONObject envelope = new JSONObject();
                envelope.put("format", "BPOHUB_ENCRYPTED_BACKUP"); envelope.put("version", 1);
                envelope.put("algorithm", "AES-256-GCM"); envelope.put("kdf", "PBKDF2-SHA256");
                envelope.put("iterations", BACKUP_ITERATIONS);
                envelope.put("salt", Base64.encodeToString(salt, Base64.NO_WRAP));
                envelope.put("iv", Base64.encodeToString(iv, Base64.NO_WRAP));
                envelope.put("ciphertext", Base64.encodeToString(ciphertext, Base64.NO_WRAP));
                envelope.put("createdAt", new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US).format(new java.util.Date()));
                return envelope.toString();
            } catch (Exception e) { return "ERROR:Không mã hóa được file sao lưu."; }
        }
        @JavascriptInterface public String decryptBackup(String envelopeJson, String password) {
            try {
                if (password == null || password.length() < 1) return "ERROR:Chưa nhập mật khẩu.";
                JSONObject envelope = new JSONObject(envelopeJson);
                if (!"BPOHUB_ENCRYPTED_BACKUP".equals(envelope.optString("format"))) return "ERROR:Định dạng file mã hóa không hợp lệ.";
                int iterations = envelope.optInt("iterations", BACKUP_ITERATIONS);
                if (iterations < 100000 || iterations > 1000000) return "ERROR:Thông số mã hóa không hợp lệ.";
                byte[] salt = Base64.decode(envelope.getString("salt"), Base64.DEFAULT);
                byte[] iv = Base64.decode(envelope.getString("iv"), Base64.DEFAULT);
                byte[] ciphertext = Base64.decode(envelope.getString("ciphertext"), Base64.DEFAULT);
                SecretKey key = deriveBackupKey(password, salt, iterations);
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
                byte[] plain = cipher.doFinal(ciphertext);
                return new String(plain, StandardCharsets.UTF_8);
            } catch (javax.crypto.AEADBadTagException e) { return "ERROR:Sai mật khẩu hoặc file đã bị thay đổi."; }
              catch (Exception e) { return "ERROR:Không giải mã được file. Hãy kiểm tra mật khẩu và định dạng."; }
        }
        @JavascriptInterface public void saveFile(String filename, String mimeType, String base64) {
            runOnUiThread(() -> {
                try {
                    pendingSaveBytes = Base64.decode(base64, Base64.DEFAULT);
                    pendingSaveMime = (mimeType == null || mimeType.trim().isEmpty()) ? "application/octet-stream" : mimeType;
                    launchSaveDialog(filename, pendingSaveMime);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Không chuẩn bị được tệp để lưu.", Toast.LENGTH_LONG).show();
                }
            });
        }
        @JavascriptInterface public void saveBundledTemplate() {
            runOnUiThread(() -> {
                try (InputStream in = getAssets().open("assets/BPO-Intelligence-Hub-Excel-Template.xlsx"); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[8192]; int n;
                    while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
                    pendingSaveBytes = out.toByteArray();
                    pendingSaveMime = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    launchSaveDialog("BPO-Intelligence-Hub-Excel-Template.xlsx", pendingSaveMime);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Không tìm thấy template Excel tích hợp trong app.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void launchSaveDialog(String filename, String mimeType) {
        try {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType(mimeType);
            intent.putExtra(Intent.EXTRA_TITLE, filename == null || filename.trim().isEmpty() ? "BPO-export" : filename);
            startActivityForResult(intent, SAVE_FILE_REQUEST);
        } catch (Exception e) {
            Toast.makeText(this, "Không mở được nơi lưu tệp. Hãy kiểm tra ứng dụng Tệp trên điện thoại.", Toast.LENGTH_LONG).show();
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST && filePathCallback != null) {
            Uri[] results = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
            return;
        }
        if (requestCode == SAVE_FILE_REQUEST) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null && pendingSaveBytes != null) {
                try (OutputStream out = getContentResolver().openOutputStream(data.getData(), "w")) {
                    if (out == null) throw new IOException("Output stream unavailable");
                    out.write(pendingSaveBytes); out.flush();
                    Toast.makeText(this, "Đã lưu tệp thành công.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Lưu tệp thất bại. Hãy thử chọn thư mục khác.", Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Đã hủy lưu tệp.", Toast.LENGTH_SHORT).show();
            }
            pendingSaveBytes = null;
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) { super.onSaveInstanceState(outState); if (webView != null) webView.saveState(outState); }
    @Override public void onBackPressed() {
        if (webView == null) { super.onBackPressed(); return; }
        webView.evaluateJavascript("(function(){return window.handleAppBack?window.handleAppBack():false})()", value -> {
            boolean handled = "true".equals(value);
            if (handled) { lastRootBackAt = 0L; return; }
            long now = SystemClock.elapsedRealtime();
            if (now - lastRootBackAt < 2000L) { finish(); }
            else { lastRootBackAt = now; Toast.makeText(MainActivity.this, "Đang ở trang chính. Nhấn Back lần nữa để thoát.", Toast.LENGTH_SHORT).show(); }
        });
    }
}
