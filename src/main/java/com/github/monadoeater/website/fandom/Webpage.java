package com.github.monadoeater.website.fandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class Webpage {
    private String url = "";
    private Document document;

    public Webpage(String webpageUrl) {
        setUrl(webpageUrl);
        setDocument(loadWebpage(webpageUrl));
    }

    private Document loadWebpage(String webpageUrl) {
        Document webpageDocument = null;
        try {
            webpageDocument = Jsoup.connect(webpageUrl).get();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return webpageDocument;
    }

    /*** Getters/setters ***/
    public String getUrl() {return url;}
    private void setUrl(String url) {this.url = url;}
    public Document getDocument() {return document;}
    private void setDocument(Document document) {this.document = document;}
}
