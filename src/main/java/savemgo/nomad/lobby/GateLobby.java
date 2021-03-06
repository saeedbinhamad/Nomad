package savemgo.nomad.lobby;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import savemgo.nomad.helper.Lobbies;
import savemgo.nomad.helper.News;
import savemgo.nomad.packet.Packet;
import savemgo.nomad.server.Lobby;

@Sharable
public class GateLobby extends Lobby {

	private static final Logger logger = LogManager.getLogger(GateLobby.class.getSimpleName());

	public GateLobby(int id) {
		super(id);
	}

	@Override
	public boolean readPacket(ChannelHandlerContext ctx, Packet in) {
		int command = in.getCommand();

		switch (command) {

		/** General */
		case 0x0003:
			ctx.close();
			break;

		case 0x0005:
			ctx.write(new Packet(0x0005));
			break;

		/** Main Lobby */
		case 0x2005:
			Lobbies.getLobbyList(ctx);
			break;

		case 0x2008:
			News.getNews(ctx);
			break;

		default:
			logger.error("Couldn't handle command " + Integer.toHexString(in.getCommand()));
			return false;
		}

		return true;
	}

}
