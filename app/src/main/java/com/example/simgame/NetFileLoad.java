package com.example.simgame;

import android.os.Handler;
import android.os.Looper;
import android.text.PrecomputedText;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.core.os.HandlerCompat;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetFileLoad {
    ///  ////////////////////////////////////////////////
    /// ダウンロードファイルリスト（URL部分なし）
    public String file;
    /// ////////////////////////////////////////////////////
    /// ダウンロードファイル・サイズ
    public ArrayList<Integer> filesize;
    /// /////////////////////////////////////////////////////
    /// ダウンロードURL（ファイル名と接続）
    private String urlname;
    /// //////////////////////////////////////////////////////
    /// ストレージのPath名
    public String stragename;
    /// /////////////////////////////////////////////////////
    /// 表示用プログレスバー
    ProgressBar progressBar;
    /// //////////////////////////////////////////////////////
    /// 　ストレージアクセス用クラス
    StrageFileAccess strageFileAccess;
    /// ///////////////////////////////////////////////////////
    /// 終了状態
    TextView dummy;
    /// /////////////////////////////////////////////////
    /// コンストラクタで引数は
    /// ダウンロードするファイルリスト(ArrayList):URL抜き
    /// 表示用プログレスバー
    /// 結果通知用int
    public NetFileLoad( ProgressBar pbar, TextView dummy) {
        this.progressBar = pbar;                      ///表示用プログレスバー
        this.dummy = dummy;

    }
    /// /////////////////////////////////////////////////////
    /// ストレージPath設定
    ///     ストレージがあるか判定
    public boolean setStrageFilePath(String path) {
        this.stragename = path;
        StrageFileAccess strageFileAccess = new StrageFileAccess(stragename);
        return strageFileAccess.chk_stragefile();
    }
    /// ///////////////////////////////////////////////////
    /// 　ダウンロードするURLを設定
    void setdownloadURLFiles(String url) {
        this.urlname = url;
    }
    /// ////////////////////////////////////////////////////////////////////
    /// ダウンロードするファイル名を設定
    void  setFilename(String file){
        this.file=file;
    }
    /// ////////////////////////////////////////////////////////////////////
    /// 別タスク起動
    void start_task() {
        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        BackgroundTask backgroundTask = new BackgroundTask(handler, progressBar,dummy);

        /// ///////////////////////////////////////////////////////////////////
        ///別タスク用にURL、ファイルリスト、ストレージPath、ファイルサイズ、ダミーテキストを引き渡す
        backgroundTask.setURL(urlname);
        /// /////////////////////////////////////////////////////////////////////////////////////////
        backgroundTask.setfilesize(filesize);
        backgroundTask.setFile(file);
        ///////////////////////////////////////////////////////////////////////////
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(backgroundTask);
    }
    /// /////////////////////////////////////////////////////////////////////////////////////////
    /// 以下、別タスク起動部分  
    private class BackgroundTask implements Runnable{
        private final Handler _handler;
        /// ////////////////////////////////////////////////////////
        ///UI用プログレスバー
        ProgressBar progressBar;
        /// ///////////////////////////////////////////////////////////
        String file;
        /// /////////////////////////////////////////////////////////
        String url;
        ArrayList<Integer> filesize;
        /// ////////////////////////////////////////////////////////
        /// ダミーテキスト
        public TextView bkdammytextviwe;
        /// ////////////////////////////////////////////////////////////////////////
    public BackgroundTask(Handler handler,ProgressBar pro,TextView dummy){
        this._handler = handler;
        this.progressBar=pro;
        this.bkdammytextviwe=dummy;
    }
    /// /////////////////////////////////////////////////////////////////////////////////////////
    void setURL(String urlname){
        this.url=urlname;
    }
    /// /////////////////////////////////////////////////////////////////////////////////////////
    void setfilesize(ArrayList<Integer> filesize){
        this.filesize=filesize;
    }
        /// ///////////////////////////////////////////////////////////////////
    void setFile(String filename){
         this.file = filename;
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////
    /// URLで指定したファイルの存在をチェック
    ///  引数：URLでファイル指定
    ///  戻り値：存在する場合、ファイルサイズ
    ///          存在しない場合、-1
    int chk_download_file(String urlstring) {
        int ret = 0;
        URL downloadurl;
        HttpURLConnection url_connection = null;
        String downLoadFileName;
        int sumsize = 0;
        int worksize;
///        for (int i = 0; i < filelist.size(); i++) {
            downLoadFileName = (String) (url + file);
            try {
                downloadurl = new URL(downLoadFileName);                               //DownLoadするファイル名からURL生成
                URLConnection conn = downloadurl.openConnection();              //接続用URLConnectionを作成
                url_connection = (HttpURLConnection) conn;
                //URLConnectionからHttpURLConnectionを生成
                url_connection.connect();                                     //接続開始
                int response = url_connection.getResponseCode();
                //接続結果を取得
                if (response == HttpURLConnection.HTTP_OK) {              //接続がOKかチェック
                    ret = url_connection.getContentLength();                  //接続先のサイズを戻り値として取得
                    sumsize=0;

                } else {
                    return -1;                                           //正常に接続できない場合、-1を返す
                }
                url_connection.disconnect();                                      //接続を切断する
            } catch (IOException e) {
                e.printStackTrace();                                    //例外発生時
                if (url_connection != null) {                          //例外発生時、接続確立していた場合
                    url_connection.disconnect();                      //接続を切断する
                }
            }
            ///filesize.set(i,ret);
            ///sumsize = sumsize + ret;

        return sumsize;
    }
    /// //////////////////////////////////////////////////////////////////////////
    @WorkerThread
    @Override
    public  void run(){
///        for(int k = 0;k<filelist.size(); k++){
///            String urlfilename = url + filelist.get(k);
///            int filesize = chk_download_file(urlfilename);
///        }
        //////////////////////////////////////////////////////
        ///新位置（要動作確認）
        /// ダウンロード開始
        /// ストレージを設定
        strageFileAccess = new StrageFileAccess(stragename);
        //④DownLoad処理
        //指定された全ファイルのDownLoad処理
        int byte_count_all = 0;                             ///DownLoadしたﾊﾞｲﾄ数
        int byte_count_sum = 0;
        ///            for (int i = 0; i < filelist.size(); i++) {
        ///                 byte_count_sum = byte_count_sum + filesize.get(i);
        ///            }
        //dummy.setText(filelist.get(0));
        InputStream inputstream ;                 ///DownLoadﾌｧｲﾙのInputStream
        BufferedInputStream buff_inputstream;    ///DownLoadﾌｧｲﾙのBufferedInputStream
        FileOutputStream file_outputStream;      ///ｽﾄﾚｰｼﾞへのFileOutputStream
        HttpURLConnection url_connection;        ///DownLoad用HttpURLConnection
        //for (int i = 0; i < filelist.size(); i++) {               ///DownLoad用ループ（ﾌｧｲﾙ単位）
            dummy.setText(">>>"+">>>");
            int byte_count = 0;                             ///DownLoadした途中のﾊﾞｲﾄ数
            inputstream = null;                 ///DownLoadﾌｧｲﾙのInputStream
            buff_inputstream = null;    ///DownLoadﾌｧｲﾙのBufferedInputStream
            file_outputStream = null;      ///ｽﾄﾚｰｼﾞへのFileOutputStream
            url_connection = null;        ///DownLoad用HttpURLConnection
            try {
                byte[] buffer = new byte[1024];                             ///DownLoad用バッファ
                URL url = new URL(urlname + file);            ///DownLoadﾌｧｲﾙ用URL
                //DownLoad用URLから、HttpURLConnectionを生成
                url_connection = (HttpURLConnection) url.openConnection();
                url_connection.connect();                                    ///DownLoadﾌｧｲﾙへ接続
                inputstream = url_connection.getInputStream();               ///DownLoad用InputStream生成
                ///Max1024ﾊﾞｲﾄのBufferedInputStreamにInputStreamを接続
                buff_inputstream = new BufferedInputStream(inputstream, 1024);
                ///ストレージ上にFileOutputStreamとして出力先ﾌｧｲﾙを生成
                file_outputStream = new FileOutputStream(stragename + "/" + file);
                int len;
                ///ファイル転送ループ
                while ((len = buff_inputstream.read(buffer)) != -1) {         ///DownLoad用InputStreamからBufferedInputStreamにRead
                    file_outputStream.write(buffer, 0, len);            ///BufferedInputStreamからFileOutputStreamにWrite
                    byte_count += len;
                    ///progressBar.setProgress((int) (100 * (byte_count_all + byte_count) / byte_count_sum));    //定期的に呼ぶ処理（ダイアログ更新）
                }
                ///byte_count_all += filesize.get(i);                                  ///DownLoad済みバイト数更新
                ///progressBar.setProgress((int) (100 * byte_count_all / byte_count_sum));    ///定期的に呼ぶ処理（ダイアログ更新）
                ///1ﾌｧｲﾙDownLoad終了後に一旦、接続を切る
                file_outputStream.close();
                buff_inputstream.close();
                file_outputStream.flush();
                url_connection.disconnect();

                ///ﾌｧｲﾙｻｲｽﾞが小さい場合、ダイアログが瞬間で消えるので、時間を取る

            } catch (IOException e) {
                try {
                    file_outputStream.flush();
                    file_outputStream.close();
                    buff_inputstream.close();
                    url_connection.disconnect();
                } catch (IOException e1) {
                }
            }
///
///                    } catch (IOException e) {
///                        throw new RuntimeException(e);
///                    }


///            ArrayList<String> read_data = new ArrayList<String>();

///            if(urlname==null){
///                return null;
///            }
///            if(stragename==null){
///                return null;
///            }


        dummy.setText("__"+file+"__");
        //////////////////////////////////////////////////////
        PostExecutor postExecutor = new PostExecutor();
        postExecutor.setProgress(progressBar);
        postExecutor.starter();
        _handler.post(postExecutor);
        Log.i("Async_BackgroundTask","ここに非同期処理");

    }
    private  class PostExecutor implements  Runnable{
        ////////////////////////////////////////////////////
        private ProgressBar progressBar;    ///表示用プログレスバー
        private int barMax;                 ///プログレスバー最大値
        private int barParam;               ///プログレスバー現在値
        public  int output;
        /// //////////////////////////////////////////////////
        /// ダウンロード進捗表示の設定
        public void setProgress(ProgressBar bar){
            this.progressBar=bar;
            //this.barMax=max;
            //this.barParam=parm;
        }
        /// //////////////////////////////////////////////////////////////
        ///
        public void starter(){
            output=0;
        }
        public int ending(){
            output=-1;
            return output;
        }
        /// //////////////////////////////////////////////////////////////
        @UiThread
        @Override
        public void run(){
            /// ダウンロード量が少ない場合、一瞬で終わるので、わざと500msec待つ
            Log.i("Async_PostExecutor","ここにUIスレッド");
            ///dummy.setText("end");
            ///progressBar.setProgress(5);
            ///////////////////////////////////////////////////////
            ///プログレスバー表示

            //int state=100*barParam/barMax;
            //progressBar.setProgress(10);
        }
    }

}
}
