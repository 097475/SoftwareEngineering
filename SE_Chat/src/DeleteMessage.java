import java.util.ArrayList;
import java.util.List;

public class DeleteMessage implements Message{
	private List<Integer> IDs = new ArrayList<>();

	public DeleteMessage(List<Integer> IDs) {
		this.IDs = IDs;
	}

	public List<Integer> getIDs() {
		return this.IDs;
	}
	
}
