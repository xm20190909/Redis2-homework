package com.zime;

public class Article {
    private String title;
    private String link;
    private String author;
    private String time;
    private String votes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", time='" + time + '\'' +
                ", votes='" + votes + '\'' +
                '}';
    }
}
