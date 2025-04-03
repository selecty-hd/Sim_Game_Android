package com.example.simgame;

import java.util.ArrayList;

public class Setting {
    ArrayList<String> mapfile;      ///Mapファイル名
    ArrayList<String> mapchipfile;  ///Mapchipファイル名
    ArrayList<String> unitfile;     ///Unitファイル名
    int set;
    int mapcount;                   ///Map数
    int chipcount;                  ///Mapchip数
    int unitcount;                  ///Unit数
    ////////////////////////////////////////////////////////
    /// コンストラクタ
    Setting(){};
    /////////////////////////////////////////////////////////
    /// Configファイルの行単位の情報から各データファイル名を生成する
    int fileSetting(ArrayList<String> stregefiles){
        for(int i=0;i<stregefiles.size();i++) {
            /// 設定ファイルから1行読み込み
            String buffer = new String(stregefiles.get(i));
            /// ファイルリストからスペース等、不要文字列を削除して戻す
            stregefiles.set(i,buffer.trim());
            /// 先頭が＃ならコメント行
            if(((stregefiles.get(i)).indexOf("#")!=-1)){
                /// コメントであれば、ファイルデータから抜く
                stregefiles.remove(i);
                i--;
                continue;
            }
            ////////////////////////////////////////////////////////////
            /// Map枚数の抽出
            if((buffer.indexOf("map="))!=-1){
                /// マップ件数を検出
                mapcount=Integer.getInteger(buffer.substring(5));
                /// Map設定行の削除
                stregefiles.remove(i);
                i--;
                continue;
            }else if((buffer.indexOf("chip="))!=-1){
                /// マップチップ件数を検出
                chipcount=Integer.getInteger(buffer.substring(6));
                /// Chip設定行の削除
                stregefiles.remove(i);
                i--;
                continue;
            }else if((buffer.indexOf("unit="))!=-1){
                /// ユニット件数を検出
                unitcount=Integer.getInteger(buffer.substring(6));
                /// Unit設定行の削除
                stregefiles.remove(i);
                i--;
                continue;
            }
        }
        for (int i=0;i<mapcount;i++){
            ///ArrayList<String> mapfile;
            StringBuffer mapbuffer=null;
            mapbuffer.append("map");
            mapbuffer.append(setNumber(i+1));
            mapbuffer.append(".dat");
            mapfile.set(i,mapbuffer.toString());
        }
        for (int i=0;i<chipcount;i++){
            ///ArrayList<String> mapchipfile;
            StringBuffer chipbuffer=null;
            chipbuffer.append("mapc");
            chipbuffer.append(setNumber(i+1));
            chipbuffer.append(".png");
            mapchipfile.set(i,chipbuffer.toString());
        }
        for (int i=0;i<unitcount;i++){
            ///ArrayList<String> unitfile;
            StringBuffer unitbuffer=null;
            unitbuffer.append("unit");
            unitbuffer.append(setNumber(i+1));
            unitbuffer.append(".png");
            unitfile.set(i,unitbuffer.toString());
        }
        /// //////////////////////////////////////////////////////
        ///
    return (1);
    }
    /// ///////////////////////////////////////////////////////////////
    /// 「000～999」番号生成
    String setNumber(int x){
        String retbuffer;
        if(x<10){
            retbuffer="00"+Integer.toString(x);
        }else if(x<100){
            retbuffer="0"+Integer.toString(x);
        }else{
            retbuffer=Integer.toString(x);
        }
        return (retbuffer);
    }


}
