package src;

public class Player 
{
    private String uuid;
    private long balance = 0;
    private boolean is_legitimate = true;
    private PlayerAction first_illegal_operation = null;
    private long winning_amount = 0;
    private long total_wins = 0;
    private long total_bets = 0;

    public Player(String uuid)
    {
        this.uuid = uuid;
    }

    public String get_uuid()
    {
        return this.uuid;
    }

    public long get_balance()
    {
        return this.balance;
    }

    public void add_coins(int coin_number)
    {
        this.balance += coin_number;
    }

    public void subtract_coins(int coin_number)
    {
        this.balance -= coin_number;
    }

    public boolean is_operation_legal(int coin_number)
    {
        return (this.balance - coin_number) >= 0 ? true : false;
    }

    public boolean get_is_legitimate()
    {
        return this.is_legitimate;
    }

    public PlayerAction get_first_illegal_operation()
    {
        return this.first_illegal_operation;
    }

    public void set_player_illegitimate(PlayerAction player_action)
    {
        this.is_legitimate = false;
        this.first_illegal_operation = player_action;
    }

    public long get_winning_amount()
    {
        return this.winning_amount;
    }

    public void add_winning_amount(int value)
    {
        this.winning_amount += value;
    }

    public void subtract_winning_amount(int value)
    {
        this.winning_amount -= value;
    }

    public long get_total_wins()
    {
        return this.total_wins;
    }

    public void add_win()
    {
        this.total_wins++;
    }

    public long get_total_bets()
    {
        return this.total_bets;
    }

    public void add_bet()
    {
        this.total_bets++;
    }
}
