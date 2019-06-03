import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;
import server.EchoService;

import java.io.IOException;

/**
 * サーバを立ち上げる。
 */
public class ServerMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        final int portNumber = 50051;
        NettyServerBuilder builder = (NettyServerBuilder)ServerBuilder.forPort(portNumber)
                .addService(new EchoService()); // gRPCのサービスを紐づける。
        Server server = builder.build();

        server.start();
        System.out.printfln("start server on port:%s", portNumber);
        server.awaitTermination();
    }
}
