import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;
import server.EchoService;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        NettyServerBuilder builder = (NettyServerBuilder)ServerBuilder.forPort(50052)
                .addService(new EchoService());
        Server server = builder.build();

        server.start();
        System.out.println("server started.");
        server.awaitTermination();
    }
}
