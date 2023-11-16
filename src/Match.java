package src;

public class Match 
{
    public enum Side
    {
        A,
        B
    }

    public enum Result
    {
        WINNER,
        DRAW
    }

    private String uuid;
    private double return_rate_a;
    private double return_rate_b;
    private Result result;
    private Side winner_side;

    public Match(
        String uuid, 
        double return_rate_a, 
        double return_rate_b,
        Result result
    )
    {
        this.uuid = uuid;
        this.return_rate_a = return_rate_a;
        this.return_rate_b = return_rate_b;
        this.result = result;
    }

    public Match(
        String uuid, 
        double return_rate_a, 
        double return_rate_b,
        Result result,
        Side winner_side
    )
    {
        this.uuid = uuid;
        this.return_rate_a = return_rate_a;
        this.return_rate_b = return_rate_b;
        this.result = result;
        this.winner_side = winner_side;
    }

    public String get_uuid()
    {
        return this.uuid;
    }

    public double get_return_rate_a()
    {
        return this.return_rate_a;
    }

    public double get_return_rate_b()
    {
        return this.return_rate_b;
    }

    public Result get_result()
    {
        return this.result;
    }

    public Side get_winner_side()
    {
        return this.winner_side;
    }

}
