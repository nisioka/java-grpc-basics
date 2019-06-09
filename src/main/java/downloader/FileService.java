package downloader;

import com.google.protobuf.ByteString;
import file.File;
import file.FileServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileService extends FileServiceGrpc.FileServiceImplBase {

    @Override
    public void download(File.FileRequest request, StreamObserver<File.FileResponse> responseObserver) {
        /* ※外部入力されたファイル名を、ノーチェックで開くという危険極まりない処理。
             ローカルでしか動かさない前提の脆弱なコード。 */
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("src/resources/downloader/", request.getName()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 読み込んだファイル内容を1つづつレスポンスにセットする。
                responseObserver.onNext(File.FileResponse.newBuilder().setData(ByteString.copyFromUtf8(line)).build());
            }
        } catch (IOException e) {
            responseObserver.onError(e);
            return;
        }
        responseObserver.onCompleted();
    }
}
