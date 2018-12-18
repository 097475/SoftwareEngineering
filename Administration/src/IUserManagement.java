import java.util.List;
import java.util.Map;

public interface IUserManagement {
	public List<String[]> getPlayers();
	public List<String[]> getAllPlayers();
	public void banPlayer(String username, long amount);
	public void editPlayer(String username, Map<String,String> changes);
	public void setCapacity(int cap);
}
