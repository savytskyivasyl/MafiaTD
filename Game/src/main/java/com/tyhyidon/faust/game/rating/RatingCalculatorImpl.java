package com.tyhyidon.faust.game.rating;

import com.tyhyidon.faust.game.player.Constants;

import java.io.*;
import java.util.Properties;

/**
 * Created by Василий on 03.01.14.
 */
public class RatingCalculatorImpl implements RatingCalculator {

    private Properties percent;
    private int result;
    private int role;
    private int life;
    private int bestVoices;
    private int finalDecision;
    private int fouls;

    public RatingCalculatorImpl(int result, int role, int life, int bestVoices, int finalDecision, int fouls) {
        percent = new Properties();

        try {
            File directory = new File("/");
            File[] fList = directory.listFiles();

        //    for (File file : fList) {
        //       System.out.println(file.getAbsolutePath());
       //     }
            InputStream fis = getClass().getResourceAsStream(RatingConstants.PROPERTIES_FILE_PATH);
       //     System.out.println(fis.toString());
            percent.load(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.result = result;
        this.role = role;
        this.life = life;
        this.bestVoices = bestVoices;
        this.finalDecision = finalDecision;
        this.fouls = fouls;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getBestVoices() {
        return bestVoices;
    }

    public void setBestVoices(int bestVoices) {
        this.bestVoices = bestVoices;
    }

    public int getFinalDecision() {
        return finalDecision;
    }

    public void setFinalDecision(int finalDecision) {
        this.finalDecision = finalDecision;
    }

    public int getFouls() {

        return fouls;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    private String getResultString() {
        switch (result) {
            case Constants.RESULT_MAFIA_CLEAR_WIN:
                return RatingConstants.RESULT_MAFIA_CLEAR_WIN;
            case Constants.RESULT_MAFIA_WIN:
                return RatingConstants.RESULT_MAFIA_WIN;
            case Constants.RESULT_CITY_WIN:
                return RatingConstants.RESULT_CITY_WIN;
            case Constants.RESULT_CITY_CLEAR_WIN:
                return RatingConstants.RESULT_CITY_CLEAR_WIN;
            default:
                return null;
        }
    }

    private String getRoleString() {
        switch (role) {
            case Constants.ROLE_DON:
                return RatingConstants.ROLE_DON;
            case Constants.ROLE_MAFIA:
                return RatingConstants.ROLE_MAFIA;
            case Constants.ROLE_SHERIFF:
                return RatingConstants.ROLE_SHERIFF;
            case Constants.ROLE_CITIZEN:
                return RatingConstants.ROLE_CITIZEN;
            default:
                return null;
        }
    }

    private String getLifeString() {
        switch (life) {
            case Constants.LIFE_KILLED_ONE_NIGHT:
                return RatingConstants.LIFE_KILLED_ONE_NIGHT;
            case Constants.LIFE_KILLED_TWO_NIGHT:
                return RatingConstants.LIFE_KILLED_TWO_NIGHT;
            case Constants.LIFE_KILLED_THREE_NIGHT:
                return RatingConstants.LIFE_KILLED_THREE_NIGHT;
            case Constants.LIFE_KILLED_FOUR_NIGHT:
                return RatingConstants.LIFE_KILLED_FOUR_NIGHT;
            case Constants.LIFE_KILLED_FIVE_PLUS_NIGHT:
                return RatingConstants.LIFE_KILLED_FIVE_PLUS_NIGHT;
            case Constants.LIFE_ZERO_DAY_AWAY:
                return RatingConstants.LIFE_ZERO_DAY_AWAY;
            case Constants.LIFE_ONE_DAY_AWAY:
                return RatingConstants.LIFE_ONE_DAY_AWAY;
            case Constants.LIFE_TWO_DAY_AWAY:
                return RatingConstants.LIFE_TWO_DAY_AWAY;
            case Constants.LIFE_THREE_DAY_AWAY:
                return RatingConstants.LIFE_THREE_DAY_AWAY;
            case Constants.LIFE_FOUR_DAY_AWAY:
                return RatingConstants.LIFE_FOUR_DAY_AWAY;
            case Constants.LIFE_FIVE_PLUS_DAY_AWAY:
                return RatingConstants.LIFE_FIVE_PLUS_DAY_AWAY;
            case Constants.LIFE_NOT_AWAY:
                return RatingConstants.LIFE_NOT_AWAY;
            default:
                return null;
        }
    }

    @Override
    public Double calculateResultRating() {
        return Double.parseDouble(percent.getProperty(getResultString() + getRoleString() + RatingConstants.RESULT));
    }

    @Override
    public Double calculateLifeRating() {
        return Double.parseDouble(percent.getProperty(getResultString() + getRoleString() + RatingConstants.LIFE + getLifeString()));
    }

    @Override
    public Double calculateBestVoicesRating() {
        return bestVoices*Double.parseDouble(percent.getProperty(getResultString() + getRoleString() + RatingConstants.VOICES_BEST));
    }

    @Override
    public Double calculateFinalDecisionRating() {
        if (finalDecision>0) {
            return Double.parseDouble(percent.getProperty(getResultString() + getRoleString() + RatingConstants.FINAL_DECISION))/finalDecision;
        } else {
            return 0.0;
        }
    }

    @Override
    public Double calculateFoulsRating() {
        return Double.parseDouble(percent.getProperty(getResultString() + getRoleString() + RatingConstants.FOULS + fouls));
    }

    @Override
    public Double calculateTotalRating() {
        Double totalRating = calculateResultRating() + calculateLifeRating() + calculateBestVoicesRating()
                + calculateFinalDecisionRating() + calculateFoulsRating();
        if (totalRating>0) {
            return totalRating;
        } else {
            return 0.0;
        }
    }

}
