package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private ItemService itemService; // Handles all the Service Related to item.


    /* The method handles get All Restaurants request
    & produces response in RestaurantListResponse and returns list of restaurant with details from the db. If error returns error code and error message.
    */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/restaurant",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants(){

        //Calls restaurantsByRating method of restaurantService to get the list of restaurant entity.
        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByRating();

        //Creating restaurant list for the response
        List<RestaurantList> restaurantLists = new LinkedList<>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) { //Looping for each restaurant entity in restaurantEntities

            //Calls  getCategoriesByRestaurant to get categories of the corresponding restaurant.
            List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
            String categories = new String();
            //To concat the category names.
            ListIterator<CategoryEntity> listIterator = categoryEntities.listIterator();
            while (listIterator.hasNext()){
                categories =  categories + listIterator.next().getCategoryName() ;
                if(listIterator.hasNext()){
                    categories = categories + ", ";
                }
            }

            //Creating the RestaurantDetailsResponseAddressState for the RestaurantDetailsResponseAddress
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddressId().getStateEntityId().getUuid()))
                    .stateName(restaurantEntity.getAddressId().getStateEntityId().getStateName());

            //Creating the RestaurantDetailsResponseAddress for the RestaurantList
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddressId().getUuid()))
                    .city(restaurantEntity.getAddressId().getCity())
                    .flatBuildingName(restaurantEntity.getAddressId().getFlatBuilNumber())
                    .locality(restaurantEntity.getAddressId().getLocality())
                    .pincode(restaurantEntity.getAddressId().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            //Creating RestaurantList to add to list of RestaurantList
            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo())
                    .categories(categories)
                    .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                    .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .address(restaurantDetailsResponseAddress);

            //Adding it to the list
            restaurantLists.add(restaurantList);

        }

        //Creating the RestaurantListResponse by adding the list of RestaurantList
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }


    /* The method handles get Restaurant By Name. It takes Restaurant name as the path variable.
       & produces response in RestaurantListResponse and returns list of restaurant with details from the db. If error returns error code and error message.
       */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/name/{restaurant_name}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByName (@PathVariable(value = "restaurant_name") final String restaurantName)throws RestaurantNotFoundException {

        //Calls restaurantsByName method of restaurantService to get the list of restaurant entity.
        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByName(restaurantName);

        if (!restaurantEntities.isEmpty()) {//Checking if the restaurant entity is empty

            //Creating restaurant list for the response
            List<RestaurantList> restaurantLists = new LinkedList<>();
            for (RestaurantEntity restaurantEntity : restaurantEntities) {  //Looping for each restaurant entity in restaurantEntities

                //Calls  getCategoriesByRestaurant to get categories of the corresponding restaurant.
                List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
                String categories = new String();
                ListIterator<CategoryEntity> listIterator = categoryEntities.listIterator();
                //To concat the category names.
                while (listIterator.hasNext()) {
                    categories = categories + listIterator.next().getCategoryName();
                    if (listIterator.hasNext()) {
                        categories = categories + ", ";
                    }
                }

                //Creating the RestaurantDetailsResponseAddressState for the RestaurantDetailsResponseAddress
                RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                        .id(UUID.fromString(restaurantEntity.getAddressId().getStateEntityId().getUuid()))
                        .stateName(restaurantEntity.getAddressId().getStateEntityId().getStateName());

                //Creating the RestaurantDetailsResponseAddress for the RestaurantList
                RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                        .id(UUID.fromString(restaurantEntity.getAddressId().getUuid()))
                        .city(restaurantEntity.getAddressId().getCity())
                        .flatBuildingName(restaurantEntity.getAddressId().getFlatBuilNumber())
                        .locality(restaurantEntity.getAddressId().getLocality())
                        .pincode(restaurantEntity.getAddressId().getPincode())
                        .state(restaurantDetailsResponseAddressState);

                //Creating RestaurantList to add to list of RestaurantList
                RestaurantList restaurantList = new RestaurantList()
                        .id(UUID.fromString(restaurantEntity.getUuid()))
                        .restaurantName(restaurantEntity.getRestaurantName())
                        .averagePrice(restaurantEntity.getAveragePriceForTwo())
                        .categories(categories)
                        .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                        .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                        .photoURL(restaurantEntity.getPhotoUrl())
                        .address(restaurantDetailsResponseAddress);

                //Adding it to the list
                restaurantLists.add(restaurantList);

            }

            //Creating the RestaurantListResponse by adding the list of RestaurantList
            RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
            return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<RestaurantListResponse>(new RestaurantListResponse(),HttpStatus.OK);
        }

    }


    /* The method handles get Restaurant By Category Id. It takes category_id as the path variable.
  & produces response in RestaurantListResponse and returns list of restaurant with details from the db. If error returns error code and error message.
  */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/category/{category_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategoryId(@PathVariable(value = "category_id")String categoryId) throws CategoryNotFoundException {

        //Calls restaurantByCategory method of restaurantService to get the list of restaurant entity.
        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantByCategory(categoryId);

        //Creating restaurant list for the response
        List<RestaurantList> restaurantLists = new LinkedList<>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) { //Looping for each restaurant entity in restaurantEntities

            //Calls  getCategoriesByRestaurant to get categories of the corresponding restaurant.
            List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
            String categories = new String();
            ListIterator<CategoryEntity> listIterator = categoryEntities.listIterator();
            //To concat the category names.
            while (listIterator.hasNext()) {
                categories = categories + listIterator.next().getCategoryName();
                if (listIterator.hasNext()) {
                    categories = categories + ", ";
                }
            }

            //Creating the RestaurantDetailsResponseAddressState for the RestaurantDetailsResponseAddress
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id(UUID.fromString(restaurantEntity.getAddressId().getStateEntityId().getUuid()))
                    .stateName(restaurantEntity.getAddressId().getStateEntityId().getStateName());

            //Creating the RestaurantDetailsResponseAddress for the RestaurantList
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id(UUID.fromString(restaurantEntity.getAddressId().getUuid()))
                    .city(restaurantEntity.getAddressId().getCity())
                    .flatBuildingName(restaurantEntity.getAddressId().getFlatBuilNumber())
                    .locality(restaurantEntity.getAddressId().getLocality())
                    .pincode(restaurantEntity.getAddressId().getPincode())
                    .state(restaurantDetailsResponseAddressState);

            //Creating RestaurantList to add to list of RestaurantList
            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo())
                    .categories(categories)
                    .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                    .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .address(restaurantDetailsResponseAddress);

            //Adding it to the list
            restaurantLists.add(restaurantList);

        }

        //Creating the RestaurantListResponse by adding the list of RestaurantList
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }


    /* The method handles get Restaurant By Restaurant Id. It takes restaurant_id as the path variable.
  & produces response in RestaurantDetailsResponse and returns details of restaurant from the db. If error returns error code and error message.
  */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/restaurant/{restaurant_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse>getRestaurantByRestaurantId(@PathVariable(value = "restaurant_id") final String restaurantUuid)throws RestaurantNotFoundException{

        //Calls restaurantByUUID method of restaurantService to get the restaurant entity.
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantUuid);

        //Calls  getCategoriesByRestaurant to get categories of the corresponding restaurant.
        List<CategoryEntity> categoryEntities = categoryService.getCategoriesByRestaurant(restaurantUuid);

        //Creating category Lists  for the response
        List<CategoryList> categoryLists = new LinkedList<>();
        for (CategoryEntity categoryEntity:categoryEntities){  //Looping for each CategoryEntity in categoryEntities

            //Calls getItemsByCategoryAndRestaurant of itemService to get list of itemEntities.
            List<ItemEntity> itemEntities = itemService.getItemsByCategoryAndRestaurant(restaurantUuid ,categoryEntity.getUuid());
            //Creating Item List for the CategoryList.
            List<ItemList> itemLists = new LinkedList<>();
            itemEntities.forEach(itemEntity -> {
                ItemList itemList = new ItemList()
                        .id(UUID.fromString(itemEntity.getUuid()))
                        .itemName(itemEntity.getItemName())
                        .price(itemEntity.getPrice());


                ItemList.ItemTypeEnum itemTypeEnum =
                        (Integer.valueOf(itemEntity.getType().toString()) == 0)
                                ? ItemList.ItemTypeEnum.VEG
                                : ItemList.ItemTypeEnum.NON_VEG;

                itemList.setItemType(itemTypeEnum);

                itemLists.add(itemList);
            });

            //Creating new category list to add listof category list
            CategoryList categoryList = new CategoryList()
                    .itemList(itemLists)
                    .id(UUID.fromString(categoryEntity.getUuid()))
                    .categoryName(categoryEntity.getCategoryName());

            //adding to the categoryLists
            categoryLists.add(categoryList);

        }

        //Creating the RestaurantDetailsResponseAddressState for the RestaurantDetailsResponseAddress
        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                .id(UUID.fromString(restaurantEntity.getAddressId().getStateEntityId().getUuid()))
                .stateName(restaurantEntity.getAddressId().getStateEntityId().getStateName());

        //Creating the RestaurantDetailsResponseAddress for the RestaurantList
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                .id(UUID.fromString(restaurantEntity.getAddressId().getUuid()))
                .city(restaurantEntity.getAddressId().getCity())
                .flatBuildingName(restaurantEntity.getAddressId().getFlatBuilNumber())
                .locality(restaurantEntity.getAddressId().getLocality())
                .pincode(restaurantEntity.getAddressId().getPincode())
                .state(restaurantDetailsResponseAddressState);

        //Creating the RestaurantDetailsResponse by adding the list of categoryList and other details.
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse()
                .restaurantName(restaurantEntity.getRestaurantName())
                .address(restaurantDetailsResponseAddress)
                .averagePrice(restaurantEntity.getAveragePriceForTwo())
                .customerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()))
                .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                .id(UUID.fromString(restaurantEntity.getUuid()))
                .photoURL(restaurantEntity.getPhotoUrl())
                .categories(categoryLists);

        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse,HttpStatus.OK);
    }


}