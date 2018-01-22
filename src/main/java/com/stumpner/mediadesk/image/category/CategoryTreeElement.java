package com.stumpner.mediadesk.image.category;

import java.util.List;
import java.util.ArrayList;

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
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 11.07.2006
 * Time: 20:33:12
 * To change this template use File | Settings | File Templates.
 */
public class CategoryTreeElement extends Category {

    List subCategoryList = new ArrayList();

    public CategoryTreeElement(Category category) {
        this.setCategoryId(category.getCategoryId());
        this.setCatTitle(category.getCatTitle());
        this.setCatName(category.getCatName());
        this.setDescription(category.getDescription());
        this.setParent(category.getParent());
        this.setImageCount(category.getImageCount());
        this.setImageCountS(category.getImageCountS());
        this.setIcon(category.getIcon());
        this.setCategoryDate(category.getCategoryDate());
        this.setCreateDate(category.getCreateDate());
        this.setPrimaryIvid(category.getPrimaryIvid());
        this.setPublicAcl(category.isPublicAcl());
        this.setProtectedAcl(category.isProtectedAcl());
    }

    public List getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

}