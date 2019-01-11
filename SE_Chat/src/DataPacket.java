import java.util.List;

public class DataPacket {
	private String type;
	private List<String> data;
	public DataPacket(DataPacketTypes type, List<String> data) {
		this.type = type.toString();
		this.data = data;
	}
	public String getType() {
		return this.type;
	}
	public List<String> getData() {
		return this.data;
	}
}
