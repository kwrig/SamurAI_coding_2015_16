package kwrig.io;

import kwrig.General;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kwrig on 2016/01/26.
 */
public class FileIO {


    public FileIO(){
    }

    public void output(String file_name , String data){
        File file = new File(file_name);
        try {
            FileWriter filewriter = new FileWriter(file, true);
            filewriter.write(data);
            filewriter.write(General.crlf);
            filewriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}
