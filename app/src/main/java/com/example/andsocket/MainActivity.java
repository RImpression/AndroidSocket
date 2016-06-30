package com.example.andsocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Runnable{


    @Bind(R.id.btnSend)
    Button btnSend;
    @Bind(R.id.etMessage)
    EditText etMessage;
    @Bind(R.id.lvChat)
    ListView lvChat;
    private static final String HOST = "183.35.235.200";
    private static final int PORT = 12345;
    private Socket socket = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private String content = "";
    private ArrayList<ChatMessage> messageList = null;
    private ChatAdapter chatAdapter;
    private ChatMessage message = null;
    private ChatMessage messageGET = null;
    private Object obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        messageList = new ArrayList<>();
        message = new ChatMessage();
        message.setUserName("RIM");
        message.setType("OUTPUT");


        new Thread() {
            @Override
            public void run() {
                initSocket();
            }
        }.start();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etMessage.getText().toString();
                etMessage.setText("");
                if(msg == "bye") {
                    sendMessage(message.getUserName() + " 离开讨论组");
                } else {
                    sendMessage(msg);
                }
            }
        });


        new Thread(MainActivity.this).start();



    }

    private void sendMessage(String msg) {
            message.setMsg(msg);
            //notifyListData(message);
            if (socket.isConnected()) {
                //Log.i("AppData","socket isConnected");
                if (!socket.isOutputShutdown()) {
                    try {
                        Log.i("AppData","发送消息 "+message.getMsg());
                        oos.writeObject(message);
                        oos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    private void notifyListData(ChatMessage message) {
        messageList.add(message);
        chatAdapter = new ChatAdapter(this,messageList);
        lvChat.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }

    private void initSocket() {
        try {
            socket = new Socket(HOST,PORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            Log.i("AppData","socket connect"+ois.toString());
        } catch (IOException e) {
            Log.i("AppData","socket is not connect");
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            sendMessage(message.getUserName() + " 加入讨论组");
            Log.i("AppData","run start");
            while (true) {
                if (socket.isConnected()) {
                    //Log.i("AppData","socket isConnected send");
                    if (!socket.isInputShutdown()) {
                        obj = ois.readObject();
                        if (obj != null) {
                            messageGET = (ChatMessage) obj;
                            //notifyListData(message);
                            Log.i("AppData",messageGET.getMsg());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
