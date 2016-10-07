import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.stream.events.EndElement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mysql.jdbc.Connection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http2.internal.hpack.Decoder;
import io.netty.util.CharsetUtil;

public class ConnectHandler extends ChannelInboundHandlerAdapter {
	
	private String SID=null;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client" + ctx.channel().remoteAddress() + " connected");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
	
		/////DB관련 변수/////
		DAO mydao = new DAO();
		Connection con = null;
		con = (Connection) mydao.getMyConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		PreparedStatement psmt = null;
		
		//////Login 관련 변수/////
		//String SID = null;
		String PW = null;
		JSONParser jsonParser = new JSONParser();
		
		//////수신데이터별 비지니스 로직//////
		try {
			JSONObject jsonObj = (JSONObject) jsonParser.parse(in.toString(CharsetUtil.UTF_8));
			JSONArray memberArray=null;
			if (jsonObj.get("Login") != null) {
				LoginHandler loginInfo = new LoginHandler(in.toString(CharsetUtil.UTF_8));
				SID=loginInfo.getSID();
				PW=loginInfo.getPW();
				loginInfo.SetLoginResultJson(loginInfo.CheckLogin(con));
				ctx.writeAndFlush(Unpooled.copiedBuffer(loginInfo.SetLoginResultJson(loginInfo.CheckLogin(con)),CharsetUtil.UTF_8));
			}
			else if(jsonObj.get("SEARCH")!=null){
				SearchHandler SearchObj = new SearchHandler(SID,in.toString(CharsetUtil.UTF_8),con);
				//SearchHandler SearchObj = new SearchHandler("201131046",in.toString(CharsetUtil.UTF_8),con);
				ctx.writeAndFlush(Unpooled.copiedBuffer(SearchObj.getSearchResultJson(),CharsetUtil.UTF_8));
			}
			else if(jsonObj.get("BEACON")!=null){
				BeaconHandler BeaconObj = new BeaconHandler(in.toString(CharsetUtil.UTF_8));	
				BeaconObj.judgement(SID, con);
				ctx.writeAndFlush(Unpooled.copiedBuffer(BeaconObj.getSendAndupdateDb(con),CharsetUtil.UTF_8));
			}
			else if(jsonObj.get("OPEN")!=null){
				OpenHandler OpenObj= new OpenHandler(in.toString(CharsetUtil.UTF_8));
				OpenObj.OpenDB(SID, con);
				//OpenObj.OpenDB("201131046", con);
			}
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// ChannelPipeline cpl=ctx.pipeline();
		// cpl.write(Unpooled.copiedBuffer("TRUE",CharsetUtil.UTF_8));
		//ctx.writeAndFlush(in);
		// ctx.write(Unpooled.copiedBuffer("netty",CharsetUtil.UTF_8));
		// cpl.
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

}
