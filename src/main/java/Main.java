import io.grpc.Server;
import io.grpc.ServerBuilder;
import server.EchoService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50051)
                .addService(new EchoService())
                .build();

        server.start();

        server.awaitTermination();
    }
}
