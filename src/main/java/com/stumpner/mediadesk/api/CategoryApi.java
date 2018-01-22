package com.stumpner.mediadesk.api;

import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.ImageVersionService;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.DublicateEntry;
import com.stumpner.mediadesk.image.category.Category;
import com.stumpner.mediadesk.image.category.CategoryMultiLang;
import com.stumpner.mediadesk.image.ImageVersion;

import java.util.List;
import java.util.Iterator;

/*********************************************************
 Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

 openMEDIADESK is licensed under Apache License Version 2.0

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *********************************************************/
/**
 * User: stumpner
 * Date: 19.03.2008
 * Time: 11:54:40
 */
public class CategoryApi extends ApiBase {

    private final int CATEGORYEXIST = 1;
    private final int CATEGORYCREATE = 2;
    private final int CATEGORYDELETE = 3;
    private final int CATEGORYCHANGEDATE = 4;
    private final int REMOVEOBJECTSFROMCATEGORY = 5;
    private final int ADDOBJECTTOCATEGORY = 6;
    private final int GETOBJECTSFROMCATEGORY = 7;

    private final int DELETEMEDIAOBJECTFROMCATEGORY = 8;
    private final int GETCATEGORYIDBYFID = 9;

    public CategoryApi() {

        registerMethod("categoryExist", CATEGORYEXIST);
        registerMethod("categoryCreate", CATEGORYCREATE);
        registerMethod("categoryDelete", CATEGORYDELETE);
        registerMethod("categoryChangeDate", CATEGORYCHANGEDATE);
        registerMethod("removeObjectsFromCategory", REMOVEOBJECTSFROMCATEGORY);
        registerMethod("addObjectToCategory", ADDOBJECTTOCATEGORY);
        registerMethod("getObjectsFromCategory", GETOBJECTSFROMCATEGORY);

        registerMethod("deleteMediaObjectFromCategory",  DELETEMEDIAOBJECTFROMCATEGORY);
        registerMethod("getCategoryIdByFid", GETCATEGORYIDBYFID);

    }

    public String call(User user, String method, String[] parameter) {

        switch (getMethodId(method)) {
            case CATEGORYEXIST:
                return existsCategory(parameter);
            case CATEGORYCREATE:
                return categoryCreate(parameter);
            case CATEGORYDELETE:
                return categoryDelete(parameter);
            case CATEGORYCHANGEDATE:
                return categoryChangeDate(parameter);
            case REMOVEOBJECTSFROMCATEGORY:
                return removeObjectsFromCategory(parameter);
            case ADDOBJECTTOCATEGORY:
                return addObjectToCategory(parameter);
            case GETOBJECTSFROMCATEGORY:
                return getObjectsFromCategory(parameter);
            case DELETEMEDIAOBJECTFROMCATEGORY:
                return deleteMediaObjectFromCategory(parameter);
            case GETCATEGORYIDBYFID:
                return getCategoryIdByFid(parameter);
        }

        return "no value.";
    }

    /**
     * Gibt die Category-ID anhang der FID (Foreign-ID) zurück
     * @param parameter
     * @return
     */
    private String getCategoryIdByFid(String[] parameter) {
        String fid = parameter[0];
        CategoryService catService = new CategoryService();
        try {
            return String.valueOf(catService.getCategoryIdByFid(fid));
        } catch (ObjectNotFoundException e) {
            return "-1;ObjectNotFound";
        } catch (IOServiceException e) {
            return "ERROR";
        }
    }

    private String deleteMediaObjectFromCategory(String[] parameter) {
        int categoryId = Integer.parseInt(parameter[0]);
        int ivid = Integer.parseInt(parameter[1]);
        
        CategoryService categoryService = new CategoryService();
        categoryService.deleteImageFromCategory(categoryId,ivid);
        return "OK";
    }

