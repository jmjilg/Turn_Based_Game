package com.example.client;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import datapackage.Data;
import datapackage.Info;


public class MainActivity extends AppCompatActivity {

    TextView txt_info;
    ImageView img_card;
    Button btn_select, btn_end;
    Info info;
    Utils u;
    Data d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        u = new Utils();

        Data dd = new Data();
        dd.setAttack(0);

        info = new Info();
        info.init();

        txt_info = (TextView) findViewById(R.id.txt_info);
        img_card = (ImageView) findViewById(R.id.img_card);
        btn_select = (Button) findViewById(R.id.btn_select);
        btn_end = (Button)findViewById(R.id.btn_end);

        txt_info.setText(u.setInfo(info, dd));

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), SelectCard.class);

                trans.launch(i);
            }
        });


    }

    ActivityResultLauncher<Intent> trans = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // 공격
                if(result.getResultCode() == 1) {
                    try {
                        Game ga = new Game();
                        Thread thr = new Thread(ga);
                        thr.start();
                        thr.join();
                        txt_info.setText(u.setInfo(info, d));
                        info.setHP(info.getHP() - d.getAttack());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                if(result.getResultCode() == 2){
                    //ToDo
                }
            }
    );


    class Game implements Runnable {

        @Override
        public void run() {
            try {
                Socket socket = new Socket("172.16.37.210", 5000);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                Data data = new Data();
                data.setAttack(5);
                out.writeObject(data);

                d = (Data) in.readObject();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}

