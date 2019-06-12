package interceptor;

import io.grpc.*;

import java.util.Date;

public class LoggingUnary implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        final long time = new Date().getTime();
        return null;
    }
}
