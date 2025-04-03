package com.github.monadoeater;
import com.github.monadoeater.db.sqlite.EaterDb;
import com.github.monadoeater.website.fandom.Webpage;
import com.github.monadoeater.website.fandom.WebsiteCrawler;


public class Main {
    private static final String FANDOM_NAME = "xenoblade";

    // Scrape fandom website and load into eater database.
    private static void eatFandomWebsite(String fandomName) {
        EaterDb.connect();
        WebsiteCrawler websiteCrawler = new WebsiteCrawler(fandomName, "Brams_Driver");
        Webpage webpage = websiteCrawler.crawl();
        while (webpage != null) {
            System.out.println("Eating: ".concat(webpage.getUrl()));
            webpage = websiteCrawler.crawl();
            EaterDb.insertWebpage(webpage);
        }
    }


    public static void main(String[] args) {
        eatFandomWebsite(FANDOM_NAME);
    }
}
