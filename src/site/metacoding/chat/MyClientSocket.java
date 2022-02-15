package site.metacoding.chat;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MyClientSocket01 {

    Socket socket;
    BufferedWriter writer;

    public MyClientSocket01() {
        try {
            socket = new Socket("localhost", 1077);
            writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())
            );
            writer.write("이선경\n"); // 윈도우 os는 \n을 써야 데이터가 보내짐
            writer.flush(); // 버퍼 비우기
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new MyClientSocket01();
    }
}
