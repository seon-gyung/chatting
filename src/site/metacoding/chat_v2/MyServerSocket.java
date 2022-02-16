package site.metacoding.chat_v2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class MyServerSocket {

    // 리스너 (연결 받기) - 메인 스레드로 실행
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트;
    // 서버 메시지 받아서 보내기 (클라이언트 수에 따라 스레드가 늘어남)

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000);
            고객리스트 = new  Vector<>(); // 동기화가 처리된 ArrayList
            // 여러 클라이언트의 연결을 계속 받아야 하기 때문에 선을 while로 돌려서 계속 만들어야 함
            while(true){
                Socket socket = serverSocket.accept(); // 메인 스레드가 연결 대기중... 바이트 스트림 선임
                System.out.println("클라이언트 연결됨");
                고객전담스레드 t = new 고객전담스레드(socket);
                고객리스트.add(t);
                System.out.println("고객리스트 크기 : " + 고객리스트.size());
                new Thread(t).start();                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 내부 클래스
    class 고객전담스레드 implements Runnable {

        // 버퍼를 내부에 가지고 있어야 함. 고객마다 버퍼를 달 수 없어서

        Socket socket;
        BufferedReader reader;
        BufferedWriter writer;
        boolean isLogin = true; 

        public 고객전담스레드(Socket socket) {
            this.socket = socket;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        // 새로운 실
        public void run() {
            while(isLogin){
                try {
                    String inputData =  reader.readLine();
                    System.out.println("from 클라이언트 : " + inputData);

                    // 메시지 받았으니까 List<고객전담스레드> 고객리스트 에 담긴
                    // 모든 클라이언트에게 메시지 전송 (for문 돌려서!)
                    for(고객전담스레드 t : 고객리스트){ // 왼 컬렉션 타입 : 오 컬렉션
                        if(t != this){
                            t.writer.write(inputData+"\n");
                            t.writer.flush();
                        }
                        // 고객리스트 get(i).writer.write(inputDate+"\n")
                    }
                } catch (Exception e) {
                    System.out.println("통신 실패 : " + e.getMessage());
                    try {
                        isLogin = false;
                        고객리스트.remove(this); // 통신 터졌을 때 자기 자신을 날림. 날림으로써 가비지 컬렉션 대상이 됨.
                        reader.close(); // 통신 부하를 최대한 덜기 위해 최대한 빠르게 날림
                        writer.close(); // 통신 부하를 최대한 덜기 위해 최대한 빠르게 날림
                        socket.close(); // 통신 부하를 최대한 덜기 위해 최대한 빠르게 날림
                    } catch (Exception e1) {
                        System.out.println("연결 해제 프로세스 실패" + e1.getMessage());
                    }
                }

            }
        }

    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
    
}