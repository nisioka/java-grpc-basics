package downloader;

import file.File;
import file.FileServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

/**
 * クライアントからサーバへリクエストを送る。
 */
public class ClientMain {
    public static void main(String[] args) {
        final int portNumber = 50051;
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", portNumber)
                .usePlaintext(true) // 簡単のためサーバ証明書なしのローカル通信とする。
                .build();

        var stub = FileServiceGrpc.newBlockingStub(channel);

        // ファイル名をリクエストする。
        var request = File.FileRequest.newBuilder().setName("fileA.txt").build();
        Iterator<File.FileResponse> responses = stub.download(request);

        // レスポンスを1つづつ表示する。
        for (int i = 0; responses.hasNext(); i++) {
            File.FileResponse response = responses.next();
            System.out.println("response message[" + i + "]: " + response.getData().toStringUtf8());
        }
    }
}
