package com.github.monadoeater;
import com.github.monadoeater.db.sqlite.EaterDb;
import com.github.monadoeater.website.fandom.WebsiteCrawler;


public class Main {
    private static final String FANDOM_NAME = "xenoblade";

    public static void main(String[] args) {
        WebsiteCrawler websiteCrawler = new WebsiteCrawler(FANDOM_NAME);
        websiteCrawler.crawl();


        //EaterDb.connect();
    }
}
