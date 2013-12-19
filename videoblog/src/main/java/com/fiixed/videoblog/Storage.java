package com.fiixed.videoblog;

import android.content.Context;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by abell on 12/15/13.
 */
 public class Storage {

    //
    // Singleton pattern here:
    //
    private static Storage storageRef;
    private Storage(){

    }
    public static Storage getInstance()
    {
        if (storageRef == null){
            storageRef = new Storage();
        }
        return storageRef;
    }

    //
    // Serialization here
    //

    static String DATA_FILE_ARRAY = "data_file_array";


    //this method is private so we can control what gets saved.
    private static boolean saveMyData(Context context, HashMap<UUID,VideoData> myData) {
        try {
            FileOutputStream fos = context.openFileOutput(DATA_FILE_ARRAY, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(myData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    //used to add a new video to the file
    public static void add(Context context , VideoData videoData){

        HashMap<UUID,VideoData> myData;

        try{
            myData = getMyData(context);
            //if it doesnt exist, create one.
            if(myData == null){
                myData = new HashMap<UUID, VideoData>();
            }

            //This try catch is to replace the old alarm name if needed.
            try{
                myData.put(videoData.getId(), videoData);
            }catch (Exception e){
//                myData.remove(videoData.alarmName);
//                myData.put(videoData.alarmName,videoData);
            }

            saveMyData(context,myData);

        }catch(Exception e){

        }
    }


    //will delete a video from the file
    public static void remove(Context context , VideoData videoData){
        HashMap<UUID,VideoData> myData;
        try{
            myData = getMyData(context);
            //if it doesnt exist, stop one.
            if(myData == null){
                return;
            }


            myData.remove(videoData);
            saveMyData(context,myData);
        }catch(Exception e){

        }
    }

    //used to get a list of all current videos.
    public static HashMap<UUID,VideoData> getMyData(Context context) {
        try {
            FileInputStream fis = context.openFileInput(DATA_FILE_ARRAY);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object readObject = is.readObject();
            is.close();

            if(readObject != null && readObject instanceof HashMap) {
                return (HashMap<UUID,VideoData>) readObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}