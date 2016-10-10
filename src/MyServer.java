
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class MyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            .handler(new LoggingHandler(LogLevel.TRACE))
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new LoggingHandler(LogLevel.INFO));
                  //p.addLast(new LengthFieldBasedFrameDecoder(1024*64, 0, 2,0, 2));
                    p.addLast(new decorder(1024*64, 0, 2,0, 2)); //길이제거+또다른 decode
                    p.addLast(new encoder()); //보낼때 호출됨 encode해서 보내기
                    p.addLast(new MainLogicHandler());  //비지니스 로직 구성하기
               //     p.addLast(new IdleStateHandler(0, 0, 1, TimeUnit.SECONDS));  
                }
           });

            ChannelFuture f = b.bind(8888).sync();

            
            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

