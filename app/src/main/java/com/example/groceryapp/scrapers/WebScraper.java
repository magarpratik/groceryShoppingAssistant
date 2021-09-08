/*
package com.example.groceryapp.scraper;

import com.example.groceryapp.database.DatabaseHandler;
import com.example.groceryapp.model.ItemModel;
import com.example.groceryapp.viewModel.InsideListViewModel;

import java.util.ArrayList;

public class WebScraper {
    InsideListViewModel insideListViewModel;
    DatabaseHandler db;

    public WebScraper(InsideListViewModel insideListViewModel, DatabaseHandler db) {
        this.insideListViewModel = insideListViewModel;
        this.db = db;
    }

    public void scrape(int listId) {
        ArrayList<ItemModel> itemsList = db.getItemsList(listId);



        // StoreId 0 = Asda, 1 = Sainsburys, 2 = Tesco
        for (int i = 0; i < itemsList.size(); i++) {
            AsdaScraper asdaScraper = new AsdaScraper(insideListViewModel);
            ArrayList<ArrayList<String>> result = asdaScraper.scrape(itemsList.get(i).getName() + " "
                    + itemsList.get(i).getQuantity() + itemsList.get(i).getUnit());

            // Convert results to itemModels
            ArrayList<ItemModel> finalList = sorter(result, listId, itemsList.get(i).getId());

            // Add the results to the database
            for (ItemModel item:finalList) {
                item.setStoreId(0);
                db.addResults(item);
            }
        }

        */
/*for (int i = 0; i < itemsList.size(); i++) {
            SainsburysScraper sainsburysScraper = new SainsburysScraper();
            ArrayList<ArrayList<String>> result = sainsburysScraper.scrape(itemsList.get(i).getName() + " "
                    + itemsList.get(i).getQuantity() + itemsList.get(i).getUnit());
            // TODO update the database
            // Add the results to the database
            ArrayList<ItemModel> finalList = sorter(result, listId, itemsList.get(i).getId());

            // Add the results to the database
            for (ItemModel item:finalList) {
                item.setStoreId(1);
                db.addResults(item);
            }
        }*//*


        */
/*for (int i = 0; i < itemsList.size(); i++) {
            TescoScraper tescoScraper = new TescoScraper();
            ArrayList<ArrayList<String>> result = tescoScraper.scrape(itemsList.get(i).getName() + " "
                    + itemsList.get(i).getQuantity() + itemsList.get(i).getUnit());
            // TODO update the database
            // Add the results to the database
            ArrayList<ItemModel> finalList = sorter(result, listId, itemsList.get(i).getId());

            // Add the results to the database
            for (ItemModel item:finalList) {
                item.setStoreId(2);
                db.addResults(item);
            }
        }*//*

    }

    public ArrayList<ItemModel> sorter(ArrayList<ArrayList<String>> result, int listId, int itemId) {
        ArrayList<ItemModel> itemsList = new ArrayList<>();

        // create itemModels from the results
        for (int i = 0; i < result.size(); i++) {
            ItemModel itemModel = new ItemModel(listId, result.get(i).get(0));
            itemModel.setId(itemId);
            itemModel.setPrice(result.get(i).get(1));
            itemModel.setQuantity(result.get(i).get(2));
            itemModel.setPricePerUnit(result.get(i).get(3));
            itemsList.add(itemModel);
        }

        return itemsList;
    }
}*/

