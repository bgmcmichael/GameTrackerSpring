package com.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenji on 9/19/2016.
 */
@RestController
public class GameTrackerJSONController {
    @Autowired
    GameRepository games;

    @RequestMapping(path = "/games.json", method = RequestMethod.GET)
    public ArrayList<Game> getGames(){
        ArrayList<Game> gameList = new ArrayList<Game>();
        Iterable<Game> allGames = games.findAll();
        for (Game game : allGames) {
            gameList.add(game);
        }
        try{

            Thread.sleep(3000);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return gameList;
    }

    @RequestMapping(path = "/toggleGame.json", method = RequestMethod.GET)
    public List<Game> toggleGame(int gameID) {
        System.out.println("toggling game with ID " + gameID);
        Game game = games.findOne(gameID);
        game.name = "**" + game.name;
        games.save(game);

        return getGames();
    }

    @RequestMapping(path = "/addGame.json", method = RequestMethod.POST)
    public List<Game> addGame(HttpSession session, @RequestBody Game game) throws Exception {
        User user = (User)session.getAttribute("user");

        if (user == null) {
            throw new Exception("Unable to add game without an active user in the session");
        }
        game.user = user;

        games.save(game);

        return getGames();
    }
}
