
package com.example.andsocket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author RImpression
 */
public class AndroidServer {

    //定义相关的参数,端口,存储Socket连接的集合,ServerSocket对象
    //以及线程池
    private static final int PORT = 12345;
    private List<Socket> mList = new ArrayList<Socket>();
    private ServerSocket server = null;
    private ExecutorService myExecutorService = null;


    public static void main(String[] args) {
        new AndroidServer();
    }

    public AndroidServer()
    {
        try
        {
            server = new ServerSocket(PORT);
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            System.out.println("ip = "+ip);
            //创建线程池
            myExecutorService = Executors.newCachedThreadPool();
            System.out.println("服务端运行中...\n");
            Socket client = null;
            while(true)
            {
                client = server.accept();
                mList.add(client);
                myExecutorService.execute(new Service(client));
            }

        }catch(Exception e){e.printStackTrace();}
    }

    class Service implements Runnable
    {
        private Socket socket;
        //		private BufferedReader in = null;
        private String msg = "";
        private ObjectInputStream ois = null;
        private ObjectOutputStream oos = null;
        private ChatMessage message;
        Object obj = null;

        public Service(Socket socket) {
            this.socket = socket;
            try
            {
//                          in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                msg = "用户:" +this.socket.getInetAddress() + "~加入了聊天室"
                        +"当前在线人数:" +mList.size();
                System.out.println(msg);
                ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                oos = new ObjectOutputStream(socket.getOutputStream());
                this.sendmsg();
            }catch(IOException e){
                System.out.println("error info 0");
                e.printStackTrace();
            }
        }



        @Override
        public void run() {
            try{
                while(true)
                {
                    obj = ois.readObject();
                    if(obj != null)
                    {
                        message = (ChatMessage) obj;
                        System.out.println("message chat");

                        if(message.getMsg().equals("bye"))
                        {
                            //System.out.println("~~~~~~~~~~~~~");
                            mList.remove(socket);
                            ois.close();
                            msg = "用户:" + socket.getInetAddress()
                                    + "退出:" +"当前在线人数:"+mList.size();
                            System.out.println(msg);
                            socket.close();
                            this.sendmsg();
                            break;
                        }else{
                            msg = socket.getInetAddress() + "   说: " + message.getMsg();
                            System.out.println("say = "+ msg);
                            this.sendmsg();
                        }
                    }
                }
            }catch(Exception e){
                System.out.println("error info 2");
                e.printStackTrace();}
        }

        //为连接上服务端的每个客户端发送信息
        public void sendmsg()
        {
            //System.out.println(msg);
            int num = mList.size();
            for(int index = 0;index < num;index++)
            {
                Socket mSocket = mList.get(index);

                //System.out.println("ObjectOutputStream oos = null");
                try {
                    //oos = new ObjectOutputStream(mSocket.getOutputStream());
                    //System.out.println(oos.toString()+"dfdfdf");
                    ChatMessage messageSend = new ChatMessage();
                    messageSend.setUserName("ABC");
                    messageSend.setMsg("hello");
                    messageSend.setType("INPUT");
                    oos.writeObject(messageSend);
                    oos.flush();
                    //oos.close();
                    System.out.println("info send success "+messageSend.getMsg());
                }catch (IOException e) {
                    System.out.println("error info 3");
                    e.printStackTrace();}
            }
        }
    }
}
