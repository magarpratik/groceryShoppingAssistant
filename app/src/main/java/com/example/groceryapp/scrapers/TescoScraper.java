package com.example.groceryapp.scrapers;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TescoScraper {
    public ArrayList<ArrayList<String>> scrape(Document page) {
        ArrayList<ArrayList<String>> finalResult = new ArrayList<ArrayList<String>>();

        /*Document page = null;
        try {
            page = Jsoup.connect("https://www.tesco.com/groceries/en-GB/search?query=" + URLEncoder.encode(searchTerm, "UTF-8")).get();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // scrape prices
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        Elements price = page.select("span.value");

        // scrape name + quantity
        Elements name = page.select("div.product-details--content h3 a");

        // scrape name
        nameScraper(name, result);

        // sort prices into 2 categories
        priceScraper(price, result);

        // scrape quantity
        quantityScraper(name, result);

        // scrape units
        Elements unit = page.select("span.weight");
        quantityScraper(price, unit, result);

        finalResult = AsdaScraper.resultSorter(result);
        return finalResult;
    }

    // sort prices into 2 categories
    public static void priceScraper(Elements price, ArrayList<ArrayList<String>> result) {
        ArrayList<String> priceList = new ArrayList<String>();

        for (int i = 0; i < 20; i++) {
            if (i%2 == 0) {
                priceList.add(price.get(i).text());
            }
        }

        result.add(priceList);
    }

    public static void quantityScraper(Elements price, Elements unit, ArrayList<ArrayList<String>> result) {
        ArrayList<String> unitList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            unitList.add(price.get(i * 2 + 1).text() + unit.get(i).text());
        }
        result.add(unitList);
    }

    public static void nameScraper(Elements name, ArrayList<ArrayList<String>> result) {
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> quantityList = new ArrayList<String>();

        for (int j = 0; j < 10; j++) {
            String[] dirtyName = name.get(j).text().split(" ");
            StringBuilder itemName = new StringBuilder();
            if(dirtyName[dirtyName.length - 1].equals("Pack") && dirtyName[dirtyName.length - 3].equals("Minimum")) {
                for (int i = 0; i < dirtyName.length - 3; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 4)) {
                        itemName.append(" ");
                    }
                }
            }
            else if(dirtyName[dirtyName.length - 1].equals("Pack") || dirtyName[dirtyName.length - 2].equals("Approx")) {
                for (int i = 0; i < dirtyName.length - 2; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 3)) {
                        itemName.append(" ");
                    }
                }
            }
            else if(dirtyName[dirtyName.length - 1].equals("Pint") && dirtyName[dirtyName.length - 3].equals("Ml,")) {
                for (int i = 0; i < dirtyName.length - 4; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 5)) {
                        itemName.append(" ");
                    }
                }
            }
            else if(dirtyName[dirtyName.length - 1].equals("Pint") && dirtyName[dirtyName.length - 3].contains(",")) {
                for (int i = 0; i < dirtyName.length - 3; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 4)) {
                        itemName.append(" ");
                    }
                }
            }

            else if(dirtyName[dirtyName.length - 1].equals("Pint") && dirtyName[dirtyName.length - 2].contains("/")) {
                for (int i = 0; i < dirtyName.length - 2; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 3)) {
                        itemName.append(" ");
                    }
                }
            }
            else if(dirtyName[dirtyName.length - 1].equals("Pints") && dirtyName[dirtyName.length - 2].contains("/")) {
                for (int i = 0; i < dirtyName.length - 2; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 3)) {
                        itemName.append(" ");
                    }
                }
            }
            else if(dirtyName[dirtyName.length - 1].equals("Pint") || dirtyName[dirtyName.length - 1].equals("Pints")) {
                for (int i = 0; i < dirtyName.length - 3; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 4)) {
                        itemName.append(" ");
                    }
                }
            }
            else if(dirtyName[dirtyName.length - 1].equals("Litre") && dirtyName[dirtyName.length - 3].equals("X")) {
                for (int i = 0; i < dirtyName.length - 4; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 5)) {
                        itemName.append(" ");
                    }
                }
            }
            else if(dirtyName[dirtyName.length - 1].equals("Litre")) {
                for (int i = 0; i < dirtyName.length - 2; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 3)) {
                        itemName.append(" ");
                    }
                }
            }
            else if(dirtyName[dirtyName.length - 1].equals("Eggs")) {
                for (int i = 0; i < dirtyName.length; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 1)) {
                        itemName.append(" ");
                    }
                }
            }
            else {
                for (int i = 0; i < dirtyName.length - 1; i++) {
                    itemName.append(dirtyName[i]);
                    if(i != (dirtyName.length - 2)) {
                        itemName.append(" ");
                    }
                }
            }
            nameList.add(itemName.toString().replace("Tesco ", "").replace("British ", ""));
        }
        result.add(nameList);
    }

    // scrape quantity from the dirty name list
    public static void quantityScraper(Elements name, ArrayList<ArrayList<String>> result) {
        ArrayList<String> quantityList = new ArrayList<String>();
        String regex = "(Loose)|(\\d+g)|(\\s\\d+\\.\\d+L)|(\\s\\d+\\.?\\s?(G|Ml|Litre|Kg|Pint|Pints)?)";
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i < 10; i++) {
            Matcher matcher = pattern.matcher(name.get(i).text());
            int count = 0;
            String quantity = "";
            String extraQuantity = "";
            int crossIndex = 0;
            while(matcher.find()) {
                count++;
                if(count == 1) {
                    quantity = name.get(i).text().substring(matcher.start(), matcher.end());
                }
                else if(count == 2) {
                    extraQuantity = name.get(i).text().substring(matcher.start(), matcher.end());
                    crossIndex = matcher.start() - 1;
                }
            }
            if((count == 2 && extraQuantity.contains("Pint"))) {
                quantityList.add(quantity);
            }
            else if((count == 2 && extraQuantity.endsWith("G") && name.get(i).text().charAt(crossIndex) == 'X')
                    || (count == 2 && extraQuantity.endsWith("g") && name.get(i).text().charAt(crossIndex) == 'X')) {
                quantityList.add(quantity + "X" + extraQuantity);
            }
            else if(count == 2){
                quantityList.add(extraQuantity);
            }
            else {
                quantityList.add(quantity);
            }
        }
        result.add(quantityList);
    }
}
