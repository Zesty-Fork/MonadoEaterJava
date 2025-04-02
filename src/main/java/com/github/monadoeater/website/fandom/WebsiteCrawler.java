package com.github.monadoeater.website.fandom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class WebsiteCrawler {
    private static final String DEFAULT_URL = "https://%s.fandom.com";
    private static final String SITEMAP_HREF = "/wiki/Local_Sitemap";
    private static final String NAV_CLASS = "mw-allpages-nav";
    private static final String CHUNK_CLASS = "mw-allpages-chunk";

    private static String baseUrl;
    private static Document document;
    private static String navHrefNext;
    private static final ArrayList<String> chunkHrefs = new ArrayList<String>();

    public WebsiteCrawler(String fandomName) {
        setBaseUrl(String.format(DEFAULT_URL, fandomName));
        initializeSitemap(baseUrl.concat(SITEMAP_HREF));
    }


    public void crawl() {
        boolean crawlComplete;
        do {
            for (String href: chunkHrefs) {
                href = href;
            }
            chunkHrefs.clear();
            initializeSitemap(baseUrl.concat(navHrefNext));
            crawlComplete = isCrawlComplete();
        } while (!crawlComplete);
    }

    private boolean isCrawlComplete() {
        return navHrefNext == null;
    }

    private void initializeSitemap(String sitemapUrl) {
        System.out.println("Initializing sitemap: " + sitemapUrl);
        loadUrl(sitemapUrl);
        setNav();
        setChunk();
    }

    public void loadUrl(String url) {
        try {
            setDocument(Jsoup.connect(url).get());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setNav() {
        Element nav = getFirstClassElement(NAV_CLASS);
        ArrayList<Element> anchors = nav.getElementsByTag("a");
        Element lastAnchor = anchors.getLast();
        if (lastAnchor.text().startsWith("Next page")) {
            String href = getHrefFromAnchor(lastAnchor);
            setNavHrefNext(href);
        }
        else {
            setNavHrefNext(null);
        }
    }

    private void setChunk() {
        Element chunkClass = getFirstClassElement(CHUNK_CLASS);
        List<Element> li = chunkClass.getElementsByTag("li");
        for (Element item : li) {
            Element anchor = item.getElementsByTag("a").first();
            if (anchor != null) {
                String href = getHrefFromAnchor(anchor);
                chunkHrefs.add(href);
            }
        }
    }


    private Element getFirstClassElement(String className) {
        ArrayList<Element> navClass = WebsiteCrawler.getDocument().getElementsByClass(className);
        return navClass.getFirst();
    }

    private String getHrefFromAnchor(Element anchor) {
        return anchor.attr("href");
    }



    /*** Getters/setters ***/
    // baseUrl
    private static String getBaseUrl() {return baseUrl;}
    private static void setBaseUrl(String baseUrl) {
        WebsiteCrawler.baseUrl = baseUrl;}

    // document
    private static Document getDocument() {return document;}
    private static void setDocument(Document document) {WebsiteCrawler.document = document;}

    // navHrefNext
    private static String getNavHrefNext() {return navHrefNext;}
    private static void setNavHrefNext(String navHrefNext) {WebsiteCrawler.navHrefNext = navHrefNext;}
}
