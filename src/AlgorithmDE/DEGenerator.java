package AlgorithmDE;

import Common.Parameters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Main {
    static private Parameters parameters;

    public static void main(String[] args) {
        boolean isConnected = false;
        ServerSocket serverSocket;
        Socket socket;
        Initiator initiator;
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;
        double[][] outputArray;

        while (!isConnected) {
            try {
                serverSocket = new ServerSocket(4335);
                socket = serverSocket.accept();
                System.out.printf("Connected to Connector!");
                isConnected = true;
                inputStream = new ObjectInputStream(socket.getInputStream());
                parameters = (Parameters) inputStream.readObject();
                //TODO: generate new population and copy it to array
                initiator = new Initiator(parameters);
                outputArray = initiator.ConvertListToOutputArray();
                //sending result
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(outputArray);
                socket.close();
                outputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}