package com.tyhyidon.faust.game.model.statistics;

import java.util.ArrayList;
import java.util.List;

public class BaseStatistics {

    private List<Long> all = new ArrayList<>();
    private List<Long> wins = new ArrayList<>();
    private List<Long> clearWins = new ArrayList<>();

    public List<Long> getAll() {
        return all;
    }

    public void setAll(List<Long> all) {
        this.all = all;
    }

    public List<Long> getWins() {
        return wins;
    }

    public void setWins(List<Long> wins) {
        this.wins = wins;
    }

    public List<Long> getClearWins() {
        return clearWins;
    }

    public void setClearWins(List<Long> clearWins) {
        this.clearWins = clearWins;
    }
}
