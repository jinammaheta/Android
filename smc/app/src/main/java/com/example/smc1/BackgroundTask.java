package com.example.smc1;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String, Void, String> {

    response r1=null;
    String res="";
    Context c;
    BackgroundTask(Context c, response r)
    {
        this.r1=r;
        this.c=c;
    }
    @Override
    protected void onPreExecute()
    {
    }
    @Override
    protected String doInBackground(String... params) {

        if (params[0].equals("Login")) {
            String regurl = "https://smarttutorial.000webhostapp.com/login.php";
            try {
                URL url = new URL(regurl);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("POST");
                huc.connect();
                String data=URLEncoder.encode("user_name")+"="+URLEncoder.encode(params[1])+"&"
                        +URLEncoder.encode("password")+"="+URLEncoder.encode(params[2]);
                OutputStream os=huc.getOutputStream();
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
                bw.write(data);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = huc.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                res = br.readLine();


            } catch (MalformedURLException e) {
                res ="1"+ e.getMessage();

            } catch (FileNotFoundException e) {
                res = "2"+e.getMessage();
            } catch (Exception e) {
                res = "3"+e.getMessage();
            }


        }
        if (params[0].equals("adddata")) {
            String regurl = "https://smarttutorial.000webhostapp.com/adddata.php";
            try {
                URL url = new URL(regurl);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("POST");
                huc.connect();
                String data=URLEncoder.encode("name")+"="+URLEncoder.encode(params[1])+"&"
                        +URLEncoder.encode("regno")+"="+URLEncoder.encode(params[2])+"&"
                +URLEncoder.encode("area")+"="+URLEncoder.encode(params[3]);
                OutputStream os=huc.getOutputStream();
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
                bw.write(data);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = huc.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                res = br.readLine();


            } catch (MalformedURLException e) {
                res ="1"+ e.getMessage();

            } catch (FileNotFoundException e) {
                res = "2"+e.getMessage();
            } catch (Exception e) {
                res = "3"+e.getMessage();
            }


        }
        if (params[0].equals("removedata")) {
            String regurl = "https://smarttutorial.000webhostapp.com/removedata.php";
            try {
                URL url = new URL(regurl);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("POST");
                huc.connect();
                String data=URLEncoder.encode("regno")+"="+URLEncoder.encode(params[1])+"&"
                        +URLEncoder.encode("area")+"="+URLEncoder.encode(params[2]);
                OutputStream os=huc.getOutputStream();
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
                bw.write(data);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = huc.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                res = br.readLine();


            } catch (MalformedURLException e) {
                res ="1"+ e.getMessage();

            } catch (FileNotFoundException e) {
                res = "2"+e.getMessage();
            } catch (Exception e) {
                res = "3"+e.getMessage();
            }


        }
        if (params[0].equals("fetchdata")) {
            String regurl = "https://smarttutorial.000webhostapp.com/fetch.php";
            try {
                URL url = new URL(regurl);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("POST");
                huc.connect();
                String data=URLEncoder.encode("area")+"="+URLEncoder.encode(params[1]);

                OutputStream os=huc.getOutputStream();
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
                bw.write(data);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = huc.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                res = br.readLine();


            } catch (MalformedURLException e) {
                res ="1"+ e.getMessage();

            } catch (FileNotFoundException e) {
                res = "2"+e.getMessage();
            } catch (Exception e) {
                res = "3"+e.getMessage();
            }


        }
        return res;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String res) {

        r1.onProgressFinish(res,c);



    }


}
