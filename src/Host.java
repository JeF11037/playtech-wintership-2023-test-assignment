package src;

public class Host 
{
    private long balance = 0;

    public long get_balance()
    {
        return this.balance;
    }

    public void adjust_balance(int value)
    {
        this.balance += value;
    }
}
