import java.util.List;

public interface IGameManagement {
	enum gameTypes{
		BLACKJACK,
		ROULETTE,
		SLOTS
	}
	public void closeGame(int ID);
	public List<String[]> getGames();
	public void setMinBet(gameTypes gameType, int value);
	public List<String[]> query(String SQLquery);
}
