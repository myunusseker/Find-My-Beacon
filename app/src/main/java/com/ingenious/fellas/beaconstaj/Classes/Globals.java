package com.ingenious.fellas.beaconstaj.Classes;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.ToneGenerator;
import android.util.Log;
import android.widget.TextView;

import com.ingenious.fellas.beaconstaj.Activities.BeaconFinderActivity;
import com.ingenious.fellas.beaconstaj.Activities.NewBeaconActivity;
import com.ingenious.fellas.beaconstaj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehmet on 09/06/16.
 */
public class Globals {
    public static String email, username, password ,namesurname;
    public static String TAG = "asdf", URL = "http://188.166.29.184/";
    public static int id;
    public static ArrayList<Beacon> myBeacons = new ArrayList<>();
    public static int whichActivity=0;
    public static List<Beacon> beaconsAround = new ArrayList<>();
    public static ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(whichActivity==1){
                Log.i(TAG, "Bluetooth device found\n");
                int rssi = result.getRssi();
                BluetoothDevice device = result.getDevice();
                Beacon newBeacon = new Beacon(device.getName(), device.getAddress(),rssi);
                Log.i(TAG, "rssi degisimi: " + rssi + " MAC: " + newBeacon.getAddress());

                boolean beaconExist = false;
                for (Beacon beacon : beaconsAround) {
                    if(beacon.getAddress().equalsIgnoreCase(newBeacon.getAddress())){
                        beacon.setRssi(newBeacon.getRssi());
                        beaconExist = true;
                        NewBeaconActivity.mAdapter.notifyDataSetChanged();
                    }
                }
                if(!beaconExist) {
                    Log.i(TAG, "Add yapmadan once");
                    if (!Globals.doesBeaconsExists(newBeacon.getAddress())){
                        beaconsAround.add(newBeacon);
                        NewBeaconActivity.mAdapter.notifyDataSetChanged();
                        new NewBeaconActivity.sendLocationTask().execute(newBeacon);
                    }
                }
            }
            else if (whichActivity == 2){
                Log.i("YUNUS", "Bluyo\n");
                int rssi = result.getRssi();
                String foundMac = result.getDevice().getAddress();
                if(foundMac.equalsIgnoreCase(BeaconFinderActivity.mac)){
                    Log.i("YUNUS", String.valueOf(rssi));

                    (BeaconFinderActivity.finderRssi).setText(String.valueOf(rssi));
                    BeaconFinderActivity.mBeacon.setRssi(rssi);
                    Log.i("YUNUS",BeaconFinderActivity.mBeacon.getDistance());
                    (BeaconFinderActivity.finderResult).setText(BeaconFinderActivity.mBeacon.getDistance());
                    BeaconFinderActivity.toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,rssi*10+1000);
                    if(rssi>-35){
                        (BeaconFinderActivity.finderResult).setText("You have found");
                    }
                }
            }
            else if (whichActivity == 3){
                Log.i(TAG, "Bluetooth device found\n");
                int rssi = result.getRssi();
                BluetoothDevice device = result.getDevice();
                Beacon newBeacon = new Beacon(device.getName(), device.getAddress(),rssi);
                Log.i(TAG, "rssi degisimi: " + rssi + " MAC: " + newBeacon.getAddress());

                boolean beaconExist = false;
                for (Beacon beacon : beaconsAround) {
                    if(beacon.getAddress().equalsIgnoreCase(newBeacon.getAddress())){
                        beacon.setRssi(newBeacon.getRssi());
                        beaconExist = true;
                    }
                }
                if(!beaconExist) {
                    Log.i(TAG, "Add yapmadan once");
                    beaconsAround.add(newBeacon);
                }
            }
        }
    };


    public static void initialize(Context applicationContext) {
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        email = sharedPref.getString("email","nullUser");
        username = sharedPref.getString("username","nullUser");
        password = sharedPref.getString("password","nullUser");
        namesurname = sharedPref.getString("namesurname","nullUser");
        id = sharedPref.getInt("id",-1);

    }


    public static boolean doesBeaconsExists(String mac){
        for (int i=0;i<myBeacons.size();i++)
            if(myBeacons.get(i).getAddress().equals(mac))
                return true;
        return false;
    }

    public static Beacon getBeacon(String mac)
    {
        for(int i=0;i<myBeacons.size();i++)
        {
            if(myBeacons.get(i).getAddress().equals(mac))
                return myBeacons.get(i);
        }
        return null;
    }

}
