import com.tyhyidon.faust.game.ApplicationWeb;
import com.tyhyidon.faust.game.entity.Game;
import com.tyhyidon.faust.game.entity.Member;
import com.tyhyidon.faust.game.entity.Player;
import com.tyhyidon.faust.game.entity.enums.Life;
import com.tyhyidon.faust.game.entity.enums.Result;
import com.tyhyidon.faust.game.entity.enums.Role;
import static org.junit.Assert.*;

import com.tyhyidon.faust.game.entity.enums.Season;
import com.tyhyidon.faust.game.services.GameServiceImpl;
import com.tyhyidon.faust.game.services.MemberServiceImpl;
import com.tyhyidon.faust.game.services.PlayerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.*;
import java.util.stream.Stream;

/**
 * Created by vasylsavytskyi on 05.05.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationWeb.class)
public class GameTest {

    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    PlayerServiceImpl playerService;

    @Autowired
    GameServiceImpl gameService;

    @Test
    public void testAddRemoveMember() {
        Member stubMember = getStubMember("stubMember");
        long initialMemberAmount = memberService.amount();
        memberService.addUpdate(stubMember);
        assertEquals(initialMemberAmount + 1, memberService.amount());
        Member dbStubMember = memberService.find("stubMember");
        assertNotNull(dbStubMember);
        assertEquals("stubMember", dbStubMember.getNickname());
        memberService.remove(dbStubMember.getNickname());
        assertEquals(initialMemberAmount, memberService.amount());
        assertNull(memberService.find("stubMember"));
    }

    @Test
    public void testAddRemoveGame() {
        long initialPlayerAmount = playerService.amount();
        long initialGameAmount = gameService.amount();
        Member dbStubMember = memberService.addUpdate(getStubMember("stubMember"));
        Game game = getStubGame(dbStubMember.getNickname(), new ArrayList<>());
        Iterable<Member> dbStubMembers = Stream.iterate(1, n -> n + 1).limit(10).
                map(i -> memberService.addUpdate(getStubMember("stubMember" + i))).collect(toList());
        dbStubMembers.forEach(m -> game.getPlayers().add(getStubPlayer(game, m)));
        Game dbStubGame = gameService.addUpdate(game);
        assertNotNull(dbStubGame);
        dbStubGame = gameService.find(dbStubGame.getId());
        assertNotNull(dbStubGame);
        testGame(dbStubGame);
        assertEquals(initialPlayerAmount + 10, playerService.amount());
        assertEquals(initialGameAmount + 1, gameService.amount());
        gameService.remove(dbStubGame.getId());
        assertEquals(initialPlayerAmount, playerService.amount());
        assertEquals(initialGameAmount, gameService.amount());
        dbStubMembers.forEach(m -> memberService.remove(m.getNickname()));
        memberService.remove(dbStubMember.getNickname());
    }

    @Test
    public void testGamesUpdate() {
        List<Game> bySeason = gameService.findBySeason(Season.SPRING_15);
        assertNull(bySeason.get(0).getPlayers());
    }

    private Member getStubMember(String nickname) {
        return new Member(nickname);
    }

    private Game getStubGame(String master, List<Player> players) {
        Game game = new Game();
        game.setDate(new Date());
        game.setMaster(master);
        game.setResult(Result.CITY);
        game.setPlayers(players);
        return game;
    }

    private Player getStubPlayer(Game game, Member member) {
        Player player = new Player();
        player.setGame(game);
        player.setMember(member);
        player.setLife(Life.KILLED_2ND);
        player.setBestVoices(2);
        player.setFinalDecision(1);
        player.setFouls(3);
        player.setNumber(4);
        player.setRole(Role.CITIZEN);
        return player;
    }

    private void testGame(Game stubGame) {
        assertNotNull(stubGame.getMaster());
        assertEquals(Result.CITY, stubGame.getResult());
        assertNotNull(stubGame.getPlayers());
        assertEquals(10, stubGame.getPlayers().size());
        stubGame.getPlayers().stream().forEach(this::testPlayer);
    }

    private void testPlayer(Player stubPlayer) {
        assertNotNull(stubPlayer.getMember());
        assertEquals(Life.KILLED_2ND, stubPlayer.getLife());
        assertEquals(2, stubPlayer.getBestVoices());
        assertEquals(1, stubPlayer.getFinalDecision());
        assertEquals(3, stubPlayer.getFouls());
        assertEquals(4, stubPlayer.getNumber());
        assertEquals(Role.CITIZEN, stubPlayer.getRole());
    }
}
