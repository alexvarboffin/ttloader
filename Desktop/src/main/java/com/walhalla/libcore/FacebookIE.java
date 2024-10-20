package com.walhalla.libcore;

import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacebookIE {


    private final Gson gson = new Gson();


    private Map<String, Object> performLogin(String username, String password) throws IOException {
        String loginPageUrl = "https://www.facebook.com/login.php?next=http%3A%2F%2Ffacebook.com%2Fhome.php&login_attempt=1";
        Request loginPageRequest = new Request.Builder()
                .url(loginPageUrl)
                .addHeader("Cookie", "facebook.com; locale=en_US")
                .build();
        OkHttpClient client = defClient();
        Response loginPageResponse = client.newCall(loginPageRequest).execute();
        String loginPage = loginPageResponse.body().string();

        String lsd = extractValue(loginPage, "<input type=\"hidden\" name=\"lsd\" value=\"([^\"]*)\"");
        String lgnrnd = extractValue(loginPage, "name=\"lgnrnd\" value=\"([^\"]*?)\"");

        Map<String, String> loginForm = new HashMap<>();
        loginForm.put("email", username);
        loginForm.put("pass", password);
        loginForm.put("lsd", lsd);
        loginForm.put("lgnrnd", lgnrnd);
        loginForm.put("next", "http://facebook.com/home.php");
        loginForm.put("default_persistent", "0");
        loginForm.put("legacy_return", "1");
        loginForm.put("timezone", "-60");
        loginForm.put("trynum", "1");

        RequestBody formBody = new FormBody.Builder()
                .add("email", username)
                .add("pass", password)
                .add("lsd", lsd)
                .add("lgnrnd", lgnrnd)
                .add("next", "http://facebook.com/home.php")
                .add("default_persistent", "0")
                .add("legacy_return", "1")
                .add("timezone", "-60")
                .add("trynum", "1")
                .build();
        Request loginRequest = new Request.Builder()
                .url(loginPageUrl)
                .post(formBody)
                .build();
        Response loginResponse = client.newCall(loginRequest).execute();
        String loginResult = loginResponse.body().string();

        if (loginResult.contains("<form") && loginResult.contains("name=\"login\"")) {
            String error = extractValue(loginResult, "<div[^>]+class=(?:\"|').*?login_error_box.*?(?:\"|')[^>]*><div[^>]*>.*?</div><div[^>]*>(?P<error>.+?)</div>");
            if (error != null) {
                throw new IOException("Unable to login: " + error);
            }
            System.out.println("Unable to log in: bad username/password, or exceeded login rate limit (~3/min). Check credentials or wait.");
            return null;
        }

        String fbDtsg = extractValue(loginResult, "name=\"fb_dtsg\" value=\"(.+?)\"");
        String h = extractValue(loginResult, "name=\"h\"\\s+(?:\\w+=\"[^\"]+\"\\s+)*?value=\"([^\"]+)\"");

        if (fbDtsg == null || h == null) {
            return null;
        }

        Map<String, String> checkForm = new HashMap<>();
        checkForm.put("fb_dtsg", fbDtsg);
        checkForm.put("h", h);
        checkForm.put("name_action_selected", "dont_save");

        RequestBody checkFormBody = new FormBody.Builder()
                .add("fb_dtsg", fbDtsg)
                .add("h", h)
                .add("name_action_selected", "dont_save")
                .build();
        Request checkRequest = new Request.Builder()
                .url("https://www.facebook.com/checkpoint/?next=http%3A%2F%2Ffacebook.com%2Fhome.php&_fb_noscript=1")
                .post(checkFormBody)
                .build();
        Response checkResponse = client.newCall(checkRequest).execute();
        String checkResult = checkResponse.body().string();

        if (checkResult.contains("id=\"checkpointSubmitButton\"")) {
            System.out.println("Unable to confirm login, you have to login in your browser and authorize the login.");
            return null;
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("fb_dtsg", fbDtsg);
        responseData.put("h", h);
        return responseData;
    }

    protected OkHttpClient defClient() {
        return new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private Map<String, Object> extractFromUrl(String url, String videoId) throws IOException {
        String realUrl = url.startsWith("facebook:") ? "https://www.facebook.com/video/video.php?v=" + videoId : url;
        // Implement the extraction logic here
        return null;
    }

    private Map<String, Object> realExtract(String url) throws IOException {
        String videoId = url.substring(url.lastIndexOf('/') + 1);
        return extractFromUrl(url, videoId);
    }

//    public void performLogin(String username, String password) throws IOException {
//        // Step 1: Downloading login page
//        Request loginPageRequest = new Request.Builder()
//                .url("https://www.facebook.com/login.php?next=http%3A%2F%2Ffacebook.com%2Fhome.php&login_attempt=1")
//                .addHeader("Cookie", "facebook.com; locale=en_US")
//                .build();
//        Response loginPageResponse = client.newCall(loginPageRequest).execute();
//        String loginPage = loginPageResponse.body().string();
//
//        // Extracting necessary values from the login page
//        String lsd = extractValue(loginPage, "<input type=\"hidden\" name=\"lsd\" value=\"([^\"]*)\"");
//        String lgnrnd = extractValue(loginPage, "name=\"lgnrnd\" value=\"([^\"]*?)\"");
//
//        // Step 2: Logging in
//        RequestBody formBody = new FormBody.Builder()
//                .add("email", username)
//                .add("pass", password)
//                .add("lsd", lsd)
//                .add("lgnrnd", lgnrnd)
//                .add("next", "http://facebook.com/home.php")
//                .add("default_persistent", "0")
//                .add("legacy_return", "1")
//                .add("timezone", "-60")
//                .add("trynum", "1")
//                .build();
//        Request loginRequest = new Request.Builder()
//                .url("https://www.facebook.com/login.php")
//                .post(formBody)
//                .build();
//        Response loginResponse = client.newCall(loginRequest).execute();
//        String loginResult = loginResponse.body().string();
//
//        // Check for errors during login
//        if (loginResult.contains("<form") && loginResult.contains("name=\"login\"")) {
//            // Handling login errors
//            String error = extractValue(loginResult, "<div[^>]+class=(?:\"|').*?login_error_box.*?(?:\"|')[^>]*><div[^>]*>.*?</div><div[^>]*>(?P<error>.+?)</div>");
//            if (error != null) {
//                throw new IOException("Unable to login: " + error);
//            }
//            System.out.println("Unable to log in: bad username/password, or exceeded login rate limit (~3/min). Check credentials or wait.");
//            return;
//        }
//
//        // Extracting necessary values after successful login
//        String fbDtsg = extractValue(loginResult, "name=\"fb_dtsg\" value=\"(.+?)\"");
//        String h = extractValue(loginResult, "name=\"h\"\\s+(?:\\w+=\"[^\"]+\"\\s+)*?value=\"([^\"]+)\"");
//
//        // Step 3: Confirming login
//        if (fbDtsg != null && h != null) {
//            RequestBody checkFormBody = new FormBody.Builder()
//                    .add("fb_dtsg", fbDtsg)
//                    .add("h", h)
//                    .add("name_action_selected", "dont_save")
//                    .build();
//            Request checkRequest = new Request.Builder()
//                    .url("https://www.facebook.com/checkpoint/?next=http%3A%2F%2Ffacebook.com%2Fhome.php&_fb_noscript=1")
//                    .post(checkFormBody)
//                    .build();
//            Response checkResponse = client.newCall(checkRequest).execute();
//            String checkResult = checkResponse.body().string();
//
//            // Check if confirmation is required
//            if (checkResult.contains("id=\"checkpointSubmitButton\"")) {
//                System.out.println("Unable to confirm login, you have to login in your browser and authorize the login.");
//            }
//        }
//    }

    // Helper method to extract values using regex
    private String extractValue(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static final String VALID_URL = "";


    static Pattern pattern = Pattern.compile(VALID_URL);

    public static void main(String[] args) throws IOException {
        FacebookIE main = new FacebookIE();
        //main.performLogin("your_username", "your_password");

        Matcher matcher = pattern.matcher("input");

        if (matcher.find()) {
            System.out.println("Found match: " + matcher.group("id"));
        } else {
            System.out.println("No match found.");
        }
    }


}
