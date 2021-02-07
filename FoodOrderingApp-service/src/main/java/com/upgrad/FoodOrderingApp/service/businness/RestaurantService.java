package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;
    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    private CategoryDao categoryDao;

    /* This method is to get restaurants By Rating and returns list of RestaurantEntity
If error throws exception with error code and error message.
*/
    public List<RestaurantEntity> restaurantsByRating() {

        //Calls restaurantsByRating of restaurantDao to get list of RestaurantEntity
        List<RestaurantEntity> restaurantEntities = restaurantDao.restaurantsByRating();
        return restaurantEntities;
    }


    /* This method is to get restaurants By Name and returns list of RestaurantEntity. its takes restaurant name as the input string.
 If error throws exception with error code and error message.
 */
    public List<RestaurantEntity> restaurantsByName(String restaurantName) throws RestaurantNotFoundException {
        if (restaurantName == null || restaurantName == "") { //Checking for restaunrant name to be null or empty to throw exception.
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        //Calls restaurantsByName of restaurantDao to get list of RestaurantEntity
        List<RestaurantEntity> restaurantEntities = restaurantDao.restaurantsByName(restaurantName);
        return restaurantEntities;
    }


    /* This method is to get restaurant By Category and returns list of RestaurantEntity. its takes categoryId as the input string.
   If error throws exception with error code and error message.
   */
    public List<RestaurantEntity> restaurantByCategory(String categoryId) throws CategoryNotFoundException {

        if (categoryId == null || categoryId == "") { //Checking for categoryId to be null or empty to throw exception.
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        //Calls getCategoryByUuid of categoryDao to get list of CategoryEntity
        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryId);

        if (categoryEntity == null) {//Checking for categoryEntity to be null or empty to throw exception.
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        //Calls getRestaurantByCategory of restaurantCategoryDao to get list of RestaurantCategoryEntity
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getRestaurantByCategory(categoryEntity);

        //Creating new restaurantEntity List and add only the restaurant for the corresponding category.
        List<RestaurantEntity> restaurantEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            restaurantEntities.add(restaurantCategoryEntity.getRestaurantId());
        });
        return restaurantEntities;
    }


    public RestaurantEntity restaurantByUUID(String uuid) throws RestaurantNotFoundException {
        if (uuid == null || uuid == "") { //Checking for restaurantUuid to be null or empty to throw exception.
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        RestaurantEntity restaurant = restaurantDao.restaurantByUUID(uuid);
        if (restaurant == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurant;
    }


}