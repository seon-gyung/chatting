package site.metacoding.chat;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;
    Scanner sc;

    // 추가 (서버한테 메시지 받기)
    BufferedReader reader;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 2000);
            // 메시지 쓰는 버퍼 달기
            writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
            // 메시지 읽는 버퍼 달기
            reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            
            // 버퍼 읽기 
            new Thread(()->{
                while(true){
                    try {
                        String inputData = reader.readLine();
                        System.out.println("서버한테 받은 메시지 : " + inputData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 스캐너 달고
            sc = new Scanner(System.in);        

            // 키보드로 입력 받고 서버한테 보내기 반복 - 메인 스레드가 하는 일
            while (true) {
                String inputData = sc.nextLine();
                writer.write(inputData + "\n");
                writer.flush(); // 버퍼 비우기
            }          
        } catch (Exception e) {
            System.out.println("통신 오류 발생 : " + e.getMessage());
            // e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new MyClientSocket();
    }
}