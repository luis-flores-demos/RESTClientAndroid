package luisflores.demos.gmail.com;

import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Luis FM on 26/02/2018.
 */

public class  RESTClient<T>{

    private static String TAG_LIMIT = "------------------------------------------------------";

    private Class<T> tClass;

    private HashMap<String ,String> headers = new HashMap<>();

    private int code;

    private SSLContext SSLContext = null;

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public T POST(String urlApi, String dataJSON){
        return new Gson().fromJson(_POST(urlApi, dataJSON), tClass);
    }

    public T GET(String urlApi){
        return new Gson().fromJson(_GET(urlApi), tClass);
    }

    public String _POST(String urlApi, String dataJSON) {

        Log.e(this.getClass().getSimpleName() ,  TAG_LIMIT + "POST" + TAG_LIMIT);

        Log.e(this.getClass().getSimpleName() + " urlApi => ", urlApi);

        Log.e(this.getClass().getSimpleName() + " dataJSON => ", dataJSON);


        InputStream inputStream = null;
        InputStream inputStream_conection = null;
        HttpURLConnection urlConnection_http = null;
        HttpsURLConnection urlConnection_https = null;
        DataOutputStream wr = null;
        String result = null;

        try {
            URL url = new URL(urlApi);

            if("http".equals(url.getProtocol())){
                urlConnection_http = (HttpURLConnection) url.openConnection();
                urlConnection_http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection_http.setRequestProperty("Accept", "application/json");
                urlConnection_http.setRequestMethod("POST");
                urlConnection_http.setDoOutput(true);
                wr = new DataOutputStream(urlConnection_http.getOutputStream());
            }else{
                urlConnection_https = (HttpsURLConnection) url.openConnection();
                urlConnection_https.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection_https.setRequestProperty("Accept", "application/json");
                urlConnection_https.setRequestMethod("POST");
                urlConnection_https.setDoOutput(true);
                if(SSLContext != null){
                    urlConnection_https.setSSLSocketFactory(SSLContext.getSocketFactory());
                    urlConnection_https.setHostnameVerifier(hostnameVerifier(urlApi));
                }
                wr = new DataOutputStream(urlConnection_https.getOutputStream());
            }

            wr.writeBytes(dataJSON);
            wr.flush();
            wr.close();

            if(urlConnection_http != null){
                inputStream_conection = urlConnection_http.getInputStream();
                code = urlConnection_http.getResponseCode();
            }
            else{
                code = urlConnection_https.getResponseCode();
                inputStream_conection = urlConnection_https.getInputStream();
            }

            if (code != 404) {
                StringBuffer sb = new StringBuffer();

                inputStream = new BufferedInputStream(inputStream_conection);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null)
                    sb.append(inputLine);

                result = sb.toString();

                Log.e(this.getClass().getSimpleName() + " result => ", result);


            }

        }
        catch(Exception e){
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName() + " Exception => ", e.getMessage());
        }finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(this.getClass().getSimpleName() + " Exception => ", e.getMessage());
                }

            if (urlConnection_http != null)
                urlConnection_http.disconnect();
            else
                urlConnection_https.disconnect();

            Log.e(this.getClass().getSimpleName() ,  TAG_LIMIT + "POST" + TAG_LIMIT);

        }
        
        return result;

    }

    public String _GET(String urlApi) {

        Log.e(this.getClass().getSimpleName() ,  TAG_LIMIT + "GET" + TAG_LIMIT);


        Log.e(this.getClass().getSimpleName() + " urlApi => ", urlApi);

        InputStream inputStream = null;
        InputStream inputStream_conection = null;
        HttpURLConnection urlConnection_http = null;
        HttpsURLConnection urlConnection_https = null;
        String result = null;

        try {
            URL url = new URL(urlApi);

            if("http".equals(url.getProtocol())){
                urlConnection_http = (HttpURLConnection) url.openConnection();
                urlConnection_http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection_http.setRequestMethod("GET");
                if(headers != null) {
                    for (Map.Entry header : headers.entrySet()) {
                        urlConnection_http.setRequestProperty(header.getKey().toString(), header.getValue().toString());
                        Log.e(this.getClass().getSimpleName() + " headers => ", header.getKey().toString() + " :: " + header.getValue().toString());
                    }
                    headers = null;
                }
            }else{
                urlConnection_https = (HttpsURLConnection) url.openConnection();
                urlConnection_https.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection_https.setRequestMethod("GET");
                if(headers != null) {
                    for (Map.Entry header : headers.entrySet()) {
                        urlConnection_https.setRequestProperty(header.getKey().toString(), header.getValue().toString());
                        Log.e(this.getClass().getSimpleName() + " headers => ", header.getKey().toString() + " :: " + header.getValue().toString());
                    }
                    headers = null;
                }
                if(SSLContext != null){
                    urlConnection_https.setSSLSocketFactory(SSLContext.getSocketFactory());
                    urlConnection_https.setHostnameVerifier(hostnameVerifier(urlApi));
                }
            }

            if(urlConnection_http != null){
                inputStream_conection = urlConnection_http.getInputStream();
                code = urlConnection_http.getResponseCode();
            }
            else{
                inputStream_conection = urlConnection_https.getInputStream();
                code = urlConnection_https.getResponseCode();
            }

            if (code != 404) {
                StringBuffer sb = new StringBuffer();

                inputStream = new BufferedInputStream(inputStream_conection);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null)
                    sb.append(inputLine);

                result = sb.toString();

                Log.e(this.getClass().getSimpleName() + " result => ", result);


            }

        }
        catch(Exception e){
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName() + " Exception => ", e.getMessage());
        }finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(this.getClass().getSimpleName() + " Exception => ", e.getMessage());
                }

            if (urlConnection_http != null)
                urlConnection_http.disconnect();
            else
                urlConnection_https.disconnect();

            Log.e(this.getClass().getSimpleName() ,  TAG_LIMIT + "GET" + TAG_LIMIT);

        }
        
        return result;
        
    }

    public void executeLongOperation(ILongOperation<T> iLongOperation){

        this.tClass = iLongOperation.getTypeClass();

        new LongOperation<T>(code, iLongOperation).execute();

    }

    public Class<T> getTypeClass() {
        return tClass;
    }

    public void setCertificate(InputStream file){

        try {
            Certificate ca;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(file);

            try {
                ca = cf.generateCertificate(caInput);
                Log.e("Certificate",  ((X509Certificate) ca).getSubjectDN().toString());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext = javax.net.ssl.SSLContext.getInstance("TLS");
            SSLContext.init(null, tmf.getTrustManagers(), null);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName() + " Exception => ", e.getMessage());
        }

    }

    private HostnameVerifier hostnameVerifier (final String URL){

        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                if (hv.verify(URL,session)) {
                    return true;
                } else
                    return true;
            }
        };


    }

}
