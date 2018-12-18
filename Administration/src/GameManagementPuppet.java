import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Date;

public class GameManagementPuppet implements IGameManagement {

	List<String[]> games = null;
	@Override
	public void closeGame(int ID) {
		// TODO Auto-generated method stub
		games.removeIf(x-> x[0].equals(Integer.toString(ID)));
	}

	@Override
	public List<String[]> getGames() {
		// TODO Auto-generated method stub
		if(this.games!=null)
			return this.games;
		
		Random random = new Random();
		List<String[]> games = new ArrayList<>();
		for(int i = 0; i < 10; i++){
			games.add(new String[]{Integer.toString(i),IGameManagement.gameTypes.values()[random.nextInt(3)].toString(),"player"+i,new Date((long)(random.nextDouble()*(1544832370000L - 1544000000000L) + 1544000000000L)).toString()});
		}
		
		this.games = games;
		return games;
	}

	@Override
	public void setMinBet(gameTypes gameType, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String[]> query(String SQLquery) {
		// TODO Auto-generated method stub
		Random random = new Random();
		int min,max;
		float income,expenditure;
		NumberFormat formatter = NumberFormat.getNumberInstance(Locale.ROOT);
		formatter.setMinimumFractionDigits(2);
		formatter.setMaximumFractionDigits(2);
		List<String[]> query = new ArrayList<>();
		switch(SQLquery) {
		case "gamestats":  min = 0;
						   max = 100;
						   income = min + random.nextFloat() * (max - min);
						   expenditure = min + random.nextFloat() * (max - min);
						   query.add(new String[] {formatter.format(expenditure),formatter.format(income)}); // loss gain total
						   query.add(new String[] {new Integer(random.nextInt(10)+1).toString(),new Integer(random.nextInt(20)+2).toString()}); // min avg bet
						   query.add(new String[] {new Integer(random.nextInt(50)+1).toString()}); //game sessions
						   //profitability						   
						   break;
		case "dailystats": query.add(new String[] {"50","52"}); // total
						   query.add(new String[] {"11","12"}); //blackjack
						   query.add(new String[] {"15","8"}); //roulette
						   query.add(new String[] {"24","32"}); //slots
						   query.add(new String[] {"5"}); //game sessions
						   //profitability?
						   break;
		case "totalstats": query.add(new String[] {"500","520"}); // total
							query.add(new String[] {"150","120"}); //blackjack
							query.add(new String[] {"150","100"}); //roulette
							query.add(new String[] {"190","300"}); //slots
							query.add(new String[] {"50"}); //game sessions
						   //profitability?
						   break;
		case "playerstats": 
							min = 0;
							max = 1000;
							income = min + random.nextFloat() * (max - min);
							expenditure = min + random.nextFloat() * (max - min);	
							query.add(new String[] {"player"+random.nextInt(50),"player"+random.nextInt(50)+"@gmail.com"}); //anagraphic
							query.add(new String[] {formatter.format(expenditure),formatter.format(income),new Integer(random.nextInt(1000)).toString()}); //data loss gain balance
							break;
		case "history":		min = 0;
							max = 100;			
							for(int i = 0; i<10;i++) {
								income = min + random.nextFloat() * (max - min);
								expenditure = min + random.nextFloat() * (max - min);	
								query.add(new String[] {formatter.format(expenditure),formatter.format(income),new Integer(random.nextInt(50)).toString()}); // total in out sessions
							} break;  //profitability?
		}
		return query;
	}

}
