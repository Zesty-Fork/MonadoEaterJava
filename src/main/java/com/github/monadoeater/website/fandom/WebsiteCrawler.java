package com.github.monadoeater.website.fandom;

import java.util.ArrayList;
import java.util.Iterator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebsiteCrawler {
    private static final String DEFAULT_URL = "https://%s.fandom.com";
    private static final String DEFAULT_SITEMAP_HREF = "/wiki/Local_Sitemap";
    private static final String NAV_CLASS = "mw-allpages-nav";
    private static final String CHUNK_CLASS = "mw-allpages-chunk";

    private static String baseUrl;
    private static String navHrefNext;
    private static Iterator<String> chunkHrefIterator = null;

    public WebsiteCrawler(String fandomName) {
        setBaseUrl(String.format(DEFAULT_URL, fandomName));
        loadSitemap(baseUrl.concat(DEFAULT_SITEMAP_HREF));
    }

    public WebsiteCrawler(String fandomName, String nameFrom) {
        setBaseUrl(String.format(DEFAULT_URL, fandomName));
        loadSitemap(baseUrl.concat(DEFAULT_SITEMAP_HREF).concat("?namefrom=").concat(nameFrom));
    }

    // Loop site map pages and find all content pages.
    public Webpage crawl() {
        Webpage webpage = null;
        if (chunkHrefIterator.hasNext()) {
            webpage = getNextWebpage();
        }
        else if (!isLastSitemap()) {
            loadSitemap(baseUrl.concat(navHrefNext));
            webpage = getNextWebpage();
        }
        return webpage;
    }

    private Webpage getNextWebpage() {
        String href = chunkHrefIterator.next();
        return new Webpage(baseUrl.concat(href));
    }

    // Check if the next nav href is null.
    private boolean isLastSitemap() {
        return navHrefNext == null;
    }

    // Load sitemap and parse nav and chunk elements.
    private void loadSitemap(String sitemapUrl) {
        Webpage webpage = new Webpage(sitemapUrl);
        parseNav(webpage);
        setChunkHrefIterator(parseChunk(webpage));
    }


    // Parse nav for next site map href.
    private void parseNav(Webpage webpage) {
        Element nav = getFirstClassElement(webpage.getDocument(), NAV_CLASS);
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
    private Iterator<String> parseChunk(Webpage webpage) {
        ArrayList<String> chunkHrefs = new ArrayList<String>();
        Element chunkClass = getFirstClassElement(webpage.getDocument(), CHUNK_CLASS);
        ArrayList<Element> li = chunkClass.getElementsByTag("li");
        for (Element item : li) {
            Element anchor = item.getElementsByTag("a").first();
            if (anchor != null) {
                String href = getHrefFromAnchor(anchor);
                chunkHrefs.add(href);
            }
        }
        return chunkHrefs.iterator();
    }

    // Return first element from a class.
    private Element getFirstClassElement(Document document, String className) {
        ArrayList<Element> navClass = document.getElementsByClass(className);
        return navClass.getFirst();
    }

    // Return href from an anchor element.
    private String getHrefFromAnchor(Element anchor) {
        return anchor.attr("href");
    }

    /*** Getters/setters ***/
    private static void setBaseUrl(String baseUrl) {WebsiteCrawler.baseUrl = baseUrl;}
    private static void setNavHrefNext(String navHrefNext) {WebsiteCrawler.navHrefNext = navHrefNext;}
    public static void setChunkHrefIterator(Iterator<String> chunkHrefIterator) {WebsiteCrawler.chunkHrefIterator = chunkHrefIterator;}
}
