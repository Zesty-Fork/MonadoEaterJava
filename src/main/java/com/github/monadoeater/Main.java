package com.github.monadoeater;
import com.github.monadoeater.db.sqlite.EaterDb;
import com.github.monadoeater.website.fandom.Webpage;
import com.github.monadoeater.website.fandom.WebsiteCrawler;


public class Main {
    private static final String FANDOM_NAME = "xenoblade";


    private static void eatXenobladeFandomWebsite() {
        EaterDb.connect();
        WebsiteCrawler websiteCrawler = new WebsiteCrawler(FANDOM_NAME);
        Webpage webpage = websiteCrawler.crawl();
        while (webpage != null) {
            System.out.println(webpage.getUrl());
            webpage = websiteCrawler.crawl();
        }
    }


    public static void main(String[] args) {
        eatXenobladeFandomWebsite();
    }
}
