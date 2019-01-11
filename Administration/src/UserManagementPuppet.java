import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class UserManagementPuppet implements IUserManagement {

	List<String[]> players = null;
	List<String[]> allPlayers = null;
	@Override
	public List<String[]> getPlayers() {
		// TODO Auto-generated method stub
		if(this.players != null)
			return this.players;
		
		Random random = new Random();
		List<String[]> players = new ArrayList<>();
		for(int i = 0; i < 10; i++){
			players.add(new String[]{"player"+i,""+random.nextInt(256)+"."+random.nextInt(256)+"."+random.nextInt(256)+"."+random.nextInt(256),"game"+i,new Date((long)(random.nextDouble()*(1544832370000L - 1544000000000L) + 1544000000000L)).toString()});
		}
		
		this.players = players;
		return players;
	}

	@Override
	public void banPlayer(String username, long amount) {
		// TODO Auto-generated method stub
		players.removeIf(x-> x[0].equals(username));
	}

	@Override
	public void editPlayer(String username, Map<String, String> changes) {
		// TODO Auto-generated method stub
		if(this.allPlayers.stream().filter(x -> x[0].equals(changes.get("username")) || x[1].equals(changes.get("email"))).count() > 1 )
			return;
		
		for(String[] player : this.allPlayers) {
			if(player[0].equals(username)){
				player[0] = changes.get("username");
				player[1] = changes.get("email");
				break;
			}
		}
		for(String[] player : this.players) {
			if(player[0].equals(username)){
				player[0] = changes.get("username");
				break;
			}
		}

	}

	@Override
	public List<String[]> getAllPlayers() {
		// TODO Auto-generated method stub
		if(this.allPlayers!=null)
			return this.allPlayers;
		List<String[]> players = new ArrayList<>();
		for(int i = 0; i < 10; i++){
			players.add(new String[]{"player"+i,"player"+i+"@gmail.com"});
		}
		
		this.allPlayers = players;
		return players;
	}

	@Override
	public void setCapacity(int cap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String[]> getPlayerStatistics(String SQLquery) {
		// TODO Auto-generated method stub
		Random random = new Random();
		int min,max;
		float income,expenditure;
		List<String[]> query = new ArrayList<>();
		NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ROOT);
		formatter.setMinimumFractionDigits(2);
		formatter.setMaximumFractionDigits(2);
		min = 0;
		max = 1000;
		income = min + random.nextFloat() * (max - min);
		expenditure = min + random.nextFloat() * (max - min);	
		query.add(new String[] {"player"+random.nextInt(50),"player"+random.nextInt(50)+"@gmail.com"}); //anagraphic
		query.add(new String[] {formatter.format(expenditure),formatter.format(income),new Integer(random.nextInt(1000)).toString()}); //data loss gain balance
		return query;
	}

}
