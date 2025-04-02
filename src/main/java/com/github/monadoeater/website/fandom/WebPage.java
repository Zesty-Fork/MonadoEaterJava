package com.github.monadoeater.website.fandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;

public class WebPage {
    private String url = "";
    private Document document;

    public WebPage(String url) {
        setUrl(url);
    }

    public void load(String url) {
        try {
            setDocument(Jsoup.connect(url).get());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
