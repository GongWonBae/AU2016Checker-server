import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import javax.xml.stream.events.EndElement;

import com.sun.corba.se.impl.ior.ByteBuffer;
import com.sun.xml.internal.bind.v2.runtime.output.Encoded;
import com.sun.xml.internal.ws.api.message.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http2.internal.hpack.Decoder;
import io.netty.util.CharsetUtil;


public class encoder extends MessageToMessageEncoder<ByteBuf>{

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		System.out.println("encode");
		System.out.println("encode data : "+msg.toString(CharsetUtil.UTF_8));
		System.out.println("읽을수있는 바이트 수 : "+String.valueOf(msg.readableBytes()));
		String teststr="netty공";
		String lien = "\r\n";
		ByteBuf result= Unpooled.buffer();
		result.writeShort(msg.readableBytes()+lien.getBytes().length); //길이헤더 추가
		//result.writeShort(teststr.getBytes().length);
		result.writeBytes(msg);
		result.writeBytes("\r\n".getBytes());
		System.out.println(result.toString(CharsetUtil.UTF_8));
		ctx.writeAndFlush(result);
		
		//결국 클라이언트가 받을수있는 인코딩작업이 필요함
	
	}

}
	
	
	/*public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("test");
		if (in.readableBytes() >= 2) {
			out.add(in.readChar());
		}
	}*/


/*public class decorder extends ChannelInboundHandlerAdapter {
	String mm=null;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client" + ctx.channel().remoteAddress() + " connected");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Channel cnl =ctx.channel();
		ChannelPipeline cnlpi = ctx.pipeline();
		ChannelHandlerContext cttx= ctx;
		
		 ByteBuf in =(ByteBuf)msg;
		 System.out.println("Server received2: " + in.toString(Charset.defaultCharset()));
	    
	       if(out.toString()=="123")
	       {
	    	  System.out.println("들어옴");;
	        }
	      //  ctx.write(in);
	        
	     
	        cttx.fireChannelRead(msg);//다음 inboundhandler에 넘겨줌
	}


	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client" + ctx.channel().remoteAddress() + " disconnected");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}*/
