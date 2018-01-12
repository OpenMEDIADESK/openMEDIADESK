package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;
import com.stumpner.mediadesk.util.MaintenanceService;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.web.mvc.commandclass.settings.MaintenanceSettings;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.CategoryService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.image.category.CategoryMultiLang;
import com.stumpner.mediadesk.image.category.Category;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
 * Date: 22.05.2012
 * Time: 21:57:11
 * To change this template use File | Settings | File Templates.
 */
public class MaintenanceSettingsController extends SimpleFormControllerMd {

    public MaintenanceSettingsController() {

        this.setCommandClass(MaintenanceSettings.class);
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole= User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        MaintenanceSettings ms = new MaintenanceSettings();

        //Anzahl der Events
        FolderService folderService = new FolderService();
        ms.setEventCount(folderService.getFolderList(10000).size());        
        ms.setResetAclStatus(MaintenanceService.getInstance().getResetAclState());
        ms.setResetAclActive(MaintenanceService.getInstance().isResetAclActive());

        return ms;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        MaintenanceSettings set = (MaintenanceSettings)o;
        /*
        if (!set.getEtoc().equalsIgnoreCase("")) {
            //System.out.println("onSubmitEtoc: "+set.getEtoc());
            MaintenanceService.getInstance().etocStart(getUser(request).getUserId());
        } */
        if (!set.getAcl().equalsIgnoreCase("")) {
            if (!MaintenanceService.getInstance().isResetAclActive()) {
                MaintenanceService.getInstance().resetAclStart();
            }
        }

        if (!set.getCatviewauto().equalsIgnoreCase("")) {
            //System.out.println("Catviewauto: "+set.getEtoc());
            setCategoryViewToAuto();
        }

        //F�r Message OK
        request.setAttribute("headline","message.wartung");
        request.setAttribute("subheadline","message.wartung.sub");
        request.setAttribute("text","message.wartung.text");
        request.setAttribute("info","message.wartung.text");
        request.setAttribute("nextUrl","settings");
        request.setAttribute("redirectTo","setmaintenance");
        request.setAttribute("htmlCode","");
        request.setAttribute("subheadlineArgs",new String[] {""});

        request.setAttribute("model",model);

        return super.onSubmit(request, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void setCategoryViewToAuto() throws IOServiceException {
        CategoryService categoryService = new CategoryService();
        //Rekursiv alle Kategorien durchgehen:
        setCategoryViewToAuto(0,categoryService);
    }

    private void setCategoryViewToAuto(int categoryId, CategoryService cs) throws IOServiceException {
        List<CategoryMultiLang> categoryList = cs.getCategoryList(categoryId);
        for (CategoryMultiLang cat : categoryList) {
            cat.setDefaultview(Category.VIEW_UNDEFINED);
            cs.save(cat);
            setCategoryViewToAuto(cat.getCategoryId(),cs);
        }
    }

}
