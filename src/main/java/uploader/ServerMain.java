package uploader;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * サーバを立ち上げる。
 */
public class ServerMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        final int portNumber = 50051;
        Server server = ServerBuilder.forPort(portNumber)
                .addService(new FileService()) // gRPCのサービスを紐づける。
                .build();

        server.start();
        System.out.println("start server on port:" + portNumber);
        server.awaitTermination();
    }
}
