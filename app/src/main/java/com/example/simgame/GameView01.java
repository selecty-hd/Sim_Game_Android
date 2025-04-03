package com.example.simgame;

import static androidx.core.content.ContextCompat.getExternalFilesDirs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import java.io.File;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.view.MotionEvent;
import android.widget.Toast;

public class GameView01 extends View {
    Context context;
    Paint paint;
    File stragedirs;
    ArrayList<String> mapstring=null;
    char mapp[][];
    int xmax;
    int ymax;
    ArrayList<String> mapdata ;
    int basex;
    int basey;
    int flag=0;
    float drag;
    float ndrag;
    float dd;
    Bitmap monster[];
    String monsterpath;
    File mosterfilebuffer;
    InputStream inputStream=null;


    public GameView01(Context context) throws FileNotFoundException {
        super(context);
        this.context=context;
        Resources resources = context.getResources();                     //リソース取得
        Paint paint = new Paint();//表示用Paint
        ///　ストレージのPath取得＆表示
        stragedirs = context.getExternalFilesDir(null);
        //if (stragedirs != null) {
        //}
        String strage = stragedirs.getAbsolutePath()+"/"+"map001.dat";
        StrageFileAccess map = new StrageFileAccess(strage);
        mapdata = map.stragefile_text_read();
        ymax = mapdata.size();
        xmax = mapdata.get(0).length();
        mapp = new char[ymax][xmax];
        for(int i=0;i<ymax;i++){
            String buffer = mapdata.get(i);
            char buffer01[];
            for(int j=0;j<xmax;j++){
                buffer01 = buffer.toCharArray();
                mapp[j][i]=buffer01[j];
            }
        }

        monster = new Bitmap[3];
        for(int i=1;i<3;i++){
            stragedirs = context.getExternalFilesDir(null);
            //if (stragedirs != null) {
            //}

            //monsterpath = context.getFilesDir().getPath()+"/"+"mos00"+i+".png";
            monsterpath = stragedirs.getAbsolutePath()+"/"+"mos00"+i+".png";
            mosterfilebuffer = new File(monsterpath);
            ///monsterpath = "file:/"+context.getDataDir()+"/"+"mos00"+i+".png";
            ///monsterpath = stragedirs.getAbsolutePath()+"/"+"mos00"+i+".png";
            //getExternalFilesDirs
            try {
                inputStream = new FileInputStream(mosterfilebuffer);
                monster[i] = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        /// ///////////////////////////////////////////////////////////////
        ///
        basex = 0;
        basey = 0;
        /// //////////////////////////////////////////////////////////////
    }
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Paint paint03 = new Paint();
        Paint paint04 = new Paint();
        Paint paint05 = new Paint();

        //canvas.drawColor(Color.GREEN);
        Paint paint01=new Paint(Color.RED);
        ///canvas.drawBitmap(monster[0],300,300,paint);
        paint01.setTextSize(100);
        Paint paint02=new Paint(Color.RED);
        paint02.setTextSize(30);

        paint03.setColor(Color.RED);
        paint04.setColor(Color.BLUE);
        paint05.setColor(Color.WHITE);


        int lin=5;
        for(int j=0;j<xmax;j++){
        ///    for(int j=basey;j<9;j++){
            if(j%2==0){
                for(int i=0;i<ymax;i++) {
                ///    for(int i=basex;i<9;i++) {
                    if (i % 2 == 0) {
                            ///if(((i+basey)<ymax)&&((i+basey)>=0)){

                        if(((i+basex)<xmax)&&((i+basex)>=0)&&((j+basey)<ymax)&&((j+basey)>=0)){
                            canvas.drawRect(250 * i, 250 * j, 250 * (i + 1), 250 * (j + 1), paint05);
                            canvas.drawRect((250 * i)+lin, (250 * j)+lin, (250 * (i + 1))-lin, (250 * (j + 1))-lin, paint03);
                            canvas.drawText(""+mapp[j+basey][i+basex],250*i+100,250*j+200,paint01);
                        }else if(((i+basex)>=xmax)||((i+basex)<0)){
                            ///canvas.drawText(""+mapp[j+basey][i],250*i+100,250*j+200,paint01);
                        }else if(((j+basey)>=ymax)||((j+basey)<0)){
                            ///canvas.drawText(""+mapp[j][i+basex],250*i+100,250*j+200,paint01);
                        }
                    } else {
                        ///if(((i+basey)<ymax)&&((i+basey)>=0)){
                        if(((i+basex)<xmax)&&((i+basex)>=0)&&((j+basey)<ymax)&&((j+basey)>=0)){
                            canvas.drawRect(250 * i, 250 * j, 250 * (i + 1), 250 * (j + 1), paint05);
                            canvas.drawRect(250 * i + lin, 250 * j + lin, 250 * (i + 1) - lin, 250 * (j + 1) - lin, paint04);
                            canvas.drawText("" + mapp[j + basey][i+basex], 250 * i + 100, 250 * j + 200, paint01);
                        }else if(((i+basex)>=xmax)||((i+basex)<0)){
                            ///canvas.drawText("" + mapp[j+basey][i], 250 * i + 100, 250 * j + 200, paint01);
                        }else if(((j+basey)>=ymax)||((j+basey)<0)){
                            ///canvas.drawText("" + mapp[j][i+basey], 250 * i + 100, 250 * j + 200, paint01);
                        }
                   }
                }
            }else{
                for(int i=0;i<ymax;i++) {
                    if (i % 2 == 0) {
                        ///if(((i+basey)<ymax)&&((i+basey)>=0)){
                        if(((i+basex)<xmax)&&((i+basex)>=0)&&((j+basey)<ymax)&&((j+basey)>=0)){
                            canvas.drawRect(250 * i + 125, 250 * j, 250 * (i + 1) + 125, 250 * (j + 1), paint05);
                            canvas.drawRect(250 * i + 125+lin, 250 * j+lin, 250 * (i + 1) + 125-lin, 250 * (j + 1)-lin, paint03);
                            canvas.drawText("" + mapp[j + basey][i+basex], 250 * i + 100 + 125, 250 * j + 200, paint01);
                        }else if(((i+basex)>=xmax)||((i+basex)<0)){
                            ///canvas.drawText("" + mapp[j+basey][i], 250 * i + 100 + 125, 250 * j + 200, paint01);
                        }else if(((j+basey)>=ymax)||((j+basey)<0)){
                            ///canvas.drawText("" + mapp[j][i+basex], 250 * i + 100 + 125, 250 * j + 200, paint01);
                        }
                    } else {
                        if(((i+basex)<xmax)&&((i+basex)>=0)&&((j+basey)<ymax)&&((j+basey)>=0)){
                            canvas.drawRect(250 * i + 125, 250 * j, 250 * (i + 1) + 125, 250 * (j + 1), paint05);
                            canvas.drawRect(250 * i + 125+lin, 250 * j+lin, 250 * (i + 1) + 125-lin, 250 * (j + 1)-lin, paint04);
                            canvas.drawText("" + mapp[j + basey][i+basex], 250 * i + 100 + 125, 250 * j + 200, paint01);
                        }else if(((i+basex)>=xmax)||((i+basex)<0)){
                            ///canvas.drawText("" + mapp[j+basey][i], 250 * i + 100 + 125, 250 * j + 200, paint01);
                        }else if(((j+basey)>=ymax)||((j+basey)<0)){
                            ///canvas.drawText("" + mapp[j][i+basex], 250 * i + 100 + 125, 250 * j + 200, paint01);
                        }
                    }
                }
            }
        }
        ///for(int i=0;i<ymax;i++){
        ///    for(int k=0;k<xmax;k++){
                ///canvas.drawText(""+i,100*i,100*j,paint01);
        ///        canvas.drawText(""+mapp[k][i],k*100+20,i*100+100,paint01);
        ///    }
        ///}
        canvas.drawText(monsterpath,10,1000,paint02);
///        canvas.drawBitmap(monster[1],10,1100,null);
            Rect retct01 = new Rect(0,0,100,100);///90 68
            Rect retct02 = new Rect(10,1100,68+10+200,90+1100+200);///90 68
            Rect retct03 = new Rect(10,1300,68+10+200,90+1300+200);///90 68
        canvas.drawBitmap(monster[1],retct01,retct02,null);
            canvas.drawBitmap(monster[2],retct01,retct03,null);


        canvas.drawText(""+(int)dd,200,200,paint01);
        canvas.drawText(""+(int)drag,200,300,paint01);

        canvas.drawText(""+(int)ndrag,200,400,paint01);


        //paint.setTextSize(50);
        //canvas.drawText("AAA AAA",10,10,paint);
    }

    public boolean onTouchEvent(MotionEvent event){
            ///drag=event.getX();
            ///int flag;
            ///Toast toast = Toast.makeText(this.getContext(), "aaaaaaaaaaa", Toast.LENGTH_LONG);
            ////toast.show();
            ///////////////////////////////////////////////////////
            //画面タッチの検出
            switch (event.getActionMasked()) {
                case (MotionEvent.ACTION_DOWN):
                   /* if (flag == 0) {
                        basex++;
                        if (basex >= 9) {
                            flag = 1;
                        }
                    } else if (flag == 1) {
                        basex--;
                        if (basex <= 0) {
                            flag = 0;
                        }
                    }*/
                    dd = event.getX();
                    break;
                case (MotionEvent.ACTION_UP):
                    ndrag=dd-drag;
                    if(ndrag>0){
                        if (basex < 9) {
                            basex++;
                        }
                    }else if(ndrag<0) {
                        if (basex > 0) {
                            basex--;
                        }
                    }
                    break;
                case (MotionEvent.ACTION_MOVE):
                    drag = event.getX();
                    ///drag=10.0f;
                    break;
            }

        ///ndrag=drag;
        invalidate();


        return true;
    }


}
