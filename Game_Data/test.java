           //////////////////////////////////////////////////////////////////
            /// Configファイル名称設定・表示・ダウンロード準備
            ArrayList<String> configfile;
            configfile = new ArrayList<String>();
            configfile.add("config.txt");
            ///file_list_textview.setText(configfile.get(0));
            /// //////////////////////////////////////////////////////////////
            /// 別タスクの生成
            NetFileLoad sfa;
            /// コンストラクタにダウンロードファイル・プログレスバー・ダミーTextViewを渡す
            sfa = new NetFileLoad(configfile,downloadbar,file_list_textview);
            /// //////////////////////////////////////////////////////////////
            /// ストレージ名称取得・表示・別タスクへ送る
            String strage = stragedirs.getAbsolutePath()+"/";
            sfa.setStrageFilePath(strage);
            file_list_textview.setText(strage);
            /// ///////////////////////////////////////////////////////////////
            /// 別タスクにURLを送る
            sfa.setdownloadURLFiles(url_dir);
            //file_list_textview.setText(url_dir);
            /// ///////////////////////////////////////////////////////////////
            /// 別タスク起動
            sfa.start_task();
            //while (true){
            //    if(dummyText.getText().length()!=0)
            //        break;
            //}
            //file_list_textview.setText("end");
