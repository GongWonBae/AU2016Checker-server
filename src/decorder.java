import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.stream.events.EndElement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.corba.se.impl.ior.ByteBuffer;
import com.sun.xml.internal.bind.v2.runtime.output.Encoded;

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
import io.netty.handler.codec.http2.internal.hpack.Decoder;
import io.netty.util.CharsetUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.*;

public class decorder extends LengthFieldBasedFrameDecoder{


	  public decorder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip) {
		  
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
		
		System.out.println("test");
		// TODO Auto-generated constructor stub
	}
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
      	System.out.println("decode :"+msg.toString(CharsetUtil.UTF_8));
		
		return super.decode(ctx, msg);
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
	    	  System.out.println("µé¾î¿È");;
	        }
	      //  ctx.write(in);
	        
	     
	        cttx.fireChannelRead(msg);//´ÙÀ½ inboundhandler¿¡ ³Ñ°ÜÁÜ
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