    /**
     * F�gt ein Objekt (image) einer Kategorie zu
     * @param parameter
     * @return
     */
    private String addObjectToCategory(String[] parameter) {
        int ivid = Integer.parseInt(parameter[0]);
        int categoryId = Integer.parseInt(parameter[1]);

        CategoryService categoryService = new CategoryService();
        try {
            categoryService.addImageToCategory(categoryId,ivid);
            return "OK";
        } catch (DublicateEntry dublicateEntry) {
            dublicateEntry.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "ERROR";
        }


    }

    /**
     * Gibt eine List mit ivid's (Objekte) der angegebenen Kategorie zur�ck.
     * Die ausgabe erfolgt in einem String, mit ; getrennt
     * @param parameter
     * @return
     */
    private String getObjectsFromCategory(String[] parameter) {

        StringBuffer returnString = new StringBuffer();
        ImageVersionService imageService = new ImageVersionService();
        SimpleLoaderClass loaderClass = new SimpleLoaderClass();
        loaderClass.setId(Integer.parseInt(parameter[0]));
        List imageList = imageService.getCategoryImages(loaderClass);
        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            ImageVersion imageVersion = (ImageVersion)images.next();
            returnString.append(imageVersion.getIvid()+";");
        }

        return returnString.toString();

    }

    /**
     * Entfernt alle Objekte aus der angegeben Kategorie
     * @param parameter
     * @return
     */
    private String removeObjectsFromCategory(String[] parameter) {

        try {
            CategoryService categoryService = new CategoryService();
            ImageVersionService imageService = new ImageVersionService();
            SimpleLoaderClass loaderClass = new SimpleLoaderClass();
            loaderClass.setId(Integer.parseInt(parameter[0]));
            List categoryImages = imageService.getCategoryImages(loaderClass);
            Category category = categoryService.getCategoryById(loaderClass.getId());

            categoryService.deleteImagesFromCategory(category,categoryImages);

            return "OK";
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "FAILED;ObjectNotFoundException;";
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "FAILED;IOServiceException;";
        }

    }

    private String categoryChangeDate(String[] parameter) {


        try {
            Category category = getCategoryByPath(parameter[0]);
            return String.valueOf(category.getChangedDate().getTime());

        } catch (ObjectNotFoundException e) {
            return "ERROR";
        }

    }

    /**
     * Der Kategoriename kann der Name einer Kategorie sein oder ein Pfad
     * category1/subcategory/
     * Achtung: fangt die Pfadangabe mit einem / an, wird dieses weggeschnitten!
     * @param parameter
     * @return
     */
    private String existsCategory(String[] parameter) {

        String categoryName = parameter[0];

        if (categoryName.indexOf("/")==-1) {
            return existsCategoryName(categoryName);
        } else {
            if (categoryName.startsWith("/")) {
                //Vorausgehendes / wegschneiden
                categoryName = categoryName.substring(1);
            }
            return existsCategoryPath(categoryName);
        }

    }

    private String existsCategoryName(String categoryName) {

        boolean exist = false;
        CategoryService categoryService = new CategoryService();
        Category category = new Category();
        try {
            category = categoryService.getCategoryByName(categoryName);
            exist = true;
        } catch (ObjectNotFoundException e) {
            exist = false;
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (exist) {
            return "true;id="+category.getCategoryId();
        } else {
            return String.valueOf(exist);
        }

    }

    private String existsCategoryPath(String categoryPath) {

        CategoryService categoryService = new CategoryService();
        String[] pathToken = categoryPath.split("/");
        int categoryId = 0;

        for (int a=0;a<pathToken.length;a++) {

            List categoryList = categoryService.getCategoryList(categoryId);
            Iterator categories = categoryList.iterator();
            boolean found = false;
            while (categories.hasNext()) {
                Category category = (Category)categories.next();
                if (category.getCatName().equalsIgnoreCase(pathToken[a])) {
                    categoryId = category.getCategoryId();
                    found = true;
                    break;
                }
            }

            if (found == false) {
                return "false";
            }

        }

        return "true;id="+categoryId;

    }

    private Category getCategoryByPath(String categoryPath) throws ObjectNotFoundException {

        CategoryService categoryService = new CategoryService();
        return categoryService.getCategoryByPath(categoryPath);

    }

    private String categoryCreate(String[] parameter) {

        String categoryName = parameter[0];
        String categoryTitle = parameter[0];
        if (parameter.length==2) {
            categoryTitle = parameter[1];
        }
        CategoryService categoryService = new CategoryService();
        boolean success = false;
        //Pr�fen ob eine Pfadangabe oder Category-Path enthalten
        if (categoryName.indexOf("/")==-1) {
            //kein Pfad
            CategoryMultiLang category = new CategoryMultiLang();
            category.setCatName(categoryName);
            category.setCatTitle(categoryName);
            category.setCatTitleLng1(categoryTitle);
            category.setCatTitleLng2(categoryTitle);
            try {
                categoryService.addCategory(category);
                success = true;
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                success = false;
            }
        } else {
            //Pfad

            String[] pathToken = categoryName.split("/");
            int categoryId = 0;

            for (int a=0;a<pathToken.length;a++) {

                List categoryList = categoryService.getCategoryList(categoryId);
                Iterator categories = categoryList.iterator();
                boolean found = false;
                while (categories.hasNext()) {
                    Category category = (Category)categories.next();
                    if (category.getCatName().equalsIgnoreCase(pathToken[a])) {
                        categoryId = category.getCategoryId();
                        found = true;
                        if (a==pathToken.length-1) {
                            success = true;
                        }
                        break;
                    }
                }

                if (found == false) {
                    //Category anlegen:
                    CategoryMultiLang category = new CategoryMultiLang();
                    category.setCatName(pathToken[a]);
                    category.setCatTitle(pathToken[a]);
                    if (a==pathToken.length-1) {
                        //Wenn die "letzte" Kategorie in der Pfadangabe
                        category.setCatTitleLng1(categoryTitle);
                        category.setCatTitleLng2(categoryTitle);
                    } else {
                        category.setCatTitleLng1(pathToken[a]);
                        category.setCatTitleLng2(pathToken[a]);
                    }
                    category.setParent(categoryId);
                    a--; //Nochmals durchlaufen...
                    try {
                        categoryService.addCategory(category);
                        success = true;
                    } catch (IOServiceException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        success = false;
                    }
                }

            }

        }

        if (success) {
            return "OK";
        } else {
            return "ERROR";
        }
    }

    private String categoryDelete(String[] parameter) {

        CategoryService categoryService = new CategoryService();
        ImageVersionService imageService = new ImageVersionService();
        String categoryName = parameter[0];
        if (categoryName.indexOf("/")==-1) {
            //Kategorieangabe
            Category category = null;
            try {
                category = categoryService.getCategoryByName(categoryName);
                deleteCategory(category.getCategoryId());
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            //Kategorie-Pfadangabe
            String response = existsCategoryPath(categoryName);
            if (!response.startsWith("ERROR")) {
                String[] responseToken = response.split(";");
                String[] idToken = responseToken[1].split("=");
                int id = Integer.parseInt(idToken[1]);
                deleteCategory(id);
            } else {
                return "ERROR";
            }
        }
        //todo
        return "not implemented yet";
    }

    private void deleteCategory(int categoryId) {

        CategoryService categoryService = new CategoryService();
        ImageVersionService imageService = new ImageVersionService();
                try {
                    Category category = categoryService.getCategoryById(categoryId);
                    SimpleLoaderClass loaderClass = new SimpleLoaderClass();
                    loaderClass.setId(categoryId);
                    List imageList = imageService.getCategoryImages(loaderClass);
                    Iterator images = imageList.iterator();
                    while (images.hasNext()) {
                        ImageVersion image = (ImageVersion)images.next();
                        List categoryList = categoryService.getCategoryListFromImageVersion(image.getIvid());
                        if (categoryList.size()==1) {
                            imageService.deleteImageVersion(image);
                        }
                        categoryService.deleteImageFromCategory(category,image);
                    }
                    categoryService.deleteById(category.getCategoryId());
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

    }


}