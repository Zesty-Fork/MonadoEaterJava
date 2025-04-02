package com.github.monadoeater.website.fandom;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebsiteCrawler {
    private static final String DEFAULT_URL = "https://%s.fandom.com";
    private static final String DEFAULT_SITEMAP_HREF = "/wiki/Local_Sitemap";
    private static final String NAV_CLASS = "mw-allpages-nav";
    private static final String CHUNK_CLASS = "mw-allpages-chunk";

    private static String baseUrl;
    private static Document document;
    private static String navHrefNext;
    private static final ArrayList<String> chunkHrefs = new ArrayList<>();

    public WebsiteCrawler(String fandomName) {
        setBaseUrl(String.format(DEFAULT_URL, fandomName));
        initializeSitemap(baseUrl.concat(DEFAULT_SITEMAP_HREF));
    }

    // Loop site map pages and find all content pages.
    public void crawl() {
        boolean lastPage;
        do {
            for (String href: chunkHrefs) {
                href = href;
            }
            chunkHrefs.clear();
            initializeSitemap(baseUrl.concat(navHrefNext));
            lastPage = isLastPage();
        } while (!lastPage);
    }

    // Check if the next nav href is null.
    private boolean isLastPage() {
        return navHrefNext == null;
    }

    // Load sitemap and parse nav and chunk elements.
    private void initializeSitemap(String sitemapUrl) {
        System.out.println("Initializing sitemap: " + sitemapUrl);
        loadUrl(sitemapUrl);
        parseNav();
        parseChunk();
    }

    // Connect to url and store as Document object.
    public void loadUrl(String url) {
        try {
            setDocument(Jsoup.connect(url).get());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Parse nav for next site map href.
    private void parseNav() {
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

    // Parse chunk for all content page hrefs.
    private void parseChunk() {
        Element chunkClass = getFirstClassElement(CHUNK_CLASS);
        ArrayList<Element> li = chunkClass.getElementsByTag("li");
        for (Element item : li) {
            Element anchor = item.getElementsByTag("a").first();
            if (anchor != null) {
                String href = getHrefFromAnchor(anchor);
                chunkHrefs.add(href);
            }
        }
    }

    // Return first element from a class.
    private Element getFirstClassElement(String className) {
        ArrayList<Element> navClass = WebsiteCrawler.document.getElementsByClass(className);
        return navClass.getFirst();
    }

    // Return href from an anchor element.
    private String getHrefFromAnchor(Element anchor) {
        return anchor.attr("href");
    }

    /*** Getters/setters ***/
    private static void setBaseUrl(String baseUrl) {WebsiteCrawler.baseUrl = baseUrl;}
    private static void setDocument(Document document) {WebsiteCrawler.document = document;}
    private static void setNavHrefNext(String navHrefNext) {WebsiteCrawler.navHrefNext = navHrefNext;}
}
