package tyu.mvnproject1;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.HashMap;

public class CraigslistCrawler {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36";
    public static void main(String[] args) throws IOException {

        String requestUrl = args[0];
        //String requestUrl = "https://sfbay.craigslist.org/d/apts-housing-for-rent/search/apa";
        String dataFilePath = args[1];
        //String dataFilePath = "/Users/yiwang/CS502-1801/project2-exam/CraiglistCrawler/CraiglistCrawler.txt";

        File file = new File(dataFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        asyncCrawl(requestUrl, bw);
        bw.close();

    }
    private static void asyncCrawl(String requestUrl,BufferedWriter bw) {
        try{
            Document doc = getDomFromContent(requestUrl);
            String pageTitle = htmlTitle(doc);
            System.out.println("Async html title= " + pageTitle);

            String urlElePath;
            String titlePath;
            String pricePath;
            String hoodPath;

            String tempRes;

            for (int i = 1; i <= 120; i++){
                urlElePath = ("#sortable-results > ul > li:nth-child(" + Integer.toString(i) + ") > a");
                titlePath = ("#sortable-results > ul > li:nth-child(" + Integer.toString(i) + ") > p > a");
                pricePath = ("#sortable-results > ul > li:nth-child(" + Integer.toString(i) + ") > p > span.result-meta > span.result-price");
                hoodPath = ("#sortable-results > ul > li:nth-child(" + Integer.toString(i) + ") > p > span.result-meta > span.result-hood");

                tempRes = "";

                Element eleTitle = doc.select(titlePath).first();
                String title;
                try{
                    title = eleTitle.text();
                }
                catch (Exception e){
                    title = null;
                }
                tempRes += title;
                System.out.println("Async title = " + title);


                Element elePrice = doc.select(pricePath).first();
                String Price;
                try{
                    Price = elePrice.text();
                }
                catch (Exception e){
                    Price = null;
                }
                tempRes += "; " + Price;
                System.out.println("Async price = " + Price);


                Element ele = doc.select(urlElePath).first();
                String detailUrl;
                try{
                    detailUrl = ele.attr("href");
                }
                catch(Exception e){
                    detailUrl = null;
                }
                tempRes += "; " + detailUrl;
                System.out.println("Async detail url = " + detailUrl);


                Element eleHood = doc.select(hoodPath).first();
                String Hood;
                try{
                    Hood = eleHood.text();
                }
                catch (Exception e){
                    Hood = null;
                }
                tempRes += "; " + Hood;
                System.out.println("Async hood = " + Hood);

                System.out.println(i);
                bw.write(tempRes);
                bw.newLine();
            }

        } catch (Exception e){

        }
    }

    private static String htmlTitle(Document dom) {
        Element node = dom.select("title").first();
        if (node != null && node.text().length() > 0) {
            return node.text();
        }
        return null;
    }

    private static Document getDomFromContent(String url) {
        //return Jsoup.parse(content, url);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Language","en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7");
        headers.put("Connection","keep-alive");
        headers.put("UserHostAddress","108.50.243.20");
        try{
            return Jsoup.connect(url).headers(headers).userAgent(USER_AGENT).timeout(100000).get();
        }
        catch(Exception e){
            return null;
        }
    }
}
