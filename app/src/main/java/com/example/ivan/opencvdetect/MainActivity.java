package com.example.ivan.opencvdetect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
/*
    BY IVAN GAVRILOV
 */

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, AdapterView.OnItemSelectedListener,CompoundButton.OnCheckedChangeListener  {
    JavaCameraView JCam;
    Mat mRgba,ya;
    TextView t4,t5;
    boolean CheckFileDialog = false;
    String suda = "";
    String filename = "zazse_path_to_haar_cascades.txt";
    static {
        System.loadLibrary("MyLibs");
    }
    boolean Cascades = false;
    BaseLoaderCallback BLoader = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status) {
                case BaseLoaderCallback.SUCCESS: {
                    JCam.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };
    Integer x;
    Integer y;
    boolean cam1 = false , cam2 = false, corners = false;
    int type = 0;
    String[] data = {"1920x1080","1280x960", "1280x720", "720x480", "640x480", "480x320"};
    String[] eto = {"Frontal_Face_lbp", "Full_Body_haar", "Eye_Haar", "Frontal_cat_face_haar", "Frontal_Face_haar","Load_User_Cascade"};
    ToggleButton b1,b2,b3,b4,b5,b6;
    Switch c;
    boolean Color = false;
    TextView t1, t2,t3;
    boolean HSV = false, Eff = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        File f = new File(getFilesDir() + "/" + filename);
        if(!(f.exists())) {
            filedialog();
        } else {
            try {
                String str;
                BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(filename)));
                while ((str = br.readLine()) != null) {
                    suda+=str;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("ZDAROVA", "NY YA");
        JCam = (JavaCameraView)findViewById(R.id.view);
        b4 = (ToggleButton)findViewById(R.id.toggleButton2);
        b6 = (ToggleButton)findViewById(R.id.toggleButton4);
        b6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) corners = true;
                else corners = false;
            }
        });
        b4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    showDialog();
                    Color = true;
                } else {
                    Color = false;
                }
            }
        });
        c = (Switch)findViewById(R.id.switch1);
        if(c != null) c.setOnCheckedChangeListener(this);
        JCam.setMaxFrameSize(600,600);
        JCam.enableFpsMeter();
        JCam.setVisibility(View.VISIBLE);
        b1 = (ToggleButton)findViewById(R.id.toggleButton);
        t1 = (TextView)findViewById(R.id.textView2);
        b3 = (ToggleButton)findViewById(R.id.toggleButton3);
        t3 = (TextView)findViewById(R.id.textView4);
        b1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    t1.setText("HSV");
                    HSV = true;
                } else {
                    t1.setText("Normal");
                    HSV = false;
                }
            }
        });
        b3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Eff = true;
                    t3.setText("Gray");
                } else {
                    t3.setText("Normal");
                    Eff = false;
                }
            }
        });
        JCam.setCvCameraViewListener(this);
        OpencvNative.loadCascades(1, suda);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eto);
        Spinner spinner1 = (Spinner)findViewById(R.id.spinner3);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setSelection(3);
        spinner1.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(this);
        b5 = (ToggleButton)findViewById(R.id.toggleButton5);
        b5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cam1 = true;
                    camshiftdialog();
                } else {
                    cam1 = false;
                    cam2 = false;
                    cam3 = false;
                }
            }
        });
    }
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) { Cascades = true;c.setText("CASCADES-ON");if(CheckFileDialog) Toast.makeText(getApplicationContext(), "Cascade does not exist.", Toast.LENGTH_LONG).show();}
            else {Cascades = false;c.setText("CASCADES-OFF");}
    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        Spinner spinner = (Spinner) parent;
        JCam.disableView();
        JCam.disableFpsMeter();
        if(spinner.getId() == R.id.spinner2)
        {
                    switch (pos) {
                        case 0: {
                            x = 1920;
                            y = 1080;
                            break;
                        }
                        case 1: {
                            x = 1280;
                            y = 960;
                            break;
                        }
                        case 2: {
                            x = 1280;
                            y = 720;
                            break;
                        }
                        case 3: {
                            x = 720;
                            y = 480;
                            break;
                        }
                        case 4: {
                            x = 640;
                            y = 480;
                            break;
                        }
                        case 5: {
                            x = 480;
                            y = 320;
                            break;
                        }
                    }
            JCam.setMaxFrameSize(x, y);
        }
        else if(spinner.getId() == R.id.spinner3)
        {
            if(pos == 5) {
                showcascDialog();
            } else if((OpencvNative.loadCascades(pos,suda)) == -1) {
                CheckFileDialog = true;
            }
        }
        JCam.enableView();
        JCam.enableFpsMeter();

    }
    //DIALOGS
    int x1,x2,y1,y2;
    int xm1, ym1, xm2, ym2;
    void filedialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enter the path to the directory where the haar cascades are located");
        View dial = getLayoutInflater().inflate(R.layout.filedialog, null);
        dialog.setView(dial);
        final EditText t;
        t = (EditText)dial.findViewById(R.id.editText);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                suda = t.getText().toString();
                if ((OpencvNative.loadCascades(1, suda)) == -1) CheckFileDialog = true;
                else {
                    try {
                        // отрываем поток для записи
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(filename, MODE_PRIVATE)));
                        // пишем данные
                        bw.write(suda);
                        // закрываем поток
                        bw.close();
                        Log.d("ZDAROVA", "Файл записан");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CheckFileDialog = true;
            }
        });
        dialog.create();
        dialog.show();
    }
    void camshiftdialog() {
        x1 = x.intValue();
        x2 = x.intValue();
        y1 = y.intValue();
        y2 = y.intValue();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Выберите область захвата");
        View linearlayout = getLayoutInflater().inflate(R.layout.camdialog, null);
        dialog.setView(linearlayout);
        final TextView t;
        SeekBar s1,s2,s3;
        s1 = (SeekBar)linearlayout.findViewById(R.id.seekBar);
        s2 = (SeekBar)linearlayout.findViewById(R.id.seekBar2);
        s3 = (SeekBar)linearlayout.findViewById(R.id.seekBar3);
        t = (TextView)linearlayout.findViewById(R.id.textView3);
        int max = 200*x/720;
        s1.setMax(max);
        s2.setMax(max);
        s3.setMax(max);
        s1.setProgress(max/2);
        s2.setProgress(max/2);
        s3.setProgress(max/2);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {x1 = x.intValue()/2 - progress; x2 = x.intValue()/2+progress; y1 = y.intValue()/2-progress; y2 = y.intValue()/2 +progress;xm1 = x1;xm2 = x2;ym1 = y1;ym2 = y2;t.setText(" X1= "+x1+" Y1= "+y1+" X2= "+x2+" Y2= "+y2);}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        s2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {if(x2 < x && x2 > 0 && x1 < x && x1 > 0) x2 = xm2+progress;x1 = xm1 - progress;}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        s3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {if(y2 < y && y2 > 0 && y1 < y && y1 > 0)y2 = ym2 + progress; y1 = ym1 - progress;}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        JCam = null;
        JCam = (JavaCameraView)linearlayout.findViewById(R.id.view1);
        JCam.setVisibility(View.VISIBLE);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                JCam = (JavaCameraView)findViewById(R.id.view);
                JCam.setVisibility(View.VISIBLE);
                cam1 = false;
                cam2 = true;
            }
        });
        dialog.create();
        dialog.show();
    }
    public void showcascDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Write path to cascade");
        View linearlayout = getLayoutInflater().inflate(R.layout.dialog1, null);
        dialog.setView(linearlayout);
        EditText t = (EditText)linearlayout.findViewById(R.id.editText);
        final String a = t1.getText().toString();
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OpencvNative.loadUserCascade(a);
            }
        });
        dialog.create();
        dialog.show();
    }
    int h1 = 0,sc1 = 0,v1 = 0,h2 = 0,sc2 = 0,v2 = 0;
    public void showDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Выберите цветовой диапазон:");
        View linearlayout = getLayoutInflater().inflate(R.layout.dialog, null);
        dialog.setView(linearlayout);
        SeekBar s1,s2,s3,s4,s5,s6;
        final TextView t1c,t2c,t3c,t4c,t5c,t6c;
        t1c = (TextView)linearlayout.findViewById(R.id.textView21);
        t2c = (TextView)linearlayout.findViewById(R.id.textView22);
        t3c = (TextView)linearlayout.findViewById(R.id.textView17);
        t4c = (TextView)linearlayout.findViewById(R.id.textView18);
        t5c = (TextView)linearlayout.findViewById(R.id.textView19);
        t6c = (TextView)linearlayout.findViewById(R.id.textView20);
        s1 = (SeekBar)linearlayout.findViewById(R.id.seekBar16);
        s2 = (SeekBar)linearlayout.findViewById(R.id.seekBar17);
        s3 = (SeekBar)linearlayout.findViewById(R.id.seekBar12);
        s4 = (SeekBar)linearlayout.findViewById(R.id.seekBar13);
        s5 = (SeekBar)linearlayout.findViewById(R.id.seekBar14);
        s6 = (SeekBar)linearlayout.findViewById(R.id.seekBar15);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {h1 = (progress*255)/100;t1c.setText("H1 - " + Integer.toString(h1));}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        s2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {sc1 = (progress*255)/100;t2c.setText("S1 - " +Integer.toString(sc1));}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        s3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {v1 = (progress*255)/100;t3c.setText("V1 - " + Integer.toString(v1));}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        s4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {h2 = (progress*255)/100;t4c.setText("H2 - " + Integer.toString(h2));}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        s5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {sc2 = (progress*255)/100;t5c.setText("S2 - " + Integer.toString(sc2));}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        s6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {v2 = (progress*255)/100;t6c.setText("V2 - " + Integer.toString(v2));}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Color = true;
            }
        });
        dialog.create();
        dialog.show();
    }
    //DIALOGS
    //CAMS
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(width, height, CvType.CV_8UC4);
        ya = new Mat(width,height, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }
    protected void onPause() {
        super.onPause();
        if(JCam != null) JCam.disableView();
    }
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()) {
            Log.d("My", "OPENCV LOADED");
            BLoader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.d("My", "OPENCV DON'T LOADED");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, BLoader);
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        if(JCam != null) JCam.disableView();
    }
    boolean cam3 = false;
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        //System.out.println(x1 + " " + y1 + " " + " " + x2 + " " + y2);
        if(Cascades && !CheckFileDialog)
            OpencvNative.detect(mRgba.getNativeObjAddr());
        if(Eff)
            Imgproc.cvtColor(mRgba,mRgba,Imgproc.COLOR_BGR2GRAY);
        else if(HSV)
            OpencvNative.cHSV(mRgba.getNativeObjAddr());
        if(Color)
            OpencvNative.fColor(mRgba.getNativeObjAddr(),h1,sc1,v1,h2,sc2,v2);
        if(cam1)
            OpencvNative.rect(mRgba.getNativeObjAddr(),x1,y1, x2, y2);
        if(cam2 && x1 < x && x1 > 0 && x2 < x && x2 > 0 && y1 < y && y1 > 0 && y2 < y && y2 > 0 && !Eff && x1-x2 != 0 && y1-y2!=0) {
            OpencvNative.MakeHist(mRgba.getNativeObjAddr(), x1, y1, x2-x1, y2-y1);
            cam2 = false;
            cam3 = true;
        }
        if(cam3)
            OpencvNative.CamShift(mRgba.getNativeObjAddr());
        if(corners)
            OpencvNative.Harris(mRgba.getNativeObjAddr());
        return mRgba;
    }
    //CAMS
}
