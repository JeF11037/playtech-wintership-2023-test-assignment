import src.Match;
import src.Player;
import src.PlayerAction;
import src.Host;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;

public class App 
{
    public static void main(String[] args) throws FileNotFoundException
    {
        File match_data_file = new File("src\\sample\\match_data.txt");
        File player_data_file = new File("src\\sample\\player_data.txt");
        HashMap<String, Match> matches = get_matches_from_file(match_data_file);
        LinkedList<PlayerAction> players_actions = get_players_actions_from_file(player_data_file);
        HashMap<String, Player> players = process_players_actions(players_actions, matches);
        
        Host casino_host = new Host();
        String legal_players = "";
        String illegal_players = "";

        for (Player player : players.values()) 
        {
            String player_winrate = new DecimalFormat("0.00").format(((double)player.get_total_wins()/(double)player.get_total_bets())).replace(".", ",");
            if (player.get_is_legitimate())
            {
                legal_players += String.format(
                    "%s %s %s \n",
                    player.get_uuid(), player.get_balance(), player_winrate
                );
                casino_host.adjust_balance(-(int)player.get_winning_amount());
            }
            else
            {
                PlayerAction player_action = player.get_first_illegal_operation();
                illegal_players += String.format(
                    "%s %s %s %s %s \n",
                    player.get_uuid(), player_action.get_operation(), player_action.get_match_uuid(), player_action.get_coin_number(), player_action.get_side()
                );
            }
        }

        if (legal_players.equals(""))
            legal_players = "\n";
        
        if (illegal_players.equals(""))
            illegal_players = "\n";

        PrintWriter output = new PrintWriter("result.txt");
        output.print(String.format(
            "%s\n%s\n%s",
            legal_players, illegal_players, casino_host.get_balance()
            )
        );
        output.close();
    }

    private static HashMap<String, Player> process_players_actions (
        LinkedList<PlayerAction> players_actions,
        HashMap<String, Match> matches
    )
    {
        HashMap<String, Player> players = new HashMap<String, Player>();
        for (PlayerAction player_action : players_actions)
        {
            String player_uuid = player_action.get_uuid();
            int player_coin_number = player_action.get_coin_number();
            Player player = players.keySet().contains(player_uuid) ? players.get(player_uuid) : new Player(player_uuid);

            switch (player_action.get_operation())  
            {
                case DEPOSIT:
                    player.add_coins(player_coin_number);
                    break;
                case BET:
                    process_player_bet_sequence(player, player_action, matches.get(player_action.get_match_uuid()));
                    break;
                case WITHDRAW:
                    if (player.is_operation_legal(player_coin_number))
                    {
                        player.subtract_coins(player_coin_number);
                    }
                    else
                    {
                        player.set_player_illegitimate(player_action);
                    }
                    break;
            }
            players.put(player_uuid, player);
        }
        return players;
    }

    private static void process_player_bet_sequence(
        Player player,
        PlayerAction player_action,
        Match match
    )
    {
        int player_coin_number = player_action.get_coin_number();
        Match.Result match_result = match.get_result();
        Match.Side match_winner_side = match.get_winner_side();

        /*
         * Assumption: the withdrawal of coins from the player's account 
         * occurs only at the end of the logic and the body of the function, 
         * because only three events can occur (according to the assignment description) when a player bets: 
         * 1. if the bet is not legal, then it is canceled.
         * 2. If it's a draw, then nothing will happen.
         * 3. If the player wins, he will receive bet size * corresponding side rate. 
         * If none of this has happened, then the player has lost and loses coins.
         * Thus, there are no unnecessary manipulations.
         */

        if (!player.is_operation_legal(player_coin_number))
        {
            player.set_player_illegitimate(player_action);
            return;
        }

        player.add_bet();

        if (match_result.equals(Match.Result.DRAW))
            return;

        if (match_result.equals(Match.Result.WINNER) && match_winner_side.equals(player_action.get_side()))
        {
            double return_rate = match_winner_side.equals(Match.Side.A) ? match.get_return_rate_a() : match.get_return_rate_b();
            int winning_amount = (int)Math.floor(player_coin_number * return_rate);
            player.add_win();
            player.add_coins(winning_amount);
            player.add_winning_amount(winning_amount);
            return;
        }

        player.subtract_coins(player_coin_number);
        player.subtract_winning_amount(player_coin_number);
    }

    private static LinkedList<PlayerAction> get_players_actions_from_file(File file) throws FileNotFoundException
    {
        try
        {
            LinkedList<PlayerAction> players_actions = new LinkedList<PlayerAction>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())
            {
                String[] row_data = scanner.nextLine().split(",");
                if (row_data.length == 4)
                {
                    players_actions.add(
                        new PlayerAction(
                            row_data[0], 
                            PlayerAction.Operation.valueOf(row_data[1]), 
                            Integer.parseInt(row_data[3])
                        )
                    );
                }

                if (row_data.length == 5)
                {
                    players_actions.add(
                        new PlayerAction(
                            row_data[0], 
                            PlayerAction.Operation.valueOf(row_data[1]),
                            row_data[2],
                            Integer.parseInt(row_data[3]),
                            Match.Side.valueOf(row_data[4])
                        )
                    );
                }
            }
            scanner.close();
            return players_actions;
        }
        catch (FileNotFoundException exception)
        {
            throw new FileNotFoundException(String.format("player_data.txt not found | Details: %s", exception.getMessage()));
        }
    }

    private static HashMap<String, Match> get_matches_from_file(File file) throws FileNotFoundException
    {
        try 
        {
            HashMap<String, Match> matches = new HashMap<String, Match>();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) 
            {
                String[] row_data = scanner.nextLine().split(",");
                
                if (Match.Result.DRAW.toString().equals(row_data[3]))
                {
                    matches.put(
                        row_data[0],
                        new Match(
                            row_data[0], 
                            Double.parseDouble(row_data[1]), 
                            Double.parseDouble(row_data[2]), 
                            Match.Result.valueOf(row_data[3])
                        )
                    );
                }
                else
                {
                    matches.put(
                        row_data[0],
                        new Match(
                            row_data[0], 
                            Double.parseDouble(row_data[1]), 
                            Double.parseDouble(row_data[2]), 
                            Match.Result.WINNER,
                            Match.Side.valueOf(row_data[3])
                        )
                    );
                }
            }
            scanner.close();
            return matches;
        } 
        catch (FileNotFoundException exception)
        {
            throw new FileNotFoundException(String.format("match_data.txt not found | Details: %s", exception.getMessage()));
        }
    }
}
