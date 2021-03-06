package com.stumpner.mediadesk.folder;

import com.stumpner.mediadesk.media.IMultiLangObject;
import com.stumpner.mediadesk.core.database.sc.MultiLanguageService;
import net.stumpner.security.acl.AccessObject;

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
 * Date: 22.08.2007
 * Time: 07:48:47
 * To change this template use File | Settings | File Templates.
 */
public class FolderMultiLang extends Folder implements IMultiLangObject, AccessObject {

    private static final int ACL_TYPE_ID = 12;

    int usedLanguage = 0;
    String titleLng1 = "";
    String titleLng2 = "";
    String descriptionLng1 = "";
    String descriptionLng2 = "";

    boolean aclInerit = false; //Acls werden vererbt

    public void setUsedLanguage(int usedLanguage) {
        this.usedLanguage = usedLanguage;
    }

    public int getUsedLanguage() {
        return this.usedLanguage;
    }

    public String getTitle() {

        switch (getUsedLanguage()) {
            case MultiLanguageService.LNG1: return getTitleLng1();
            case MultiLanguageService.LNG2: return getTitleLng2();
            default: return super.getTitle();
        }
    }

    public String getDescription() {

        switch (getUsedLanguage()) {
            case MultiLanguageService.LNG1: return getDescriptionLng1();
            case MultiLanguageService.LNG2: return getDescriptionLng2();
            default: return super.getDescription();
        }
    }

    public String getTitleLng1() {
        return titleLng1;
    }

    public void setTitleLng1(String titleLng1) {
        this.titleLng1 = titleLng1;
    }

    public String getTitleLng2() {
        return titleLng2;
    }

    public void setTitleLng2(String titleLng2) {
        this.titleLng2 = titleLng2;
    }

    public String getDescriptionLng1() {
        return descriptionLng1;
    }

    public void setDescriptionLng1(String descriptionLng1) {
        this.descriptionLng1 = descriptionLng1;
    }

    public String getDescriptionLng2() {
        return descriptionLng2;
    }

    public void setDescriptionLng2(String descriptionLng2) {
        this.descriptionLng2 = descriptionLng2;
    }

    public int getAccessObjectSerialId() {
        return this.getFolderId();
    }

    public int getAccessObjectTypeId() {
        return ACL_TYPE_ID;
    }
}
