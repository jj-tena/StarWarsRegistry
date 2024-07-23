package com.jjtena.backend.model;

import java.util.List;

public class CharacterResponse {
    private int count;
    private String next;
    private String previous;
    private List<CharacterModel> results;

    // Getters and Setters
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public String getNext() { return next; }
    public void setNext(String next) { this.next = next; }
    public String getPrevious() { return previous; }
    public void setPrevious(String previous) { this.previous = previous; }
    public List<CharacterModel> getResults() { return results; }
    public void setResults(List<CharacterModel> results) { this.results = results; }
}
