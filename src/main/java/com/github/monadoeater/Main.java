package com.github.monadoeater;
import com.github.monadoeater.db.sqlite.EaterDb;
import com.github.monadoeater.website.fandom.Webpage;
import com.github.monadoeater.website.fandom.WebsiteCrawler;
import org.jsoup.nodes.Document;


public class Main {
    private static final String FANDOM_NAME = "xenosaga";

    // Scrape fandom website and load into eater database.
    private static void eatFandomWebsite(String fandomName) {
        WebsiteCrawler websiteCrawler = new WebsiteCrawler(fandomName);
        Webpage webpage = websiteCrawler.crawl();
        while (webpage != null) {
            System.out.println("Eating: ".concat(webpage.getUrl()));
            webpage = websiteCrawler.crawl();
            if (webpage != null) {
                EaterDb.insertWebpage(webpage);
            }
        }
    }

    private static void printHtmlText() {
        Document document = EaterDb.selectHtml(3);
        System.out.println(document.text());
    }


    public static void main(String[] args) {
        EaterDb.connect();
        eatFandomWebsite(FANDOM_NAME);
        //printHtmlText();
    }
}
