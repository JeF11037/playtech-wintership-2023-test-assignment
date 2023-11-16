package src;

public class PlayerAction
{
    public enum Operation
    {
        DEPOSIT,
        BET,
        WITHDRAW
    }

    private String uuid;
    private Operation operation;
    private String match_uuid;
    private int coin_number;
    private Match.Side side;

    public PlayerAction(
        String uuid,
        Operation operation,
        int coin_number
    )
    {
        this.uuid = uuid;
        this.operation = operation;
        this.coin_number = coin_number;
    }

    public PlayerAction(
        String uuid,
        Operation operation,
        String match_uuid,
        int coin_number,
        Match.Side side
    )
    {
        this.uuid = uuid;
        this.operation = operation;
        this.match_uuid = match_uuid;
        this.coin_number = coin_number;
        this.side = side;
    }

    public String get_uuid()
    {
        return this.uuid;
    }

    public Operation get_operation()
    {
        return this.operation;
    }

    public String get_match_uuid()
    {
        return this.match_uuid;
    }

    public int get_coin_number()
    {
        return this.coin_number;
    }

    public Match.Side get_side()
    {
        return this.side;
    }

}
