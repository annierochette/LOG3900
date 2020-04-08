package com.example.polydraw;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog; //https://codinginflow.com/tutorials/android/ambilwarna-color-picker-dialog
//seekbar https://www.tutlane.com/tutorial/android/android-seekbar-with-examples

public class DrawingActivity extends AppCompatActivity {
    DrawingCanvas drawingCanvas;
    Button eraseButton;
    Button drawButton;
    Button color;
    Button capStyle;
    SeekBar seekBar;
    TextView seekBarText;
    ImageButton download;
    private Button backButton;
    private ImageView chatButton;

    ConstraintLayout mLayout;
    int mDefaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        drawingCanvas = new DrawingCanvas(this,null);
        setContentView(R.layout.activity_canvas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        initializeObject();
        eventListeners();

    }

    private void initializeObject(){
        drawingCanvas = (DrawingCanvas) findViewById(R.id.drawing);
        eraseButton = (Button) findViewById(R.id.eraser);
        drawButton = (Button) findViewById(R.id.paint);
        color = (Button) findViewById(R.id.colorButton);
        capStyle = (Button) findViewById(R.id.capButton);
        mLayout = (ConstraintLayout) findViewById(R.id.layout);
        mDefaultColor = ContextCompat.getColor(DrawingActivity.this, R.color.colorPrimary);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarText = (TextView) findViewById(R.id.sbTextView);
        download = (ImageButton) findViewById(R.id.download);
        backButton = (Button) findViewById(R.id.backButton);
        chatButton = (ImageView) findViewById(R.id.chatButton);

    }

    private void eventListeners() {
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(true);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.selected_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));
                capStyle.setBackground(getDrawable(R.drawable.round_button));

            }
        });
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.selected_button));
                capStyle.setBackground(getDrawable(R.drawable.round_button));
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.selected_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));
                capStyle.setBackground(getDrawable(R.drawable.round_button));
                openColorPicker();

            }
        });

        capStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));
                capStyle.setBackground(getDrawable(R.drawable.selected_button));
                openCapOptions();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                drawingCanvas.setWidth(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarText.setText(pval + "/" + seekBar.getMax());
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File folder = getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE);
                boolean success = false;

                if (!folder.exists()) {
                    success = folder.mkdirs();
                }

                System.out.println(success + " folder");

                Random random = new Random();
                int randomInteger = random.nextInt();

                File file = new File(folder, "drawing" + randomInteger + ".txt");

                if (!file.exists()) {
                    try {
                        success = file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(success + " file");

                FileOutputStream ostream = null;
                String content = "Content of the file";

                try {
                    ostream = new FileOutputStream(file);

                    byte[] contentBytes = content.getBytes();

                    ostream.write(contentBytes);
                    ostream.flush();
                    ostream.close();

//                    System.out.println(ostream);
//                    View targetView = drawingCanvas;
//
//                    Bitmap well = drawingCanvas.getBitmap();
//                    Bitmap save = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
//
//                    Paint paint = new Paint();
//                    paint.setColor(Color.WHITE);
//
//                    Canvas now = new Canvas(save);
//                    now.drawRect(new Rect(0, 0, 320, 480), paint);
//                    now.drawBitmap(well, new Rect(0, 0, well.getWidth(), well.getHeight()), new Rect(0, 0, 320, 480), null);
//
//                    save.compress(Bitmap.CompressFormat.PNG, 100, ostream);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Null error", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "IO error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPlayMenu();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                drawingCanvas.setDrawingColor(mDefaultColor);

            }
        });
        colorPicker.show();
    }

    //https://www.javatpoint.com/android-popup-menu-example
    public void openCapOptions(){
        PopupMenu popup = new PopupMenu(DrawingActivity.this, capStyle);
        popup.getMenuInflater().inflate(R.menu.cap_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(DrawingActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                drawingCanvas.setPencilTip(item.getTitle().toString());
                return true;
            }
        });
        popup.show();

    }

    public void backToPlayMenu() {
        Intent intent = new Intent(this, PlayMenu.class);
        startActivity(intent);
    }

    public void openChat(){
        Intent intent = new Intent(this, ChatBoxActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() { }
}

