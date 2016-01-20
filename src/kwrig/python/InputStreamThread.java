package kwrig.python;

/**
 * Created by kwrig on 15/08/04.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * InputStreamを読み込むスレッド
 */
public class InputStreamThread extends Thread {

    private BufferedReader br;

    private int readCounter = 0;

    private List<String> list = new ArrayList<String>();

    private boolean isError = false;

    /** コンストラクター */
    public InputStreamThread(InputStream is , boolean isError) {
        br = new BufferedReader(new InputStreamReader(is));
        this.isError = isError;
    }

    /** コンストラクター */
    public InputStreamThread(InputStream is, String charset) {
        try {
            br = new BufferedReader(new InputStreamReader(is, charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            for (;;) {
                String line = br.readLine();
                if (line == null) 	break;

                if(isError){
                    System.err.println(line);
                }else{
                    System.out.println(line);
                }

                list.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** 文字列取得 */
    public List<String> getStringList() {
        return list;
    }

    public String getNextString(){

        if(list.size() > readCounter){
            String ret = list.get(readCounter);
            readCounter++;
            return ret;
        }

        return null;

    }

    public void clear(){
        list.clear();
        readCounter = 0;
    }

}