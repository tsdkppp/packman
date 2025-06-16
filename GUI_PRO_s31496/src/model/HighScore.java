package model;


import java.io.Serializable;

public record HighScore(String name, int score) implements Serializable {}
